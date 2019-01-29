package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.Merchants;
import com.huiketong.cofpasgers.entity.ShareContext;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.MerchantsRepository;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class NewShangJiaController {

    private  final  static Logger log = LoggerFactory.getLogger(NewShangJiaController.class);

     @Autowired
    MerchantsRepository  merchantsRepository;
     @Autowired
    EnterpriseRepository enterpriseRepository;

    /**
     * 进入佣金页面
     * @return
     */
    @RequestMapping(value = "/shnagjiaJsp")
    public ModelAndView yongjinJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newShangjia");
        return mv;
    }


    /**
     * 商家列表
     * @return
     */
    @RequestMapping(value = "/shnagjiaList")
    @ResponseBody
    public JSONData yongjinList(Integer page, Integer limit , String  telphone,String searchMerName) {
        Enterprise enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        JSONData response = new JSONData();
        List<Merchants> shareContextList;
        if(ObjectUtils.isEmpty(searchMerName)){
            shareContextList =merchantsRepository.findPagesByLimit(comId,(page-1)*limit,limit);
            searchMerName="";
        }else{
            shareContextList =merchantsRepository.findPagesByLikeName(comId,searchMerName,(page-1)*limit,limit);
        }

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) merchantsRepository.count(comId,searchMerName));
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
     * 上传商家
     */
    @RequestMapping(value = "/uploadShangJiaInfo")
    @ResponseBody
    public String uploadShangJiaInfo(HttpServletRequest request, String merName, String merAddress, String telphone, String merUrl, String descript,Integer merOrder, @RequestParam("merLogo") MultipartFile file ) {
        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();
        String fileUrl= null;
        try {
            fileUrl = FileUploadUtil.upload(request,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Merchants  merchants=new Merchants();
        merchants.setEnterId(comId);
        merchants.setMerAddress(merAddress);
        merchants.setMerLogo(fileUrl);
        merchants.setMerName(merName);
        merchants.setMerOrder(merOrder);
        merchants.setMerUrl(merUrl);
        merchants.setDescript(descript);
        boolean flag=false;
        try {
            merchantsRepository.save(merchants);
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
     * @param id
     * @param imgSrc
     * @param merName
     * @param merAddress
     * @param telphone
     * @param merUrl
     * @param merOrder
     * @param file
     * @return
     */
    @RequestMapping(value = "/updateShangJia")
    @ResponseBody
    public String updateYongjin(HttpServletRequest request, Integer id, String imgSrc,String merName, String merAddress, String telphone, String merUrl, Integer merOrder,String descript, @RequestParam("merLogo") MultipartFile file) {

        Enterprise  enterprise=enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId=enterprise.getId();

        if(file.getSize()>0){
            try {
                imgSrc= FileUploadUtil.upload(request,file);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
        }

        boolean flag=false;
        try {
            merchantsRepository.updateYongjin(merName,merAddress,merUrl,imgSrc,merOrder,descript,id);
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
     * 删除商家
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteShangJia")
    @ResponseBody
    public String deleteShangJia(HttpServletRequest request, Integer id) {
        boolean flag=false;

        try {
           merchantsRepository.deleteYongjin(id);
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
