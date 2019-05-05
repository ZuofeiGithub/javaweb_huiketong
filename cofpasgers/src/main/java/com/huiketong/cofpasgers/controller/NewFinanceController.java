package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.entity.Earnings;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.ShareContext;
import com.huiketong.cofpasgers.entity.WithdrawalDetails;
import com.huiketong.cofpasgers.repository.EarningsRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.WithdrawalDetailsRepository;
import com.huiketong.cofpasgers.util.FileUploadUtil;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class NewFinanceController {

    private final static Logger log = LoggerFactory.getLogger(NewFinanceController.class);

    @Autowired
    EarningsRepository earningsRepository;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    WithdrawalDetailsRepository withdrawalDetailsRepository;

    /**
     * 进入奖励明细页面
     *
     * @return
     */
    @RequestMapping(value = "/jiangliJsp")
    public ModelAndView yongjinJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newjiangli");
        return mv;
    }

    /**
     * 进入提现明细页面
     *
     * @return
     */
    @RequestMapping(value = "/tixianJsp")
    public ModelAndView tixianJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newTixian");
        return mv;
    }

    /**
     * 进入提现审批页面
     *
     * @return
     */
    @RequestMapping(value = "/tixianspJsp")
    public ModelAndView tixianspJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newTixianSp");
        return mv;
    }

    /**
     * 进入奖励明细列表
     *
     * @param page
     * @param limit
     * @param telphone
     * @param angentname
     * @param cusname
     * @return
     */
    @RequestMapping(value = "/jiangliList")
    @ResponseBody
    public JSONData yongjinList(Integer page, Integer limit, String telphone, String angentname, String cusname) {
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId = enterprise.getId();
        JSONData response = new JSONData();
        List<Earnings> shareContextList = earningsRepository.findPagesByLimit(comId, angentname, cusname, (page - 1) * limit, limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) earningsRepository.count(comId, angentname, cusname));
            response.setMsg("");
        } else {
            response.setCode(0);
            response.setData(new ArrayList<>());
            response.setCount(0);
            response.setMsg("");
        }
        return response;
    }


    /**
     * 提现明细列表
     *
     * @param page
     * @param limit
     * @param telphone
     * @param angentname
     * @param status
     * @return
     */
    @RequestMapping(value = "/tixianList")
    @ResponseBody
    public JSONData tixianList(Integer page, Integer limit, String telphone, String angentname, String status, Integer userid) {
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId = enterprise.getId();
        JSONData response = new JSONData();
        List<WithdrawalDetails> shareContextList = withdrawalDetailsRepository.findPagesByLimit(comId, angentname, userid, status, (page - 1) * limit, limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) withdrawalDetailsRepository.count(comId, angentname, userid, status));
            response.setMsg("");
        } else {
            response.setCode(0);
            response.setData(new ArrayList<>());
            response.setCount(0);
            response.setMsg("");
        }
        return response;
    }


    /**
     * 提现审批
     *
     * @param descript
     * @param status
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateTixian")
    @ResponseBody
    public String updateTixian(String descript, String status, Integer id) {

        boolean flag = false;
        try {
            withdrawalDetailsRepository.updateTixian(id, descript, status, new Date());
            flag = true;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

        if (flag == true) {
            return "1";
        } else {
            return "0";
        }
    }

}
