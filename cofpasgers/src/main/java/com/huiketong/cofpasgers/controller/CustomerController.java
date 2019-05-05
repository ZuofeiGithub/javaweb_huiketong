package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.constant.PointType;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    EarningsRepository earningsRepository;
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    IntegralRuleRepository integralRuleRepository;
    @Autowired
    PointDetailRepository pointDetailRepository;

    @GetMapping(value = "customer")
    public ModelAndView Customer(Map<String, Object> map) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "customer_info");
        return mv;
    }

    @GetMapping(value = "getcustomerinfo/{userId}")
    @ResponseBody
    public JSONData GetCusInfo(@PathVariable("userId") String userId, Integer page, Integer limit) {
        JSONData response = new JSONData();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(userId);
        if (enterprise != null) {
            List<Customer> customerList = customerRepository.findCustomerPages(enterprise.getId(), (page - 1) * limit, limit);
            if (customerList.size() > 0) {
                response.setCode(0);
                response.setData(customerList);
                long count = customerRepository.counts(enterprise.getId());
                response.setCount((int) count);
                response.setMsg("");
            } else {
                response.setCode(0);
                response.setData(new ArrayList<>());
                response.setCount(0);
                response.setMsg("");
            }
        } else {
            response.setCode(0);
            response.setData(new ArrayList<>());
            response.setCount(0);
            response.setMsg("");
        }

        return response;
    }

    @PostMapping(value = "add_customer")
    @ResponseBody
    public BaseJsonResponse addCustomer(String custName, String phone, String address, String occupation, String sex, Integer agentId) {
        BaseJsonResponse response = new BaseJsonResponse();

        if (custName != null && phone != null && address != null && sex != null && agentId != null) {
            Customer existCustomer = customerRepository.findCustomerByCustomerNameAndTelphone(custName, phone);
            if (existCustomer != null) {
                response.setCode("2");
                response.setMsg("客户已经报备");
            } else {
                Customer customer = new Customer();
                customer.setCustomerName(custName);
                customer.setTelphone(phone);
                customer.setDetailAddress(address);
                customer.setRenovRemark(occupation);
                customer.setSex(sex);
                customer.setAgentId(agentId);
                customer.setVerifyStatus(1);
                customer.setRecomDatetime(new Date());
                Agent agent = agentRepository.findAgentById(agentId);
                if (agent != null) {
                    agentRepository.updateCustomeNum(agentId, agent.getCompanyId());
                    customer.setCompanyId(agent.getCompanyId());
                    if (ObjectUtils.isNotEmpty(agent.getAgentName())) {
                        customer.setAgentName(agent.getAgentName());
                    }
                    customerRepository.save(customer);
                    response.setMsg("报备客户成功");
                    response.setCode("1");
                    PointDetail pointDetail = new PointDetail();
                    pointDetail.setAddTime(new Date());
                    pointDetail.setCompanyId(agent.getCompanyId());
                    pointDetail.setDescript("推荐客户奖励");
                    pointDetail.setStatus(1);
                    pointDetail.setType(PointType.RECOM.ordinal());
                    pointDetail.setUserId(agent.getId());
                    IntegralRule integralRule = integralRuleRepository.findIntegralRuleByCompanyId(agent.getCompanyId());
                    if (ObjectUtils.isNotNull(integralRule)) {
                        pointDetail.setPoint(integralRule.getRecomIntegral());
                    } else {
                        pointDetail.setPoint(0);
                    }
                    pointDetailRepository.save(pointDetail);
                } else {
                    response.setCode("0");
                    response.setMsg("经纪人不存在,无法报备");
                }
            }
        } else {
            response.setCode("0");
            response.setMsg("信息不完整");
        }
        return response;
    }

    @GetMapping(value = "modifycuststatus")
    public ModelAndView modifyCustStatus(Integer id, Map<String, Object> map) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "modifystatus");
        Customer customer = customerRepository.findCustomerById(id);
        if (customer != null) {
            map.put("status", customer.getVerifyStatus());
        }
        return mv;
    }

    @PostMapping(value = "modifycuststatus")
    @ResponseBody
    public BaseJsonResponse modify(Integer id, Integer status, String user_id, BigDecimal signprice, String reason) {
        BaseJsonResponse response = new BaseJsonResponse();
        if (id != null && status != null) {
            Customer customer = customerRepository.findCustomerById(id);
            if (ObjectUtils.isNotNull(customer)) {
                if (ObjectUtils.isNotEmpty(customer.getVerifyStatus())) {
                    if (status == customer.getVerifyStatus()) {
                        response.setCode("300").setMsg("该客户认证过此操作");
                    } else if (status < customer.getVerifyStatus()) {
                        response.setCode("200").setMsg("您无法执行该动作");
                    } else {
                        if (status == 4) {
                            Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
                            if (ObjectUtils.isNotNull(enterprise)) {
                                Commission commission = commissionRepository.findCommissionByCompanyId(enterprise.getId());
                                if (ObjectUtils.isNotNull(commission)) {
                                    if (commission.getScore().compareTo(new BigDecimal(0)) == 1) {
                                        Earnings earnings = new Earnings();
                                        earnings.setMoney(commission.getScore());
                                        earnings.setType(3);
                                        earnings.setComId(enterprise.getId());
                                        earnings.setAddTime(new Date());
                                        earnings.setDescript("量房佣金发放");
                                        if (ObjectUtils.isNotNull(customer)) {
                                            earnings.setUserId(customer.getAgentId());
                                            earnings.setCusname(customer.getCustomerName());
                                            earnings.setAngentname(customer.getAgentName());
                                            earnings.setCusnamePhone(customer.getTelphone());
                                            earnings.setCommissionpercentage("100%");
                                            earnings.setCommissionlevel("1级佣金");
                                        }
                                        earningsRepository.save(earnings);
                                        Notice notice = new Notice();
                                        Agent agent = agentRepository.findAgentById(customer.getAgentId());
                                        if (agent != null) {
                                            notice.setAgentName(agent.getAgentName());
                                            notice.setCompanyId(customer.getCompanyId());
                                            notice.setContext(agent.getAgentName() + "的客户已量房,获得" + commission.getScore() + "佣金");
                                            notice.setScore(commission.getScore());
                                            notice.setAddTime(new Date());
                                            noticeRepository.save(notice);
                                            agentRepository.updateScore(commission.getScore(), agent.getId(), agent.getCompanyId());
                                        }

                                        response.setCode("4").setMsg("量房佣金已发放");
                                        customerRepository.updateVerifyStatus(status, id);

                                    } else {
                                        response.setCode("4").setMsg("暂无佣金");
                                    }
                                } else {
                                    response.setMsg("佣金没有设置").setCode("100");
                                }
                            }
                        } else if (status == 8) {
                            if (ObjectUtils.isNotEmpty(signprice)) {
                                Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
                                if (ObjectUtils.isNotNull(enterprise)) {
                                    Commission commission = commissionRepository.findCommissionByCompanyId(enterprise.getId());
                                    if (ObjectUtils.isNotNull(commission)) {
                                        if (ObjectUtils.isNotNull(customer)) {
                                            try {
                                                BigDecimal mprice = signprice.setScale(2);
                                                customerRepository.updateSignPrice(mprice, customer.getId());
                                                Agent agent = agentRepository.findAgentById(customer.getAgentId());
                                                BigDecimal money1 = new BigDecimal(0);
                                                BigDecimal money2 = new BigDecimal(0);
                                                float present1 = 0;
                                                float present2 = 0;
                                                if (ObjectUtils.isNotNull(agent)) {
                                                    if (agent.getType() == 0 || agent.getType() == 1) {
                                                        present1 = commission.getFirstPercentage();
                                                        money1 = signprice.multiply(new BigDecimal(commission.getFirstPercentage() / 100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                                    } else {
                                                        present1 = commission.getOfirstPercentage();
                                                        money1 = signprice.multiply(new BigDecimal(commission.getOfirstPercentage() / 100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                                    }
                                                    Earnings earnings = new Earnings();
                                                    earnings.setMoney(money1);
                                                    earnings.setType(4);
                                                    earnings.setComId(enterprise.getId());
                                                    earnings.setAddTime(new Date());
                                                    earnings.setDescript("已签合同佣金发放");
                                                    earnings.setUserId(customer.getAgentId());
                                                    earnings.setCusname(customer.getCustomerName());
                                                    earnings.setAngentname(customer.getAgentName());
                                                    earnings.setCusnamePhone(customer.getTelphone());
                                                    earnings.setCommissionpercentage("" + present1 + "%");
                                                    earnings.setCommissionlevel("1级佣金");
                                                    agentRepository.updateScore(money1, customer.getAgentId(), customer.getCompanyId());
                                                    earningsRepository.save(earnings);
                                                    if (ObjectUtils.isNotNull(agent)) {
                                                        Notice notice = new Notice();
                                                        notice.setAgentName(agent.getAgentName());
                                                        notice.setCompanyId(customer.getCompanyId());
                                                        notice.setContext(agent.getAgentName() + "的客户已签约,获得佣金" + money1);
                                                        notice.setScore(money1);
                                                        notice.setAddTime(new Date());
                                                        noticeRepository.save(notice);
                                                        agentRepository.updateDealNum(agent.getId(), agent.getCompanyId());
                                                    }
                                                    Agent superAgent = agentRepository.findSuperById(customer.getAgentId(), customer.getCompanyId());
                                                    if (ObjectUtils.isNotNull(superAgent)) {
                                                        if (superAgent.getType() == 0 || superAgent.getType() == 1) {
                                                            present2 = commission.getSecondPercentage();
                                                            money2 = signprice.multiply(new BigDecimal(commission.getSecondPercentage() / 100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                                        } else {
                                                            present2 = commission.getOsecondPercentage();
                                                            money2 = signprice.multiply(new BigDecimal(commission.getOsecondPercentage() / 100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                                        }
                                                        Earnings earnings1 = new Earnings();
                                                        earnings1.setUserId(superAgent.getId());
                                                        earnings1.setCusname(customer.getCustomerName());
                                                        earnings1.setAngentname(customer.getAgentName());
                                                        earnings1.setCusnamePhone(customer.getTelphone());
                                                        earnings1.setCommissionpercentage("" + present2 + "%");
                                                        earnings1.setCommissionlevel("2级佣金");
                                                        earnings1.setComId(customer.getCompanyId());
                                                        earnings1.setDescript(agent.getAgentName() + "的客户已签合同");
                                                        earnings1.setAddTime(new Date());
                                                        earnings1.setMoney(money2);
                                                        earnings1.setType(4);
                                                        agentRepository.updateScore(money2, superAgent.getId(), superAgent.getCompanyId());
                                                        earningsRepository.save(earnings1);
                                                        //下线客户成功签约
//                                                        Notice notice = new Notice();
//                                                        notice.setAgentName(superAgent.getAgentName());
//                                                        notice.setCompanyId(customer.getCompanyId());
//                                                        notice.setContext(superAgent.getAgentName()+"的下线客户已签约,获得佣金"+money2);
//                                                        notice.setScore(money2);
//                                                        notice.setAddTime(new Date());
//                                                        noticeRepository.save(notice);
                                                    }
                                                    response.setMsg("签单佣金发放成功").setCode("8");
                                                    customerRepository.updateVerifyStatus(status, id);
                                                } else {
                                                    response.setCode("500").setMsg("经纪人不存在");
                                                }

                                            } catch (Exception e) {
                                                response.setMsg("签单失败").setCode("2");
                                            }

                                        }

                                    }
                                } else {
                                    response.setMsg("请填写成交总价").setCode("2");
                                }
                            } else {
                                response.setMsg("请先填写签单价格").setCode("200");
                            }
                        } else if (status == 9) {
                            if (ObjectUtils.isNotEmpty(reason)) {
                                try {
                                    customerRepository.updateReason(reason, customer.getId());
                                    customerRepository.updateVerifyStatus(status, id);
                                    response.setMsg("状态修改成功").setCode("9");
                                } catch (Exception e) {
                                    response.setCode("9").setMsg("修改失败");
                                }
                            } else {
                                response.setMsg("拒绝理由不能为空").setCode("9");
                            }
                        } else {
                            response.setMsg("状态修改成功").setCode("1");
                            customerRepository.updateVerifyStatus(status, id);
                        }
                    }
                } else {
                    response.setMsg("状态不正常").setCode("200");
                }
            }
        } else {
            response.setCode("0");
            response.setMsg("修改失败");
        }
        return response;
    }
}
