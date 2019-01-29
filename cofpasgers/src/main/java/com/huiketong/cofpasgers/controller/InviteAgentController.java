package com.huiketong.cofpasgers.controller;

import com.aliyuncs.exceptions.ClientException;
import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.PointType;
import com.huiketong.cofpasgers.constant.UserType;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.jgim.JPUserService;
import com.huiketong.cofpasgers.json.data.ShareComData;
import com.huiketong.cofpasgers.json.data.ShareComDetailData;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.service.RedisService;
import com.huiketong.cofpasgers.util.MD5Util;
import com.huiketong.cofpasgers.util.ObjectUtils;
import com.huiketong.cofpasgers.util.RandomValidateCodeUtil;
import com.huiketong.cofpasgers.util.SerialGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import static com.huiketong.cofpasgers.util.AlicomDysmsUtil.sendSms;

@Controller
@RequestMapping(value = "/invite")
public class InviteAgentController {

    @Autowired
    RedisService redisService;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    CompanyUserRepository companyUserRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    UserRightsRepository userRightsRepository;
    @Autowired
    DefaultEnterRepository defaultEnterRepository;
    @Autowired
    ShareContextRepository shareContextRepository;
    @Autowired
    EarningsRepository earningsRepository;
    @Autowired
    IntegralRuleRepository integralRuleRepository;
    @Autowired
    PointDetailRepository pointDetailRepository;

    @GetMapping(value = "customer")
    public ModelAndView InviteCustomer(Map<String, Object> map) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "customer/invite");
        return mv;
    }

    @GetMapping(value = "order")
    public ModelAndView OrderCustomer() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "customer/order");
        return mv;
    }

    @GetMapping(value = "agent")
    public ModelAndView InviteAgent() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "agent/invite");
        return mv;
    }

    @PostMapping(value = "getInviteCompany")
    @ResponseBody
    public BaseJsonResponse GetInviteCompany(HttpServletRequest request) {
        String inviteCode = request.getParameter("inviteCode");
        BaseJsonResponse response = new BaseJsonResponse();
        if (inviteCode != null) {
            CompanyBindUser companyBindUser = companyUserRepository.findCompanyBindUserByInviteCode(inviteCode);
            if (companyBindUser != null) {
                Enterprise enterprise = enterpriseRepository.findEnterpriseById(companyBindUser.getCompanyId());
                if (enterprise != null) {
                    response.setMsg(enterprise.getEnterName());
                    response.setCode("1");
                } else {
                    response.setCode("100");
                    response.setMsg("该公司不存在");
                }
            } else {
                response.setCode("100");
                response.setMsg("该公司不存在");
            }
        } else {
            response.setMsg("邀请码不正确");
            response.setCode("100");
        }
        return response;
    }

    @PostMapping(value = "getInviteContent")
    @ResponseBody
    public BaseJsonResponse GetInviteContent(HttpServletRequest request) {
        String inviteCode = request.getParameter("inviteCode");
        BaseJsonResponse response = new BaseJsonResponse();
        if (inviteCode != null) {
            CompanyBindUser companyBindUser = companyUserRepository.findCompanyBindUserByInviteCode(inviteCode);
            if (companyBindUser != null) {
                Enterprise enterprise = enterpriseRepository.findEnterpriseById(companyBindUser.getCompanyId());
                if (enterprise != null) {
                    ShareContext shareContext = shareContextRepository.findShareContextByCompanyId(enterprise.getId());
                    ShareComDetailData data = new ShareComDetailData();
                    data.setContent(shareContext.getContext());
                    data.setTitle(shareContext.getTitle());
                    response.setData(data);
                    response.setCode("1");
                } else {
                    response.setCode("100");
                    response.setMsg("该公司不存在");
                }
            } else {
                response.setCode("100");
                response.setMsg("该公司不存在");
            }
        } else {
            response.setMsg("邀请码不正确");
            response.setCode("100");
        }
        return response;
    }

    /**
     * 获取手机验证码
     *
     * @param request
     */
    @PostMapping(value = "/recvcode")
    @ResponseBody
    @CrossOrigin
    public BaseJsonResponse GetCode(HttpServletRequest request) {
        String telphone = request.getParameter("recevier");
        BaseJsonResponse response = new BaseJsonResponse();
        try {
            if (ObjectUtils.isNotEmpty(telphone)) {
                Agent agent = agentRepository.findAgentByTelphone(telphone);
                if (ObjectUtils.isNotNull(agent)) {
                    response.setMsg("该号码已经注册过经纪人");
                    response.setCode("300");
                } else {
                    sendSms(telphone, request.getSession(), Constant.REGISTER_TEMPLATE);
                    String code = request.getSession().getAttribute("smscode").toString();
                    redisService.set("code" + telphone, code, 1800L);
                    response.setCode("1");
                    response.setMsg("获取验证码成功");
                }
            }
        } catch (ClientException e) {
            e.printStackTrace();
            response.setCode("100");
            response.setMsg("获取验证码失败");
        }
        return response;
    }

    @PostMapping(value = "getAgentName")
    @ResponseBody
    public BaseJsonResponse getAgentName(String inviteCode) {
        BaseJsonResponse response = new BaseJsonResponse();
        Agent agent = agentRepository.findAgentByInitCode(inviteCode);
        if (agent != null) {
            response.setCode(agent.getId().toString());
            response.setMsg(agent.getAgentName());
        } else {
            response.setMsg("无效用户");
            response.setCode("0");
        }
        return response;
    }

    /**
     * 用户注册
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/register")
    @CrossOrigin
    @ResponseBody
    public BaseJsonResponse Register(HttpServletRequest request) {
        JPUserService service = new JPUserService();
        BaseJsonResponse response = new BaseJsonResponse();
        String agentName = request.getParameter("agentName");
        String phone = request.getParameter("phone");//手机号
        String password = request.getParameter("password");
        String agentType = request.getParameter("agentType");
        String verifyCode = request.getParameter("verifyCode");//验证码
        String parentInvitationCode = request.getParameter("parentInvitationCode");//邀请码
        String agentCode = request.getParameter("phone");//
        if (!ObjectUtils.isEmpty(redisService.get("code" + phone))) {
            String code = redisService.get("code" + phone).toString();
            if (!ObjectUtils.isEmpty(code) && !ObjectUtils.isEmpty(verifyCode) && !verifyCode.equals(code)) {
                response.setMsg("验证码填写错误");
                response.setCode("100");
            } else {
                if (!ObjectUtils.isEmpty(agentName) && !ObjectUtils.isEmpty(phone) && !ObjectUtils.isEmpty(agentType) && !ObjectUtils.isEmpty(parentInvitationCode) && !ObjectUtils.isEmpty(agentCode)) {
                    Agent agent = new Agent();
                    CompanyBindUser companyBindUser = companyUserRepository.findCompanyBindUserByInviteCode(parentInvitationCode);

                    try {
                        String md5Pwd = MD5Util.getEncryptedPwd(password);
                        if (!ObjectUtils.isEmpty(companyBindUser)) {
                            Agent existAgent = agentRepository.findAgentByTelphoneAndCompanyId(phone, companyBindUser.getCompanyId());
                            UserRights rights = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(phone, 1, agentCode, 1);
                            if (rights != null && rights.getUserRight() == 2) {
                                response.setMsg("该号码已注册了系统用户");
                                response.setCode("3000");
                            } else if (existAgent != null) {
                                response.setMsg("经纪人已经存在");
                                response.setCode("100");
                            } else {
                                if (!ObjectUtils.isEmpty(companyBindUser.getInviteCode())) {
                                    Agent superAgent = agentRepository.findAgentByInitCode(companyBindUser.getInviteCode());
                                    if (!ObjectUtils.isEmpty(superAgent)) {
                                        agentRepository.addAgentNum(superAgent.getTelphone(), superAgent.getCompanyId());

                                        agent.setSuperId(superAgent.getId());
                                        agent.setSuperiorName(superAgent.getAgentName());

                                        CompanyBindUser existCompanyUser = companyUserRepository.findCompanyBindUserByUserTelAndCompanyId(phone,superAgent.getCompanyId());
                                        String inviteCode = SerialGeneratorUtil.GetRandomString(8);
                                        if (existCompanyUser == null) {
                                            CompanyBindUser newcompanyBindUser = new CompanyBindUser();
                                            newcompanyBindUser.setInviteCode(inviteCode);
                                            newcompanyBindUser.setUserTel(phone);
                                            newcompanyBindUser.setCompanyId(companyBindUser.getCompanyId());
                                            companyUserRepository.save(newcompanyBindUser);
                                        }

                                        agent.setCompanyId(companyBindUser.getCompanyId());
                                        agent.setRegDatetime(new Date());
                                        agent.setType(Integer.parseInt(agentType));
                                        agent.setTelphone(phone);
                                        agent.setAgentName(agentName);
                                        agent.setLoginUsername(agentCode);
                                        agent.setInitCode(inviteCode);
                                        service.registerUser("c"+inviteCode,"123456");
                                        agent.setPassword(md5Pwd);
                                        agent.setRealName("");
                                        agent.setInitAgentNam(0);
                                        agent.setDealCustomNum(0);
                                        agent.setBCertification(0);
                                        agent.setForbid(0);
                                        agent.setDeviceId("");
                                        agent.setPoints(0);
                                        agent.setAccountBalance(new BigDecimal(0));
                                        agent.setReconCustomNam(0);
                                        agent.setImposer(false);
                                        agent.setIdCard("");
                                        agent.setDrawlPad(null);

                                        DefaultEnter defaultEnter = new DefaultEnter();
                                        defaultEnter.setCompayId(companyBindUser.getCompanyId());
                                        Enterprise enterprise = enterpriseRepository.findEnterpriseById(companyBindUser.getCompanyId());
                                        if (enterprise != null) {
                                            defaultEnter.setCompayName(enterprise.getEnterName());
                                        }
                                        defaultEnter.setUserId(agentCode);
                                        defaultEnter.setUserTelphone(phone);
                                        Commission commission = commissionRepository.findCommissionByCompanyId(companyBindUser.getCompanyId());
                                        if (!ObjectUtils.isEmpty(commission)) {
                                            agentRepository.updateScore(commission.getInvitScore(), superAgent.getId(), superAgent.getCompanyId());
                                            if (ObjectUtils.isNotEmpty(commission.getScore())) {
                                                if (commission.getScore().compareTo(new BigDecimal(0)) == 1) {
                                                    Notice notice = new Notice();
                                                    notice.setAgentName(superAgent.getAgentName());
                                                    notice.setCompanyId(defaultEnter.getCompayId());
                                                    notice.setAddTime(new Date());
                                                    notice.setContext("恭喜" + superAgent.getAgentName() + "邀请成功,获得" + commission.getInvitScore() + "佣金");
                                                    notice.setScore(commission.getInvitScore());
                                                    noticeRepository.save(notice);

                                                    Earnings earnings = new Earnings();
                                                    earnings.setAddTime(new Date());
                                                    earnings.setDescript("邀请奖励");
                                                    earnings.setMoney(commission.getInvitScore());
                                                    earnings.setUserId(superAgent.getId());
                                                    earnings.setComId(commission.getCompanyId());
                                                    earnings.setType(2);
                                                    earningsRepository.save(earnings);
                                                    PointDetail pointDetail = new PointDetail();
                                                    pointDetail.setAddTime(new Date());
                                                    pointDetail.setCompanyId(superAgent.getCompanyId());
                                                    pointDetail.setDescript("邀请奖励");
                                                    pointDetail.setStatus(1);
                                                    pointDetail.setType(PointType.INVITE.ordinal());
                                                    pointDetail.setUserId(superAgent.getId());
                                                    IntegralRule integralRule = integralRuleRepository.findIntegralRuleByCompanyId(superAgent.getCompanyId());
                                                    if(ObjectUtils.isNotNull(integralRule))
                                                    {
                                                        pointDetail.setPoint(integralRule.getInviteIntegral());
                                                        pointDetailRepository.save(pointDetail);
                                                    }
                                                }
                                            }

                                        }
                                        saveUserRight(agentName, phone, agentCode);

                                        agentRepository.save(agent);
                                        defaultEnterRepository.save(defaultEnter);

                                        response.setCode("1");
                                        response.setMsg("注册成功");
                                    } else {
                                        response.setCode("100");
                                        response.setMsg("上级经纪人失效");
                                    }
                                }
                            }
                        } else {
                            response.setCode("100");
                            response.setMsg("邀请人失效");
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    response.setCode("100");
                    response.setMsg("信息不完整");
                }
            }
        } else {
            response.setMsg("请先获取验证码");
            response.setCode("100");
        }
        return response;
    }

    private void saveUserRight(String agentName, String phone, String agentCode) {
        UserRights existRight = userRightsRepository.findUserRightsByUserTel(phone);
        if (existRight == null) {
            UserRights rights = new UserRights();
            rights.setUserRight(UserType.AGENT.ordinal());
            rights.setUserName(agentName);
            rights.setUserTel(phone);
            rights.setLoginName(agentCode);
            rights.setRightName("经纪人");
            userRightsRepository.save(rights);
        }
    }

    @PostMapping(value = "/addCustomer")
    @CrossOrigin
    public BaseJsonResponse AddCustomer(HttpServletRequest request) {
        String custName = request.getParameter("custName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String occupation = request.getParameter("occupation");
        String style = request.getParameter("style");
        String type = request.getParameter("type");
        String area = request.getParameter("area");
        String budget = request.getParameter("budget");
        String merchantId = request.getParameter("merchantId");
        String agentId = request.getParameter("agentId");
        BaseJsonResponse response = new BaseJsonResponse();
        return response;
    }
}
