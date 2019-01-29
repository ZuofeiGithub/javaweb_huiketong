package com.huiketong.cofpasgers.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.util.DateUtils;
import com.huiketong.cofpasgers.util.FileUploadUtil;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 佣金
 */
@Controller
@RequestMapping(value = "/")
public class NewYongJinController {

    private  final  static Logger log = LoggerFactory.getLogger(NewYongJinController.class);

    @Autowired
    ShareContextRepository shareContextRepository;
   @Autowired
   EnterpriseRepository enterpriseRepository;
    @Autowired
    CommissionRepository commissionRepository;

    @Autowired
    ShareGuideRepository shareGuideRepository;

    @Autowired
    IntegralRuleRepository  integralRuleRepository;
    /**
     * 进入佣金页面
     * @return
     */
    @RequestMapping(value = "/yongjinJsp")
    public ModelAndView yongjinJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newYongJin");
        return mv;
    }

    /**
     * 进入积分规则页面
     * @return
     */
    @RequestMapping(value = "/integralRuleJsp")
    public ModelAndView integralRule(Map<String,Object> map,String user_id) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newIntegralRule");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if(ObjectUtils.isNotNull(enterprise))
        {
            IntegralRule rule =  integralRuleRepository.findIntegralRuleByCompanyId(enterprise.getId());
            if(ObjectUtils.isNotNull(rule)) {
                map.put("rule", rule);
                map.put("minprice",rule.getMinPrice());
                map.put("rmbforpoint",rule.getRmbForPoint());
                map.put("consume",rule.getConsume());
                map.put("loginIntegral",rule.getLoginIntegral());
                map.put("signIntegral",rule.getSignIntegral());
                map.put("recomIntegral",rule.getRecomIntegral());
                map.put("authIntegral",rule.getAuthIntegral());
                map.put("inviteIntegral",rule.getInviteIntegral());
                map.put("ispoint", rule.getIsPoint());
                map.put("islotto", rule.getIsOpenLotto());
                JSONArray jsonArray = JSON.parseArray(rule.getDials());
                if(ObjectUtils.isNotNull(jsonArray)){
                    for(int i = 0; i < jsonArray.size();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("point"+i,jsonObject.getIntValue("integral"));
                        map.put("probability"+i,jsonObject.getIntValue("probability"));
                    }
                }
            }
        }
        return mv;
    }


    /**
     * 进入分佣指南页面
     * @return
     */
    @RequestMapping(value = "/fenyongJsp")
    public ModelAndView fenyongJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newFenyong");
        return mv;
    }

    /**
     * 进入佣金规则页面
     * @return
     */
    @GetMapping(value = "/yongjinGuiZeJsp")
    public ModelAndView yongjinGuiZeJsp(String user_id,Map<String,Object> map) {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newyongjinGuiZe");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if(ObjectUtils.isNotNull(enterprise)){
            Commission commission = commissionRepository.findCommissionByCompanyId(enterprise.getId());
            if(ObjectUtils.isNotNull(commission)){
                map.put("score",commission.getScore());
                map.put("minDral",commission.getMinWithdraw());
                map.put("sharecount",commission.getShareCount());
                map.put("dralstatus",commission.getBWithdrawOpen());
                map.put("invitescore",commission.getInvitScore());
                map.put("firstpresent",commission.getFirstPercentage());
                map.put("secondpresent",commission.getSecondPercentage());
                map.put("ofirstpreset",commission.getOfirstPercentage());
                map.put("osecondpreset",commission.getOsecondPercentage());

            }else{
                map.put("rule","");
            }
        }else{
            map.put("rule","");
        }
        return mv;
    }


    /**
     * 佣金列表
     * @return
     */
    @RequestMapping(value = "/yongjinList")
    @ResponseBody
    public JSONData yongjinList(Integer page, Integer limit ,String  telphone,String searchTitle) {
        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        JSONData response = new JSONData();
        List<ShareContext> shareContextList;
        if(ObjectUtils.isEmpty(searchTitle)){
            shareContextList= shareContextRepository.findPagesByLimit(comId,(page-1)*limit,limit);
            searchTitle="";
        }else{
            shareContextList= shareContextRepository.findPagesByLikeName(comId,searchTitle,(page-1)*limit,limit);
        }

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) shareContextRepository.count());
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
     * 上传佣金图片
     */
    @RequestMapping(value = "/uploadYongJinImg")
    @ResponseBody
    public String uploadYongJinImg(HttpServletRequest request, @RequestParam("file") MultipartFile file ) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        String fileUrl= null;
        try {
            fileUrl = FileUploadUtil.upload(request,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("code", 0);//0表示成功，1失败
        map.put("msg", "上传成功");//提示消息
        map1.put("src", fileUrl);//图片url
        map1.put("title", "成功");//图片名称，这个会显示在输入框里
        map.put("data", map1);
        String result = new JSONObject(map).toString();
        return result;
    }



    /**
     * 上传佣金详情
     */
    @RequestMapping(value = "/uploadYongJinInfo")
    @ResponseBody
    public String uploadYongJinInfo(HttpServletRequest request, String title, String telphone, BigDecimal money, String url, String yongjinContent, @RequestParam("filename") MultipartFile file ) {
        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        String fileUrl= null;
        try {
            fileUrl = FileUploadUtil.upload(request,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShareContext  shareContext=new ShareContext();
        shareContext.setCompanyId(comId);
        shareContext.setContext(yongjinContent);
        shareContext.setImgUrl(fileUrl);
        shareContext.setMoney(money);
        shareContext.setUrl(url);
        shareContext.setTitle(title);
        try {
            shareContext.setDatetime(DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean flag=false;
        try {
            shareContextRepository.save(shareContext);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        if(flag==true){
            return "1";
        }else{
            return "0";
        }
    }


    /**
     * 删除佣金详情
     */
    @RequestMapping(value = "/deleteYongjin")
    @ResponseBody
    public String deleteYongjin(HttpServletRequest request, Integer id) {
        boolean flag=false;

        try {
            shareContextRepository.deleteYongjin(id);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        if(flag==true){
            return "1";
        }else{
            return "0";
        }

    }

    /**
     * 修改商家
     * @param request
     * @param title
     * @param imgSrc
     * @param id
     * @param telphone
     * @param money
     * @param url
     * @param yongjinContent
     * @param file
     * @return
     */
    @RequestMapping(value = "/updateYongjin")
    @ResponseBody
    public String updateYongjin(HttpServletRequest request, String title, String imgSrc, Integer id,String telphone, BigDecimal money, String url, String yongjinContent, @RequestParam("filename") MultipartFile file) {

        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();

        if(file.getSize()>0){
            try {
                imgSrc= FileUploadUtil.upload(request,file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean flag=false;
        try {
            shareContextRepository.updateYongjin(yongjinContent,title,money,url,imgSrc,id);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        if(flag==true){
            return "1";
        }else{
            return "0";
        }
    }

    /**
     * 添加佣金规则
     * @param score
     * @param minWithdraw
     * @param shareCount
     * @param bWithdrawOpen
     * @param invitScore
     * @param firstPercentage
     * @param secondPercentage
     * @param nbRemark
     * @param qtfirstPercentage
     * @param qtsecondPercentage
     * @param qtRemark
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/addYongjinGuiZe")
    @ResponseBody
    public String addYongjinGuiZe(BigDecimal score, BigDecimal minWithdraw, Integer shareCount, Integer bWithdrawOpen, BigDecimal invitScore, Float firstPercentage, Float secondPercentage, String nbRemark,
                                  Float qtfirstPercentage, Float qtsecondPercentage, String qtRemark,String telphone) {

        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
//        Commission commission=new Commission();
//        commission.setCompanyId(comId);
//        commission.setScore(score);
//        commission.setUpdateTime(new Date());
//        commission.setBWithdrawOpen(bWithdrawOpen);
//        commission.setFirstPercentage(firstPercentage);
//        commission.setSecondPercentage(secondPercentage);
//        commission.setOfirstPercentage(qtfirstPercentage);
//        commission.setOsecondPercentage(qtsecondPercentage);
//        commission.setQtRemark(qtRemark);
//        commission.setNbRemark(nbRemark);
//        commission.setMinWithdraw(minWithdraw);
//        commission.setShareCount(shareCount);
//        commission.setInvitScore(invitScore);
        boolean flag=false;
        try {
            commissionRepository.updateComByComId(score,bWithdrawOpen,firstPercentage,secondPercentage,qtfirstPercentage,qtsecondPercentage,shareCount,
                    invitScore,nbRemark,qtRemark,minWithdraw,comId);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        if(flag==true){
            return "1";
        }else{
            return "0";
        }

    }

    /**
     * 添加分佣指南
     * @param guideContext
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/addYongjinGuide")
    @ResponseBody
    public String addYongjinGuide( String guideContext, String telphone) {

        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        ShareFeeGuide shareFeeGuide=new ShareFeeGuide();
        shareFeeGuide.setCompanyId(comId);
        shareFeeGuide.setGuideContext(guideContext);
        boolean flag=false;
        try {
          shareGuideRepository.save(shareFeeGuide);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        if(flag==true){
            return "1";
        }else{
            return "0";
        }

    }


    /**
     * 积分规则
     * @param minPrice
     * @param rmbForPoint
     * @param isPoint
     * @param isOpenLotto
     * @param consume
     * @param loginIntegral
     * @param signIntegral
     * @param recomIntegral
     * @param authIntegral
     * @param inviteIntegral
     * @param dials
     * @param probability
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/addIntegralRule")
    @ResponseBody
    public String addIntegralRule( Integer minPrice, Integer rmbForPoint, Integer isPoint, Integer isOpenLotto, Integer consume, Integer loginIntegral,
                                   Integer signIntegral, Integer recomIntegral, Integer authIntegral,Integer inviteIntegral ,@RequestParam("dials") Integer dials [] ,@RequestParam("probability") Integer probability [],String telphone) {
         int countSum=0;
         for(int i=0;i<probability.length;i++){
             countSum+=probability[i];
         }
         if(countSum!=100){ //判断概率是否为100%
            return "2";
         }
        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        IntegralRule exisintegralRule = integralRuleRepository.findIntegralRuleByCompanyId(comId);

        boolean flag=false;
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<dials.length;i++){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",i);
            jsonObject.put("integral",dials[i]);
            jsonObject.put("probability",probability[i]);
            jsonArray.add(jsonObject);
        }
       // System.out.println(jsonArray);
        if(ObjectUtils.isNull(exisintegralRule)) {
            IntegralRule integralRule = new IntegralRule();
            integralRule.setCompanyId(comId);
            integralRule.setMinPrice(minPrice);
            integralRule.setRmbForPoint(rmbForPoint);
            integralRule.setIsPoint(isPoint);
            integralRule.setIsOpenLotto(isOpenLotto);
            integralRule.setConsume(consume);
            integralRule.setLoginIntegral(loginIntegral);
            integralRule.setSignIntegral(signIntegral);
            integralRule.setRecomIntegral(recomIntegral);
            integralRule.setAuthIntegral(authIntegral);
            integralRule.setInviteIntegral(inviteIntegral);
            integralRule.setDials(jsonArray.toString());
            try {
                integralRuleRepository.save(integralRule);
                flag=true;
            }catch (Exception e){
                e.printStackTrace();
            }
            if(flag==true){
                return "1";
            }else{
                return "0";
            }

        }else{
            try {

                integralRuleRepository.updateRule(authIntegral,consume,jsonArray.toString(),inviteIntegral,isOpenLotto,isPoint,loginIntegral,minPrice,
                        recomIntegral,rmbForPoint,signIntegral,comId);
                flag=true;
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }

            if(flag==true){
                return "1";
            }else{
                return "0";
            }
        }
    }

}
