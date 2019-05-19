package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.URL;
import com.huiketong.cofpasgers.constant.UserType;
import com.huiketong.cofpasgers.entity.Banner;
import com.huiketong.cofpasgers.entity.BannerContext;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.UserRights;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.BannerContextRepository;
import com.huiketong.cofpasgers.repository.BannerRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.UserRightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BannerBackController {
    @Autowired
    BannerRepository bannerRepository;
    @Autowired
    BannerContextRepository contextRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    UserRightsRepository rightsRepository;

    @GetMapping(value = "banner")
    public ModelAndView banner() {
        ModelAndView mv = new ModelAndView("banner");
        return mv;
    }

    @GetMapping(value = "get_banner_info")
    @ResponseBody
    public Object GetBannerInfo() {
        Map<String, Object> map = new HashMap<>();
        List<Banner> bannerList = bannerRepository.findAll();
        map.put("data", bannerList);
        return map;
    }

    @PostMapping(value = "add_banner")
    @ResponseBody
    public BaseJsonResponse AddBanner(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String banner_name = request.getParameter("banner_name");
        String banner_desc = request.getParameter("banner_desc");
        String company_login_id = request.getParameter("company_login_id");

        UserRights rights = rightsRepository.findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(company_login_id, 3, company_login_id, 3);
        if (rights != null && rights.getUserRight().equals(UserType.COMPANY.ordinal())) {
            Banner banner = new Banner();
            if (banner_name != null && !banner_name.isEmpty()) {
                banner.setName(banner_name);
            } else {
                response.setCode("-1");
                response.setMsg("Banner名称不能为空");
            }
            if (banner_desc != null && !banner_desc.isEmpty()) {
                banner.setDescript(banner_desc);
            } else {
                response.setCode("-1");
                response.setMsg("Banner描述不能为空");
            }
            if (company_login_id != null && !company_login_id.isEmpty()) {
                Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(company_login_id);
                if (enterprise != null) {
                    banner.setCompanyId(enterprise.getId());
                    banner.setStatus(1);
                    banner.setCreateDate(new Date());
                    bannerRepository.save(banner);
                    response.setCode("1");
                    response.setMsg("添加Banner图成功");
                }
            }
        } else {
            response.setCode("500");
            response.setMsg("您无权添加Banner");
        }

        return response;
    }

    @PostMapping(value = "add_bannercontext")
    @ResponseBody
    public BaseJsonResponse AddBannerContext(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String filename = request.getParameter("filename");
        String pic_url = request.getParameter("pic_url");
        String pic_desc = request.getParameter("pic_desc");
        BannerContext context = new BannerContext();
        List<Banner> banners = bannerRepository.findAll();
        List<BannerContext> bannerContexts = contextRepository.findAll();
        if (banners.size() == 1) {
            context.setBannerId(banners.get(0).getBannerId());
        } else {
            for (int i = 0; i < banners.size(); i++) {
                Integer banner_id = banners.get(i).getBannerId();
                for (int j = 0; j < bannerContexts.size(); j++) {
                    if (banner_id != bannerContexts.get(j).getBannerId()) {
                        context.setBannerId(banner_id);
                    }
                }
            }
        }
        if (pic_desc != null && !pic_desc.isEmpty()) {
            context.setPic_descript(pic_desc);
        } else {
            response.setMsg("Banner图片描述为空");
            response.setCode("-1");
        }
        if (filename != null && !filename.isEmpty()) {
            context.setPic_url(filename);
        } else {
            response.setCode("-1");
            response.setMsg("Banner图片名字为空");
        }
        if (pic_url != null && !pic_url.isEmpty()) {
            context.setPic_link_url(pic_url);
        } else {
            response.setCode("-1");
            response.setMsg("Banner图片链接为空");
        }
        contextRepository.save(context);
        response.setMsg("添加banner成功");
        response.setCode("1");
        return response;
    }
    @DeleteMapping(value = "removebanner")
    @ResponseBody
    public BaseJsonResponse RemoveBanner(HttpServletRequest request) {
        String banner_name = request.getParameter("banner_name");
        BaseJsonResponse response = new BaseJsonResponse();
        Banner banner = bannerRepository.findBannerByName(banner_name);
        if (banner != null) {
            bannerRepository.deleteById(banner.getBannerId());

            bannerRepository.deleteAll();
            response.setMsg("删除成功");
            response.setCode("1");
        } else {
            response.setCode("-1");
            response.setMsg("删除失败");
        }
        return response;
    }
    @DeleteMapping(value = "removebannerbyid")
    @ResponseBody
    public BaseJsonResponse RemoveBannerById(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String banner_id = request.getParameter("banner_id");
        if (banner_id != null) {
            bannerRepository.deleteById(Integer.parseInt(banner_id));
            contextRepository.deleteByBannerId(Integer.parseInt(banner_id));
            response.setMsg("删除成功");
            response.setCode("1");
        } else {
            response.setCode("-1");
            response.setMsg("删除失败");
        }
        return response;
    }
    @GetMapping(value = "/banner_url_context_edit")
    public String bannerUrlContextEdit(){
        return URL.BANNERCONTEXT;
    }
}
