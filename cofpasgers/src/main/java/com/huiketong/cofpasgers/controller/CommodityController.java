package com.huiketong.cofpasgers.controller;

import com.alibaba.fastjson.JSONObject;
import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.util.FileUploadUtil;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品特价购
 */
@Controller
@RequestMapping(value = "/")
public class CommodityController {

    private  final  static Logger log = LoggerFactory.getLogger(CommodityController.class);

    @Autowired
    EnterpriseRepository enterpriseRepository;

     @Autowired
     CommodityCategoryRepository commodityCategoryRepository;

    @Autowired
    CommodityStyleRepository commodityStyleRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    CommodityImgRepository commodityImgRepository;
     @Autowired

    CommodityOrderRepository  commodityOrderRepository;

     @Autowired
     VoucherShareRepository voucherShareRepository;


    /**
     * 进入商品品类管理页面
     * @return
     */
    @RequestMapping(value = "/commodityCategoryJsp")
    public ModelAndView commodityCategoryJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newCommodityCategory");
        return mv;
    }


    /**
     * 进入商品风格管理页面
     * @return
     */
    @RequestMapping(value = "/commodityStyleJsp")
    public ModelAndView commodityStyleJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newCommodityStyle");
        return mv;
    }


    /**
     * 进入商品管理页面
     * @return
     */
    @RequestMapping(value = "/commodityJsp")
    public ModelAndView commodityJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newCommodity");
        return mv;
    }



    /**
     * 进入订单管理页面
     * @return
     */
    @RequestMapping(value = "/orderManagementJsp")
    public ModelAndView orderManagementJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newOrderManagement");
        return mv;
    }


    /**
     * 获取商品品类列表
     * @param page
     * @param limit
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/commodityCategoryList")
    @ResponseBody
    public JSONData commodityCategoryList(Integer page, Integer limit , String  telphone) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        JSONData response = new JSONData();
        List<CommodityCategory> shareContextList= commodityCategoryRepository.findPagesByLimit(comId,(page-1)*limit,limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) commodityCategoryRepository.count(comId));
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
     * 修改商品品类接口
     * @param id
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "/updateCommodityCategoryList")
    @ResponseBody
    public String updateCommodityCategoryList(Integer id, String categoryName) {

        boolean flag=false;
        try {
            commodityCategoryRepository.updateCommodityCategoryList(id,  categoryName);
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
     * 添加商品品类接口
     * @param telphone
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "/addCommodityCategoryList")
    @ResponseBody
    public String addCommodityCategoryList(String  telphone, String categoryName) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();

        boolean flag=false;
        try {
            CommodityCategory commodityCategory1=commodityCategoryRepository.findPagesByCategoryName(comId,categoryName);
            if(commodityCategory1==null){
                CommodityCategory commodityCategory=new CommodityCategory();
                commodityCategory.setCategoryName(categoryName);
                commodityCategory.setCompanyId(comId);
                commodityCategoryRepository.save(commodityCategory);
                flag=true;
            }else{
                return "2";
            }
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
     * 删除商品品类接口
     * @param id
     * @return
     */
    @RequestMapping(value = "/delCommodityCategoryList")
    @ResponseBody
    public String delCommodityCategoryList( Integer id) {
        boolean flag=false;
        try {
            commodityCategoryRepository.delCommodityCategoryList(id);
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
     * 获取商品风格列表
     * @param page
     * @param limit
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/commodityStyleList")
    @ResponseBody
    public JSONData commodityStyleList(Integer page, Integer limit , String  telphone) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        JSONData response = new JSONData();
        List<CommodityStyle> shareContextList=commodityStyleRepository.findPagesByLimit(comId,(page-1)*limit,limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) commodityStyleRepository.count(comId));
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
     * 修改商品风格接口
     * @param id
     * @param typeName
     * @return
     */
    @RequestMapping(value = "/updateCommodityStyle")
    @ResponseBody
    public String updateCommodityStyle(Integer id, String typeName) {

        boolean flag=false;
        try {
            commodityStyleRepository.updateCommodityStyle(id, typeName);
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
     * 添加商品风格接口
     * @param telphone
     * @param typeName
     * @return
     */
    @RequestMapping(value = "/addCommodityStyle")
    @ResponseBody
    public String addCommodityStyle(String  telphone, String typeName) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();

        boolean flag=false;
        try {
            CommodityStyle commodityStyle1=commodityStyleRepository.findPagesByCategoryName(comId,typeName);
            if(commodityStyle1==null){
                CommodityStyle commodityStyle=new CommodityStyle();
                commodityStyle.setCompanyId(comId);
                commodityStyle.setTypeName(typeName);
                commodityStyleRepository.save(commodityStyle);
                flag=true;
            }else{
                return "2";
            }
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
     * 删除商品风格接口
     * @param id
     * @return
     */
    @RequestMapping(value = "/delCommodityStyle")
    @ResponseBody
    public String delCommodityStyle( Integer id) {
        boolean flag=false;
        try {
            commodityStyleRepository.delCommodityStyle(id);
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
     * 商品列表
     * @param page
     * @param limit
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/commodityList")
    @ResponseBody
    public JSONData commodityList(Integer page, Integer limit , String  telphone,String name) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        JSONData response = new JSONData();
        List<Commodity> shareContextList=commodityRepository.findPagesByLimit(comId,(page-1)*limit,limit,name);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) commodityRepository.count(comId));
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
     * 上传商品图片
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/addCommodityImg")
    @ResponseBody
    public String uploadYongJinImg(HttpServletRequest request, @RequestParam("file") MultipartFile file ,Integer id) {
        System.out.println(file.getOriginalFilename()+"++++");
        System.out.println(id+"---");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        String fileUrl= null;
        String result=null;
        boolean flag=false;
        try {
            fileUrl = FileUploadUtil.upload(request,file);
            CommodityImg commodityImg=new CommodityImg();
            commodityImg.setCommodityd(id);
            commodityImg.setCommodityImgUrl(fileUrl);
            commodityImgRepository.save(commodityImg);
            voucherShareRepository.updateImageUrl(id,fileUrl);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        if(flag==true){
            map.put("code", 0);//0表示成功，1失败
            map.put("msg", "上传成功");//提示消息
            map1.put("src", fileUrl);//图片url
            map1.put("title", "成功");//图片名称，这个会显示在输入框里
            map.put("data", map1);
            result = new JSONObject(map).toString();
        }else{
            map.put("code", 1);//0表示成功，1失败
            map.put("msg", "上传失败");//提示消息
            map1.put("src", fileUrl);//图片url
            map1.put("title", "失败");//图片名称，这个会显示在输入框里
            map.put("data", map1);
            result = new JSONObject(map).toString();
        }
        return result;
    }

    /**
     * 添加商品
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/addCommodity")
    @ResponseBody
    public String addCommodity(Integer commodityStyleId,Integer commodityCategoryId, String  telphone, String commodityName, String danwei,BigDecimal originalPrice, BigDecimal activityPrice, BigDecimal depositMoney, String activityDescription, String productDetails,String label) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        boolean flag=false;
        try {
            Commodity commodityStyle1=commodityRepository.findPagesByCommodityName(comId,commodityName);
            if(commodityStyle1==null){
                Commodity commodityStyle=new Commodity();
                commodityStyle.setCompanyId(comId);
                commodityStyle.setCommodityName(commodityName);
                commodityStyle.setActivityDescription(activityDescription);
                commodityStyle.setActivityPrice(activityPrice);
                commodityStyle.setDepositMoney(depositMoney);
                commodityStyle.setConcernedPeople(0);
                commodityStyle.setOriginalPrice(originalPrice);
                commodityStyle.setProductDetails(productDetails);
                commodityStyle.setDanwei(danwei);
                commodityStyle.setAddTime(new Date());
                commodityStyle.setCategoryId(commodityCategoryId);
                commodityStyle.setStyleId(commodityStyleId);
                commodityStyle.setLabel(label);
                commodityStyle.setLinkname("推荐");
               commodityRepository.save(commodityStyle);

               VoucherShare voucherShare = new VoucherShare();
               voucherShare.setCreateTime(new Date());
               voucherShare.setGoodsId(commodityStyle.getId());
               voucherShare.setSharetype(2);
               CommodityImg commodityImg = commodityImgRepository.findFirstByCommodityd(commodityStyle.getId());
               if(!ObjectUtils.isEmpty(commodityImg)){
                   voucherShare.setImage(commodityImg.getCommodityImgUrl());
               }else{
                   voucherShare.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556354874615&di=cff504226314007b171234ee2246ea78&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fb7b5b489ab8adb866af91fee3019886c5389ff9d67ab-hH0Mm2_fw658");
               }
               voucherShare.setContext(commodityStyle.getActivityDescription());
               voucherShare.setTitle(commodityStyle.getCommodityName());
               voucherShare.setLinkUrl(Constant.URL+"product_details");
               voucherShare.setCompanyId(comId);
               voucherShareRepository.save(voucherShare);
                flag=true;
            }else{
                return "2";
            }
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
     * 修改商品接口
     * @param request
     * @param id
     * @param commodityName
     * @param originalPrice
     * @param activityPrice
     * @param depositMoney
     * @param activityDescription
     * @param productDetails
     * @return
     */
    @RequestMapping(value = "/updateCommodity")
    @ResponseBody
    public String updateCommodity(HttpServletRequest request, Integer commodityStyleId,Integer commodityCategoryId,Integer id , String commodityName,String danwei, Integer originalPrice, Integer activityPrice, BigDecimal depositMoney, String activityDescription, String productDetails) {
        boolean flag=false;
        try {
           commodityRepository.updateCommodity(id, commodityName, originalPrice, activityPrice, depositMoney, activityDescription, productDetails,danwei,commodityStyleId,commodityCategoryId);
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
     * 获取商品图片列表接口
     * @param page
     * @param limit
     * @param commodityd
     * @return
     */
    @RequestMapping(value = "/commodityImgList")
    @ResponseBody
    public JSONData commodityImgList(Integer page, Integer limit , Integer  commodityd) {
        JSONData response = new JSONData();
        List<CommodityImg> shareContextList=commodityImgRepository.findPagesByLimit(commodityd,(page-1)*limit,limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) commodityImgRepository.count(commodityd));
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
     * 删除图片
     * @param id
     * @return
     */
    @RequestMapping(value = "/delCommodityImg")
    @ResponseBody
    public String delCommodityImg( Integer id) {
        boolean flag=false;
        try {
            commodityImgRepository.delCommodityImg(id);
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
     * 删除商品接口
     * @param id
     * @return
     */
    @RequestMapping(value = "/delCommodity")
    @ResponseBody
    public String delCommodity( Integer id) {
        boolean flag=false;
        try {
            commodityRepository.delCommodity(id);
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
     * 商品订单列表
     * @param page
     * @param limit
     * @param telphone
     * @param customerName
     * @return
     */
    @RequestMapping(value = "/commodityOrderList")
    @ResponseBody
    public JSONData commodityOrderList(Integer page, Integer limit , String  telphone,String customerName) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        JSONData response = new JSONData();
        List<CommodityOrder> shareContextList=commodityOrderRepository.findPagesByLimit(comId,customerName,(page-1)*limit,limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) commodityOrderRepository.count(comId,customerName));
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
     * 修改商品订单状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/updateCommodityOrder")
    @ResponseBody
    public String updateCommodityOrder(Integer id, Integer status) {

        boolean flag=false;
        try {
           commodityOrderRepository.updateCommodityOrder(id, status);
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
     * 获取公司商品风格列表
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/findAllCommodityStyle")
    @ResponseBody
    public  List<CommodityStyle> findAllCommodityStyle(String  telphone) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        List<CommodityStyle> commodityStyleList=null;
        try {
         commodityStyleList=commodityStyleRepository.findAllCommodityStyle(comId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return commodityStyleList;

    }


    /**
     * 获取公司商品品类列表
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/findAllByCompanyId")
    @ResponseBody
    public   List<CommodityCategory> findAllByCompanyId(String  telphone) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        List<CommodityCategory> commodityStyleList=null;
        try {
            commodityStyleList=commodityCategoryRepository.findAllByCompanyId(comId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return commodityStyleList;

    }

}
