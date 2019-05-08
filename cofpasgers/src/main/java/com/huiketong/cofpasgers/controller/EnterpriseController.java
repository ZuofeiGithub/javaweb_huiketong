package com.huiketong.cofpasgers.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huiketong.cofpasgers.constant.AgentType;
import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.constant.UserType;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.util.DateUtils;
import com.huiketong.cofpasgers.util.MD5Util;
import com.huiketong.cofpasgers.util.ObjectUtils;
import com.huiketong.cofpasgers.util.SerialGeneratorUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
/**
 * 企业信息
 */
public class EnterpriseController {
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    UserRightsRepository userRightsRepository;
    @Autowired
    CompanyUserRepository companyUserRepository;
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    BannerRepository bannerRepository;
    @Autowired
    IntegralRuleRepository integralRuleRepository;
    @Autowired
    AgentRepository agentRepository;

    @GetMapping(value = "enterprise")
    public ModelAndView Enter(Map<String, Object> map,String user_id) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "entermanager");
        return mv;
    }

    @GetMapping(value = "get_eateries")
    @ResponseBody
    public Object GetEnterInfo() {
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        Map<String, Object> map = new HashMap<>();
        if (enterpriseList.size() > 0) {
            map.put("data", enterpriseList);
        } else {
            map.put("data", "");
        }
        return map;
    }

    @GetMapping(value = "enterinfomain")
    public Object EnterInfoMain(String user_id, Map<String, Object> map) throws ParseException {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "enterinfomain");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if (ObjectUtils.isNotNull(enterprise)) {
            map.put("loginname", enterprise.getEnterLoginName());
            map.put("entername", enterprise.getEnterName());
            map.put("enteradress", enterprise.getEnterAddress());
            map.put("enterleader", enterprise.getEnterLegalperson());
            map.put("entertel", enterprise.getEnterTelphone());
            map.put("enterdate", DateUtils.dateFormat(enterprise.getEstablelishDate(), DateUtils.DATE_PATTERN));
            map.put("brokerage", enterprise.getBrokerage());

        }
        return mv;
    }

    @GetMapping(value = "getcommonyinfo")
    @ResponseBody
    @CrossOrigin
    public JSONData GetCommonyinfo(Integer page, Integer limit) {
        JSONData response = new JSONData();
        List<Enterprise> enterpriseList = enterpriseRepository.findPagesEnter((page - 1) * limit, limit);
        if (enterpriseList.size() > 0) {
            response.setCode(0);
            response.setData(enterpriseList);
            response.setCount((int) enterpriseRepository.count());
            response.setMsg("");
        } else {
            response.setCode(0);
            response.setData(new ArrayList<>());
            response.setCount(0);
            response.setMsg("");
        }
        return response;
    }

    @PostMapping(value = "companyinfo")
    @ResponseBody
    public Enterprise ShowCompany(HttpServletRequest request) {
        String login_id = request.getParameter("login_id");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_id);
        if (enterprise != null) {
            return enterprise;
        }
        return null;
    }

    /**
     * 添加公司
     *
     * @return
     */
    @GetMapping(value = "addenter")
    public ModelAndView AddEnter() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "addEnter");
        return mv;
    }

    /**
     * 修改公司
     *
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "modifyenter")
    public ModelAndView ModifyEnter(Integer id, Map<String, Object> map) throws ParseException {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "modifyEnter");
        Enterprise enterprise = enterpriseRepository.findEnterpriseById(id);
        map.put("enterName", enterprise.getEnterName());
        map.put("enterAddress", enterprise.getEnterAddress());
        map.put("enterLegal", enterprise.getEnterLegalperson());
        map.put("enterTelphone", enterprise.getEnterTelphone());
        String enterEstableish = DateUtils.dateFormat(enterprise.getEstablelishDate(), DateUtils.DATE_PATTERN);
        map.put("enterEstablelish", enterEstableish);
        map.put("enterLogo", Constant.IMAGE_URL + enterprise.getEnterLogo());
        map.put("enterSort", enterprise.getEnterOrder());
        map.put("enterStatus", enterprise.getEnterStatus());
        map.put("enterPerson", enterprise.getPersonOnline());
        map.put("enterComment", enterprise.getEntercomment());
        return mv;
    }

//    @PostMapping(value = "agentcompanyinfo")
//    @ResponseBody
//    public Enterprise ShowAgentCompanyinfo(HttpServletRequest request) {
//        String login_id = request.getParameter("user_id");
//        CompanyBindUser companyBindUser = companyUserRepository.findCompanyBindUserByUserTel(login_id);
//        Enterprise enterprise = enterpriseRepository.findEnterpriseById(companyBindUser.getCompanyId());
//        if (enterprise != null) {
//            return enterprise;
//        }
//        return null;
//    }

    @GetMapping(value = "company")
    public ModelAndView showCompany() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "company");
        return mv;
    }

    @PostMapping(value = "get_current_com_info")
    @ResponseBody
    public Enterprise GetCurentComInfo(HttpServletRequest request) {
        String login_id = request.getParameter("user_id");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_id);
        return enterprise;
    }

    @PostMapping(value = "addeter")
    @ResponseBody
    public BaseJsonResponse AddAddeter(HttpServletRequest request) throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {
        BaseJsonResponse response = new BaseJsonResponse();
        String enterName = request.getParameter("enter_name");
        String enterAddress = request.getParameter("enter_address");
        String enterLegal = request.getParameter("enter_legal");
        String enterLogo = request.getParameter("logo");
        String enterOrder = request.getParameter("enter_order");
        String enterStatus = request.getParameter("enter_status");
        String enterTelphone = request.getParameter("enter_telphone");
        String establelishDate = request.getParameter("establish_date");
        String personOnline = request.getParameter("person_num");
        String comment = request.getParameter("comment_id");
        String loginname = request.getParameter("enter_loginame");
        if (ObjectUtils.isEmpty(enterName)) {
            response.setCode("3").setMsg("企业名不能为空");
        } else if (ObjectUtils.isEmpty(enterAddress)) {
            response.setCode("3").setMsg("企业地址不能为空");
        } else if (ObjectUtils.isEmpty(enterLegal)) {
            response.setCode("3").setMsg("企业法人不能为空");
        } else if (ObjectUtils.isEmpty(enterLogo)) {
            response.setCode("3").setMsg("企业Logo不能为空");
        } else if (ObjectUtils.isEmpty(enterOrder)) {
            response.setCode("3").setMsg("企业排序不能为空");
        } else if (ObjectUtils.isEmpty(enterStatus)) {
            response.setCode("3").setMsg("企业状态不能为空");
        } else if (ObjectUtils.isEmpty(enterTelphone)) {
            response.setCode("3").setMsg("企业电话不能为空");
        } else if (ObjectUtils.isEmpty(establelishDate)) {
            response.setCode("3").setMsg("企业成立日期不能为空");
        } else if (ObjectUtils.isEmpty(personOnline)) {
            response.setCode("3").setMsg("企业人数上线不能为空");
        } else if (ObjectUtils.isEmpty(comment)) {
            response.setCode("3").setMsg("企业id不能为空");
        } else if (ObjectUtils.isEmpty(loginname)) {
            response.setCode("3").setMsg("登录名不能为空");
        } else {
            Enterprise enterprise2 = enterpriseRepository.findEnterpriseByEnterName(enterName);
            if (!ObjectUtils.isEmpty(enterprise2)) {
                response.setCode("3").setMsg("企业已经存在");
            } else {
                Enterprise enterprise = new Enterprise();
                enterprise.setEnterName(enterName);
                enterprise.setEnterAddress(enterAddress);
                enterprise.setEnterLegalperson(enterLegal);
                enterprise.setEnterLogo(enterLogo);
                enterprise.setEnterOrder(Integer.parseInt(enterOrder));
                enterprise.setEnterStatus(Integer.parseInt(enterStatus));
                enterprise.setEnterTelphone(enterTelphone);
                enterprise.setEntercomment(comment);
                //随机字符串
//              String loginname = RandomStringUtils.random(8, "1234567890");
                //排重
//              loginname = Distinct(loginname);
                UserRights rights = new UserRights();
                rights.setLoginName(loginname);
                rights.setUserRight(UserType.COMPANY.ordinal());
                rights.setRightName("集");
                userRightsRepository.save(rights);
                enterprise.setEnterLoginName(loginname);
                enterprise.setBrokerage(0);
                enterprise.setEnterLoginPwd(MD5Util.getEncryptedPwd("123456"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                enterprise.setEstablelishDate(format.parse(establelishDate));
                enterprise.setPersonOnline(Integer.parseInt(personOnline));
                enterpriseRepository.save(enterprise);
                Enterprise enterprise1 = enterpriseRepository.findEnterpriseByEnterLoginName(loginname);
                if (ObjectUtils.isNotNull(enterprise1)) {
                    Commission commission = new Commission();
                    commission.setUpdateTime(new Date());
                    commission.setScore(new BigDecimal(0));
                    commission.setCompanyId(enterprise1.getId());
                    commission.setInvitScore(new BigDecimal(0));
                    commission.setFirstPercentage(0.0f);
                    commission.setSecondPercentage(0.0f);
                    commission.setMinWithdraw(new BigDecimal(100));
                    commission.setBWithdrawOpen(1);
                    commission.setShareCount(5);
                    commission.setOfirstPercentage(0.0f);
                    commission.setOsecondPercentage(0.0f);
                    commissionRepository.save(commission);
                    for (int i = 0; i < 3; i++) {
                        Banner banner = new Banner();
                        if (i == 0) {
                            banner.setImgurl("http://image.yzcang.com/hd/banner.png");

                        } else {
                            banner.setImgurl("http://image.yzcang.com/hd/banner" + (i + 1) + ".png");
                        }
                        banner.setTrankurl("");
                        banner.setDescript("");
                        banner.setCreateDate(new Date());
                        banner.setStatus(1);
                        banner.setName("默认轮播图");
                        banner.setSort(i + 1);
                        banner.setCompanyId(enterprise1.getId());
                        bannerRepository.save(banner);
                    }

                    JSONArray jsonArray = new JSONArray();
                    Integer[] dials = {10, 10, 10, 10, 10, 10, 10, 10};
                    Integer[] probability = {10, 20, 30, 5, 5, 10, 10, 10};
                    for (int i = 0; i < dials.length; i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", i);
                        jsonObject.put("integral", dials[i]);
                        jsonObject.put("probability", probability[i]);
                        jsonArray.add(jsonObject);
                    }
                    Agent agent = new Agent();
                    agent.setAgentName(enterName);
                    String inviteCode = SerialGeneratorUtil.GetRandomString(8);
                    agent.setInitCode(inviteCode);
                    agent.setType(AgentType.CUSERVICE.ordinal());
                    agent.setTelphone(enterTelphone);
                    agent.setLoginUsername(loginname);
                    agent.setPassword("123456");
                    agent.setBCertification(0);
                    agent.setSuperiorName("");
                    agent.setSuperId(0);
                    agent.setTopName("");
                    agent.setTopId(0);
                    agent.setInitAgentNam(0);
                    agent.setReconCustomNam(0);
                    agent.setPoints(0);
                    agent.setCardPhoto("");
                    agent.setDealCustomNum(0);
                    agent.setForbid(0);
                    agent.setDrawlPad(MD5Util.getEncryptedPwd("123456"));
                    agent.setIdCard("");
                    agent.setImposer(false);
                    agent.setInitAgentNam(0);
                    agent.setRealName("");
                    agent.setDeviceId("");
                    agent.setAccountBalance(new BigDecimal(0));
                    agent.setCompanyId(enterprise1.getId());
                    agent.setAvatar(enterLogo);
                    agentRepository.save(agent);
                    // System.out.println(jsonArray);
                    IntegralRule integralRule = new IntegralRule();
                    integralRule.setCompanyId(enterprise1.getId());
                    integralRule.setMinPrice(10);
                    integralRule.setRmbForPoint(10);
                    integralRule.setIsPoint(2);
                    integralRule.setIsOpenLotto(2);
                    integralRule.setConsume(20);
                    integralRule.setLoginIntegral(1);
                    integralRule.setSignIntegral(1);
                    integralRule.setRecomIntegral(1);
                    integralRule.setAuthIntegral(1);
                    integralRule.setInviteIntegral(1);
                    integralRule.setDials(jsonArray.toString());
                    integralRuleRepository.save(integralRule);
                    response.setCode("1").setMsg("添加企业成功");
                }
            }
        }
        return response;
    }

    /**
     * 排重
     *
     * @param loginname
     * @return
     */
    String Distinct(String loginname) {
        String tempname = loginname;
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(tempname);
        if (enterprise != null) {
            tempname = RandomStringUtils.random(8, "1234567890");
            Distinct(tempname);
        }
        return tempname;
    }

    @PostMapping(value = "modifyenterinfo")
    @ResponseBody
    public Object modifyEnterInfo(Integer brokerage, String enteradress, String enterleader, String enterdate,
                                  String entername, String entertel, String loginname, String loginpwd, String loginrpwd) {
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(brokerage) && ObjectUtils.isNotEmpty(enteradress) && ObjectUtils.isNotEmpty(enterleader) &&
                ObjectUtils.isNotEmpty(enterdate) && ObjectUtils.isNotEmpty(entername) && ObjectUtils.isNotEmpty(entertel) && ObjectUtils.isNotEmpty(loginname)) {
            try {
                if (ObjectUtils.isNotEmpty(loginpwd)) {
                    if (ObjectUtils.isNotEmpty(loginrpwd) && !loginpwd.equals(loginrpwd)) {
                        response.setMsg("输入密码不一致").setCode("3");
                    } else {
                        String md5pwd = MD5Util.getEncryptedPwd(loginpwd);
                        enterpriseRepository.updateEnterPwd(md5pwd, loginname);
                    }
                }
                enterpriseRepository.updateEnterInfo(brokerage, enteradress, enterleader, entername, entertel, DateUtils.dateParse(enterdate, DateUtils.DATE_PATTERN), loginname);
                response.setMsg("修改企业信息成功").setCode("1");

            } catch (Exception e) {
                response.setMsg("修改企业信息异常").setCode("400");
            }

        } else {
            response.setCode("2").setMsg("修改的公司信息不完善");
        }
        return response;
    }

    @PostMapping(value = "update_enter_status")
    @ResponseBody
    public BaseJsonResponse updateEnterStatus(String enter_login_name,Integer status){
        BaseJsonResponse response = new BaseJsonResponse();
        try {
            enterpriseRepository.updateEnterStatus(enter_login_name,status);
            response.setCode("0").setMsg("修改状态成功");
        }catch (Exception e){
            response.setCode("1").setMsg("修改状态失败");
        }

        return  response;
    }
}
