package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.constant.JSONData;
import com.huiketong.cofpasgers.entity.Banner;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.Merchants;
import com.huiketong.cofpasgers.repository.BannerContextRepository;
import com.huiketong.cofpasgers.repository.BannerRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class NewLunBoImgController {

    private final static Logger log = LoggerFactory.getLogger(NewLunBoImgController.class);

    @Autowired
    BannerRepository bannerRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;

    /**
     * 进入轮播图操作页面
     *
     * @return
     */
    @RequestMapping(value = "/lunBoJsp")
    public ModelAndView yongjinJsp() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "newLunBo");
        return mv;
    }


    /**
     * 轮播图列表
     *
     * @param page
     * @param limit
     * @param telphone
     * @param searchMerName 轮播图名称
     * @param status        轮播图状态
     * @return
     */
    @RequestMapping(value = "/lunBoList")
    @ResponseBody
    public JSONData yongjinList(Integer page, Integer limit, String telphone, String searchMerName, String status) {
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId = enterprise.getId();
        JSONData response = new JSONData();
        List<Banner> shareContextList = bannerRepository.findPagesByLimit(comId, status, searchMerName, (page - 1) * limit, limit);

        if (shareContextList.size() > 0) {
            response.setCode(0);
            response.setData(shareContextList);
            response.setCount((int) bannerRepository.count(comId, status, searchMerName));
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
     * 删除轮播图
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteLunBo")
    @ResponseBody
    public String deleteLunBo(HttpServletRequest request, Integer id) {
        boolean flag = false;
        try {
            bannerRepository.updateLunBo(id);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        if (flag == true) {
            return "1";
        } else {
            return "0";
        }

    }


    /**
     * 上传轮播
     */
    @RequestMapping(value = "/uploadLunBo")
    @ResponseBody
    public String uploadLunBo(HttpServletRequest request, String name, String descript, String trankurl, Integer sort, String telphone, Integer type, @RequestParam("imgurl") MultipartFile file) {
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId = enterprise.getId();
        String fileUrl = null;
        try {
            fileUrl = FileUploadUtil.upload(request, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        Banner banner = new Banner();
        banner.setDescript(descript);
        banner.setName(name);
        banner.setStatus(1);
        banner.setImgurl(fileUrl);
        banner.setTrankurl(trankurl);
        banner.setCompanyId(comId);
        banner.setType(type);
        banner.setSort(sort);
        banner.setCreateDate(new Date());
        boolean flag = false;
        try {
            bannerRepository.save(banner);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        if (flag == true) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 修改轮播图
     *
     * @param request
     * @param id
     * @param imgSrc
     * @param name
     * @param descript
     * @param trankurl
     * @param sort
     * @param telphone
     * @param file
     * @return
     */
    @RequestMapping(value = "/updateLunBo")
    @ResponseBody
    public String updateYongjin(HttpServletRequest request, Integer id, String imgSrc, String name, String descript, String trankurl, Integer sort, Integer type, String telphone, @RequestParam("imgurl") MultipartFile file) {

        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(telphone);
        int comId = enterprise.getId();

        if (file.getSize() > 0) {
            try {
                imgSrc = FileUploadUtil.upload(request, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean flag = false;
        try {
            bannerRepository.updateLunBo(name, descript, trankurl, imgSrc, sort, type, id);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        if (flag == true) {
            return "1";
        } else {
            return "0";
        }

    }
}
