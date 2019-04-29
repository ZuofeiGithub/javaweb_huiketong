package com.huiketong.cofpasgers.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.UserType;
import com.huiketong.cofpasgers.constant.EnumLogin;
import com.huiketong.cofpasgers.entity.Agent;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.SystemUser;
import com.huiketong.cofpasgers.entity.UserRights;
import com.huiketong.cofpasgers.jni_native.HelloJni;
import com.huiketong.cofpasgers.repository.AgentRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.SystemUserRepository;
import com.huiketong.cofpasgers.repository.UserRightsRepository;
import com.huiketong.cofpasgers.util.MD5Util;
import com.huiketong.cofpasgers.util.RandomValidateCodeUtil;
import com.huiketong.cofpasgers.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {
    private int isWhich = 0;
    @Autowired
    private SystemUserRepository userRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    UserRightsRepository userRightsRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private boolean bPasswordCorrect = false;

    @GetMapping(value = "/login")
    //@GetMapping(value = "/")
    public ModelAndView Login(HttpSession session) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newlogin");
        session.setAttribute("userinfo", null);
        session.setAttribute("iswitch",null);
        //ModelAndView mv = new ModelAndView("cptsbpm/index");
        return mv;
    }

    @GetMapping(value = "/bkabout")
    public ModelAndView bkabout(){
        ModelAndView mv = new ModelAndView("cptsbpm/about");
        return mv;
    }

    @GetMapping(value = "/info")
    public ModelAndView info(){
        ModelAndView mv = new ModelAndView("cptsbpm/info");
        return mv;
    }

    @GetMapping(value = "admin")
    public ModelAndView Admin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws IOException {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "huiketong");
        HttpSession session = request.getSession();
        switch (isWhich) {
            case 0:
                response.sendRedirect("/login");
                break;
            case 1:
                SystemUser user = (SystemUser) session.getAttribute("userinfo");
                if (user == null) {
                    response.sendRedirect("/login");
                }
                break;
            case 2:
                Agent agent = (Agent) session.getAttribute("userinfo");
                if (agent == null) {
                    response.sendRedirect("/login");
                }
                break;
            case 3:
                Enterprise enterprise = (Enterprise) session.getAttribute("userinfo");
                if (enterprise == null) {
                    response.sendRedirect("/login");
                }
                break;
        }
        return mv;
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public String validLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map, HttpSession session) {
        String telphone = request.getParameter("telphone");
        String passward = request.getParameter("passward");
        String verif_code = request.getParameter("verif_code");
        if (verif_code != null) {
            verif_code = verif_code.toUpperCase();
        }
        JSONObject json = new JSONObject();
        if (telphone != null) {
            UserRights userRights3 = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(telphone, 3,telphone,3);
            UserRights userRights2 = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(telphone, 2,telphone,2);
            try {
                String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
                if (random == null) {
                    json.clear();
                    json.put("islogin", EnumLogin.CODENOUSED.ordinal());
                }else{
                    if (userRights2 != null && userRights2.getUserRight().equals(UserType.SYSTEM.ordinal())) {
                        SystemUser user = userRepository.findSystemUserByTelphoneOrLoginName(telphone, telphone);
                        if (user != null) {
                            session.setAttribute("userinfo", user);
                            session.setAttribute("iswitch",1);
                            isWhich = 1;
                            if (MD5Util.validPassword(passward, user.getLoginPassward())) {
                                bPasswordCorrect = true;
                            } else {
                                json.clear();
                                json.put("islogin", EnumLogin.PASSWORDERROR.ordinal());
                                return JSON.toJSONString(json);
                            }
                        } else {
                            bPasswordCorrect = false;
                            json.clear();
                            json.put("islogin", EnumLogin.NOUSER.ordinal());
                            return JSON.toJSONString(json);
                        }
                        if (random.equals(verif_code) && bPasswordCorrect) {
                            json.clear();
                            json.put("islogin", EnumLogin.LOGINSUCCESS.ordinal());
                            json.put("telphone", telphone);
                            json.put("username", user.getUsername());
                            json.put("rightname",userRights2.getRightName());
                            json.put("usertype", UserType.SYSTEM.ordinal());
                        } else {
                            json.clear();
                            json.put("islogin", EnumLogin.LOGINFAILED.ordinal());
                        }
                    } else if (userRights3 != null && userRights3.getUserRight().equals(UserType.AGENT.ordinal())) {
//                Agent agent = agentRepository.findAgentByTelphone(telphone);
//                if(agent != null){
//                    session.setAttribute("userinfo",agent);
//                    isWhich = 2;
//                    if(MD5Util.validPassword(passward,agent.getPassword()))
//                    {
//                        bPasswordCorrect = true;
//                    }else{
//                        json.clear();
//                        json.put("islogin", EnumLogin.PASSWORDERROR.ordinal());
//                        return JSON.toJSONString(json);
//                    }
//                }else{
//                    bPasswordCorrect = false;
//                    json.clear();
//                    json.put("islogin",EnumLogin.NOUSER.ordinal());
//                    return JSON.toJSONString(json);
//                }
//                if(random.equals(verif_code)&&bPasswordCorrect){
//                    json.clear();
//                    json.put("islogin",EnumLogin.LOGINSUCCESS.ordinal());
//                    String accessToken = TokenUtils.createJwtToken(telphone);
//                    agentRepository.updateAccesstoken(accessToken,telphone);
//                    json.put("telphone",agent.getTelphone());
//                    json.put("username",agent.getAgentName());
//                    json.put("usertype",UserType.AGENT.ordinal());
//                }else {
//                    json.clear();
//                    json.put("islogin",EnumLogin.LOGINFAILED.ordinal());
//                }
                        json.clear();
                        json.put("islogin", EnumLogin.NOUSER.ordinal());
                    } else if (userRights3 != null && (userRights3.getUserRight().equals(UserType.COMPANY.ordinal()))) {
                        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
                        if (enterprise != null) {
                            if(enterprise.getEnterStatus() == 0){
                                json.clear();
                                json.put("islogin",EnumLogin.AUTHFIRST.ordinal());
                            }else{
                                session.setAttribute("userinfo", enterprise);
                                session.setAttribute("iswitch",3);
                                isWhich = 3;
                                if (MD5Util.validPassword(passward, enterprise.getEnterLoginPwd())) {
                                    bPasswordCorrect = true;
                                } else {
                                    json.clear();
                                    json.put("islogin", EnumLogin.PASSWORDERROR.ordinal());
                                    return JSON.toJSONString(json);
                                }

                                if (random != null && random.equals(verif_code) && bPasswordCorrect) {
                                    json.clear();
                                    json.put("islogin", EnumLogin.LOGINSUCCESS.ordinal());
                                    json.put("username", enterprise.getEnterName());
                                    json.put("telphone", enterprise.getEnterLoginName());
                                    json.put("headphoto",enterprise.getEnterLogo());
                                    json.put("usertype", UserType.COMPANY.ordinal());
                                    json.put("rightname",userRights3.getRightName());
                                } else {
                                    json.clear();
                                    json.put("islogin", EnumLogin.LOGINFAILED.ordinal());
                                }
                            }
                        } else {
                            bPasswordCorrect = false;
                            json.clear();
                            json.put("islogin", EnumLogin.NOUSER.ordinal());
                            return JSON.toJSONString(json);
                        }

                    } else {
                        bPasswordCorrect = false;
                        json.clear();
                        json.put("islogin", EnumLogin.NOUSER.ordinal());
                        return JSON.toJSONString(json);
                    }
                }
            } catch (Exception e) {
                logger.error("验证码校验失败", e);
            }
        }
        return JSON.toJSONString(json);
    }

    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("login");
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>>   ", e);
        }
    }

    @PostMapping(value = "isJurisdiction")
    @ResponseBody
    public String Jurisdiction(HttpServletRequest request) {
        String telphone = request.getParameter("telphone");
        UserRights rights1 = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(telphone,1, telphone,1);
        UserRights rights2 = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(telphone,2, telphone,2);
        UserRights rights3 = userRightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(telphone,3, telphone,3);
        if (rights2 != null && rights2.getUserRight() == 2) {//系统管理员
            return "system";
        } else if (rights3 != null && rights3.getUserRight() == 3) //公司
        {
            return "company";
        } else if (rights1 != null && rights1.getUserRight() == 1) {                       //经纪人
            return "agent";
        } else {
            return "false";
        }
    }
}
