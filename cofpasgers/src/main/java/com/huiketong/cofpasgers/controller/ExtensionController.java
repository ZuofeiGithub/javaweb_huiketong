package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.URL;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.PromotActivity;
import com.huiketong.cofpasgers.json.layuidata.PromotActivityResp;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.PromotActivityRepository;
import com.huiketong.cofpasgers.util.DateUtils;
import com.huiketong.cofpasgers.util.ObjectUtils;
import com.huiketong.cofpasgers.util.SerialGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 推广活动
 */
@Controller
public class ExtensionController {
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    PromotActivityRepository promotActivityRepository;

    @GetMapping(value = "exten")
    public ModelAndView Extension(Model model, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "extension");
        model.addAttribute("url",Constant.URL);
        return mv;
    }

    @GetMapping(value = "addexten")
    public ModelAndView AddExten() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "addExtension");
        return mv;
    }


    @GetMapping(value = "activitylist")
    @ResponseBody
    public PromotActivityResp activityResp(String login_name, Integer page, Integer limit) {
        PromotActivityResp resp = new PromotActivityResp();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_name);
        resp.setCode(0);
        resp.setMsg("");
        if (ObjectUtils.isNotEmpty(enterprise)) {
            List<PromotActivity> activityList = promotActivityRepository.findAllByCompanyIdLimit(enterprise.getId(), page - 1, limit);
            if (activityList.size() > 0) {
                List<PromotActivityResp.DataBean> dataBeanList = new ArrayList<>();
                for (PromotActivity promotActivity : activityList) {
                    PromotActivityResp.DataBean dataBean = new PromotActivityResp.DataBean();
                    dataBean.setId(promotActivity.getId());
                    dataBean.setActivityTitle(promotActivity.getTitle() == null ? "" : promotActivity.getTitle());
                    dataBean.setActivityType(promotActivity.getType() == null ? 0 : promotActivity.getType());
                    dataBean.setActAlias(promotActivity.getActAlias() == null ? "":promotActivity.getActAlias());
                    if (promotActivity.getEndTime() != null) {
                        dataBean.setEndTime(promotActivity.getEndTime());
                        boolean flag = promotActivity.getEndTime().before(new Date());
                        if (flag) {
                            dataBean.setActivityStatus(0);
                        } else {
                            dataBean.setActivityStatus(1);
                        }
                    }
                    if (promotActivity.getBeginTime() != null) {
                        dataBean.setBeginTime(promotActivity.getBeginTime());
                    }
                    dataBean.setCreateTime(promotActivity.getCreateTime());
                    dataBeanList.add(dataBean);
                }
                resp.setCount(String.valueOf(promotActivityRepository.findAllByCompanyId(enterprise.getId()).size()));
                resp.setData(dataBeanList);
            }
        } else {
            resp.setCount("0");
            resp.setData(new ArrayList<>());
        }
        return resp;
    }


    @GetMapping(value = "m/act/detail")
    public String activityDetail(String actAlias,Model model) throws ParseException {
       PromotActivity promotActivity = promotActivityRepository.findPromotActivityByActAlias(actAlias);
       if(ObjectUtils.isNotEmpty(promotActivity)){
           model.addAttribute("title",promotActivity.getTitle());
           model.addAttribute("createtime",DateUtils.dateFormat(promotActivity.getCreateTime(),DateUtils.DATE_TIME_PATTERN));
           model.addAttribute("context",promotActivity.getDetail());
           model.addAttribute("type",promotActivity.getType());
       }
        return URL.ACTIVITYDETAIL;
    }

    @PostMapping(value = "addActivity")
    @ResponseBody
    public BaseJsonResponse addActivity(String login_name, String activitytitle, Integer activitytype, String begintime, String endtime, String coverImg, String activity_context) throws ParseException {
        BaseJsonResponse response = new BaseJsonResponse();
        PromotActivity promotActivity = new PromotActivity();
        Date beginTime = DateUtils.dateParse(begintime, DateUtils.DATE_TIME_PATTERN);
        Date endTime = DateUtils.dateParse(endtime, DateUtils.DATE_TIME_PATTERN);
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_name);
        if (ObjectUtils.isNotEmpty(enterprise)) {
            promotActivity.setCompanyId(enterprise.getId());
        }
        promotActivity.setTitle(activitytitle);
        promotActivity.setCoverImg(coverImg);
        promotActivity.setType(activitytype);
        promotActivity.setDetail(activity_context);
        promotActivity.setBeginTime(beginTime);
        promotActivity.setEndTime(endTime);
        promotActivity.setCreateTime(new Date());
        promotActivity.setActAlias(SerialGeneratorUtil.GetRandomString(32));
        try {
            promotActivityRepository.save(promotActivity);
            response.setCode("0").setMsg("添加成功").setData(promotActivity);
        } catch (Exception e) {
            response.setMsg("添加失败");
            response.setCode("200");
        }
        return response;
    }

    @PostMapping(value = "/delActivity")
    @ResponseBody
    public BaseJsonResponse delActivity(Integer id){
        BaseJsonResponse response = new BaseJsonResponse();
        try {
            promotActivityRepository.deleteById(id);
            response.setCode("0").setMsg("删除成功");
        }catch (Exception e){
            response.setCode("1").setMsg("删除失败");
        }

        return response;
    }


}
