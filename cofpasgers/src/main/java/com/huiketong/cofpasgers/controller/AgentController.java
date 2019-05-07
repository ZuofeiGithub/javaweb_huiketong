package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.*;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.jgim.JPUserService;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.service.IPageQueryService;
import com.huiketong.cofpasgers.util.MD5Util;
import com.huiketong.cofpasgers.util.ObjectUtils;
import com.huiketong.cofpasgers.util.SerialGeneratorUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
public class AgentController {
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private UserRightsRepository userRightsRepository;
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    @Autowired
    private CompanyUserRepository companyUserRepository;
    @Autowired
    DefaultEnterRepository defaultEnterRepository;
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    IntegralRuleRepository integralRuleRepository;
    @Autowired
    PointDetailRepository pointDetailRepository;

    /**
     * 一般查询
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value = "agentinfo/{userId}")
    @ResponseBody
    public JSONData Agent(@PathVariable("userId") String userId, Integer page, Integer limit) {
        JSONData response = new JSONData();
        if (userId != null && page != null && limit != null) {
            Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(userId);
            if (enterprise != null) {
                List<Agent> agentList = agentRepository.findPagesAgent(enterprise.getId(), (page - 1) * limit, limit);
                if (agentList.size() > 0) {
                    response.setCode(0);
                    response.setData(agentList);
                    response.setCount((int) agentRepository.counts(enterprise.getId()));
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
        } else {
            response.setCode(0);
            response.setData(new ArrayList<>());
            response.setCount(0);
            response.setMsg("");
        }

        return response;
    }

    @PostMapping(value = "get_company_agent")
    @ResponseBody
    public Object getAgent(HttpServletRequest request) {
        List<Agent> agentList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String user_id = request.getParameter("user_id");
        UserRights rights = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(user_id, 1, user_id, 1);
        if (rights != null && rights.getUserRight() == 1)//代理
        {
            Agent agent = agentRepository.findAgentByTelphoneOrLoginUsername(user_id, user_id);
            if (agent != null) {
                agentList = agentRepository.findAgentsBySuperIdOrTopId(agent.getSuperId(), agent.getTopId());
            }
        } else if (rights != null && rights.getUserRight() == 2) {//系统超级管理员
            agentList = agentRepository.findAll();
        } else if (rights != null && rights.getUserRight() == 3) {//公司
            Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
            agentList = agentRepository.findAgentList(enterprise.getId());
        } else {
            agentList = null;
        }
        map.put("data", agentList);
        return map;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping(value = "agent")
    public ModelAndView PageAgent() {
        ModelAndView mv = new ModelAndView(URL.AGENTMANAGER);
        return mv;
    }

    @GetMapping(value = "addagent")
    public ModelAndView AddAgent(String user_id, Map<String, Object> map) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "addagent");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if (enterprise != null) {
            map.put("enter_name", enterprise.getEnterName());
            map.put("enter_id", enterprise.getId());
        }
        return mv;
    }

    @PostMapping(value = "addagent")
    @ResponseBody
    public BaseJsonResponse AddAgent(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        BaseJsonResponse response = new BaseJsonResponse();
        JPUserService service = new JPUserService();
        //需要添加的经纪人
        Agent newagent = new Agent();
        String agentType = request.getParameter("agent_type");
        String agentName = request.getParameter("agent_name");
        String loginName = request.getParameter("agent_telphone");
        String agentTelphone = request.getParameter("agent_telphone");
        String loginPass = request.getParameter("login_pass");
        String loginrePass = request.getParameter("login_repass");
        String company = request.getParameter("company");

        if (agentType != null && !agentType.isEmpty() && agentName != null && !agentName.isEmpty() &&
                loginName != null && !loginName.isEmpty() && agentTelphone != null
                && !agentTelphone.isEmpty() && loginPass != null && !loginPass.isEmpty()
                && loginrePass != null && !loginrePass.isEmpty() && company != null && !company.isEmpty()) {
            String loginpassword = MD5Util.getEncryptedPwd(loginPass);
            if (loginPass != null && loginrePass != null && !loginPass.equals(loginrePass)) {
                response.setCode("100");
                response.setMsg("密码输入不一致");
                return response;
            }
            Agent exsitagent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(agentTelphone, Integer.parseInt(company), loginName, Integer.parseInt(company));
            if (exsitagent != null) {
                response.setCode("101");
                response.setMsg("用户已经存在");
            } else {
                //新添加的经济人绑定对应的公司
                Enterprise enterprise = enterpriseRepository.findEnterpriseById(Integer.parseInt(company));

                if (enterprise != null) {
                    DefaultEnter existEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(loginName, loginName);
                    if (existEnter != null) {
                        defaultEnterRepository.updateCompanyid(enterprise.getId(), enterprise.getEnterName(), loginName);
                    } else {
                        DefaultEnter defaultEnter = new DefaultEnter();
                        defaultEnter.setCompayName(enterprise.getEnterName());
                        defaultEnter.setCompayId(enterprise.getId());
                        defaultEnter.setUserTelphone(agentTelphone);
                        defaultEnter.setUserId(loginName);
                        defaultEnterRepository.save(defaultEnter);
                    }

                    newagent.setType(Integer.parseInt(agentType));
                    newagent.setAgentName(agentName);
                    newagent.setTelphone(agentTelphone);
                    newagent.setLoginUsername(loginName);
                    newagent.setPassword(loginpassword);
                    newagent.setBCertification(0);
                    newagent.setSuperiorName("");
                    newagent.setSuperId(0);
                    newagent.setTopName("");
                    newagent.setTopId(0);
                    newagent.setInitAgentNam(0);
                    newagent.setReconCustomNam(0);
                    newagent.setPoints(0);
                    newagent.setCardPhoto("");
                    newagent.setDealCustomNum(0);
                    newagent.setForbid(0);
                    newagent.setDrawlPad(MD5Util.getEncryptedPwd("123456"));
                    newagent.setIdCard("");
                    newagent.setImposer(false);
                    newagent.setInitAgentNam(0);
                    newagent.setRealName("");
                    newagent.setDeviceId("");
                    newagent.setAccountBalance(new BigDecimal(0));
                    newagent.setCompanyId(Integer.parseInt(company));
                    newagent.setAvatar("");
                    String inviteCode = SerialGeneratorUtil.GetRandomString(8);
                    newagent.setInitCode(inviteCode);
                    service.registerUser("c" + inviteCode, "123456");
                    newagent.setRegDatetime(new Date());
                    agentRepository.save(newagent);
                    CompanyBindUser existCompanyUser = companyUserRepository.findCompanyBindUserByUserTelAndCompanyId(agentTelphone, enterprise.getId());
                    if (existCompanyUser == null) {
                        CompanyBindUser companyBindUser = new CompanyBindUser();
                        companyBindUser.setCompanyId(enterprise.getId());
                        companyBindUser.setUserTel(agentTelphone);
                        companyBindUser.setInviteCode(inviteCode);
                        companyUserRepository.save(companyBindUser);
                    }
                    UserRights exsitUserRight = userRightsRepository.findUserRightsByUserTel(agentTelphone);
                    if (exsitUserRight == null) {
                        UserRights userRights = new UserRights();
                        userRights.setUserRight(UserType.AGENT.ordinal());
                        userRights.setUserName(agentName);
                        userRights.setUserTel(agentTelphone);
                        userRights.setLoginName(loginName);
                        userRightsRepository.save(userRights);
                    }
                    response.setMsg("添加经纪人成功");
                    response.setCode("1");
                } else {
                    response.setCode("321");
                    response.setMsg("公司不存在");
                }
            }
        } else {
            response.setMsg("信息不完整");
            response.setCode("301");
        }
        return response;
    }

    @PostMapping("removeagent")
    @ResponseBody
    public BaseJsonResponse removeAgent(Integer id) {
        BaseJsonResponse response = new BaseJsonResponse();
        Agent agent = agentRepository.findAgentById(id);
        if (ObjectUtils.isNotNull(agent)) {
            if (agent.getForbid() == 1) {
                response.setMsg("用户已经删除").setCode("3");
            } else {
                try {
                    agentRepository.delAgent(1, id);
                    response.setCode("1").setMsg("删除成功");
                } catch (Exception e) {
                    response.setMsg("删除失败").setCode("2");
                }
            }
        }
        return response;
    }

    @PostMapping("authuser")
    @ResponseBody
    public BaseJsonResponse authUser(Integer id) {
        BaseJsonResponse response = new BaseJsonResponse();
        Agent agent = agentRepository.findAgentById(id);
        if (ObjectUtils.isNotNull(agent)) {
            if (agent.getBCertification() == 1) {
                response.setMsg("用户已认证").setCode("3");
            } else {
                try {
                    agentRepository.authUser(1, id);
                    response.setCode("1").setMsg("认证成功");
                    PointDetail pointDetail = new PointDetail();
                    pointDetail.setAddTime(new Date());
                    pointDetail.setCompanyId(agent.getCompanyId());
                    pointDetail.setDescript("认证积分");
                    pointDetail.setStatus(1);
                    pointDetail.setType(PointType.AUTH.ordinal());
                    pointDetail.setUserId(agent.getId());
                    IntegralRule integralRule = integralRuleRepository.findIntegralRuleByCompanyId(agent.getCompanyId());
                    if (ObjectUtils.isNotNull(integralRule)) {
                        pointDetail.setPoint(integralRule.getAuthIntegral());
                        pointDetailRepository.save(pointDetail);
                    }
                } catch (Exception e) {
                    response.setMsg("认证失败").setCode("2");
                }
            }
        }
        return response;
    }

    @GetMapping("showCustomer")
    public Object showCustomer(Integer id, Integer companyId) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "recustomer");
        return mv;
    }

    @GetMapping("agentcustomer")
    @ResponseBody
    public JSONData showAgentCustomers(Integer id, Integer page, Integer limit) {
        JSONData data = new JSONData();
        if (ObjectUtils.isNotEmpty(id) && ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(limit)) {
            List<Customer> customerList = customerRepository.findAllCustomers(id, (page - 1) * limit, limit);
            if (customerList.size() > 0) {
                data.setCount(customerList.size()).setData(customerList).setCode(0).setMsg("");
            } else {
                data.setCount(0).setData(new ArrayList<>()).setCode(0).setMsg("");
            }
        } else {
            data.setCount(0).setData(new ArrayList<>()).setCode(0).setMsg("");
        }

        return data;
    }


    @GetMapping("agentsort")
    public String agentSort(){
        return URL.AGENTSORT;
    }

}
