package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.json.data.VoucherDetailData;
import com.huiketong.cofpasgers.json.layuidata.VoucherShareResp;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class VoucherController {

    @Autowired
    VoucherShareRepository voucherShareRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    CommodityRepository commodityRepository;
    @Autowired
    VoucherDetailRepository voucherDetailRepository;
    @Autowired
    DefaultEnterRepository defaultEnterRepository;
    @Autowired
    VoucherUserRepository voucherUserRepository;

    @GetMapping("diyongquan")
    public String voucherDetail() {
        return "view/vouchers/voucher.html";
    }

    @GetMapping("voucher_share")
    public String voucher() {
        return "view/vouchers/vouchers_share_list.html";
    }

    @GetMapping("voucherform")
    public String voucherForm(String user_id, Model model) {
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if (!ObjectUtils.isEmpty(enterprise)) {
            List<Commodity> commodityList = commodityRepository.findAllByCompanyId(enterprise.getId());
            if (commodityList.size() > 0) {
                model.addAttribute("goodslist", commodityList);
            }
        }
        return "view/vouchers/vouchers_share_form.html";
    }

    @PostMapping("addvoucher")
    @ResponseBody
    public BaseJsonResponse addVoucher(String title, String context, String imagefile, String link_url, Integer sharetype, Integer goodsid, String user_id, String price) {
        BaseJsonResponse response = new BaseJsonResponse();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if (!ObjectUtils.isEmpty(enterprise)) {
            VoucherShare voucherShare = new VoucherShare();
            voucherShare.setCompanyId(enterprise.getId());
            voucherShare.setTitle(title);
            voucherShare.setContext(context);
            voucherShare.setImage(imagefile);
            voucherShare.setLinkUrl(link_url);
            voucherShare.setSharetype(sharetype);
            voucherShare.setCreateTime(new Date());
            if (!ObjectUtils.isEmpty(price)) {
                VoucherDetail voucherDetail = new VoucherDetail();
                voucherDetail.setCompanyId(enterprise.getId());
                voucherDetail.setContext(context);
                voucherDetail.setTitle(title);
                voucherDetail.setStartTime(new Date());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 30);
                Date date = cal.getTime();
                voucherDetail.setEndTime(date);
                voucherDetail.setPrice(price);
                voucherDetailRepository.save(voucherDetail);
            }
            if (ObjectUtils.isEmpty(goodsid)) {
                voucherShare.setGoodsId(0);
            } else {
                voucherShare.setGoodsId(goodsid);
            }
            try {
                voucherShareRepository.save(voucherShare);
                response.setCode("0").setMsg("添加成功").setData(null);
            } catch (Exception e) {
                response.setCode("500").setMsg("添加失败").setData(null);
            }
        } else {
            response.setCode("500").setMsg("添加失败").setData(null);
        }
        return response;
    }

    @GetMapping("voucherlist")
    @ResponseBody
    public VoucherShareResp voucherList(String user_id) {
        VoucherShareResp resp = new VoucherShareResp();
        List<VoucherShareResp.DataBean> dataBeanList = new ArrayList<>();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if (!ObjectUtils.isEmpty(enterprise)) {
            List<VoucherShare> voucherShareList = voucherShareRepository.findVoucherSharesByCompanyId(enterprise.getId());
            if (voucherShareList.size() > 0) {
                for (VoucherShare voucherShare : voucherShareList) {
                    VoucherShareResp.DataBean bean = new VoucherShareResp.DataBean();
                    bean.setTitle(voucherShare.getTitle());
                    bean.setContext(voucherShare.getContext());
                    bean.setSharetype(voucherShare.getSharetype());
                    bean.setGoods_id(voucherShare.getGoodsId());
                    bean.setImage(voucherShare.getImage());
                    bean.setLink_url(voucherShare.getLinkUrl());
                    bean.setId(voucherShare.getId());
                    dataBeanList.add(bean);
                }
                resp.setCode(0);
                resp.setCount(String.valueOf(voucherShareList.size()));
                resp.setMsg("获取数据成功");
                resp.setData(dataBeanList);
            } else {
                resp.setCode(0);
                resp.setCount("0");
                resp.setMsg("无数据");
                resp.setData(new ArrayList<>());
            }
        } else {
            resp.setCode(0);
            resp.setCount("0");
            resp.setMsg("无数据");
            resp.setData(new ArrayList<>());
        }

        return resp;
    }

    @PostMapping("delvoucher")
    @ResponseBody
    public BaseJsonResponse delVoucher(Integer shareid) {
        BaseJsonResponse response = new BaseJsonResponse();
        VoucherShare voucherShare = new VoucherShare();
        voucherShare.setId(shareid);
        try {
            voucherShareRepository.delete(voucherShare);
            response.setCode("0").setMsg("删除成功").setData(null);
        } catch (Exception e) {
            response.setCode("1").setMsg("删除失败").setData(null);
        }

        return response;
    }

    @GetMapping("voucherdetail")
    @ResponseBody
    public BaseJsonResponse voucherDetail(String user_id) {
        BaseJsonResponse response = new BaseJsonResponse();

        DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserId(user_id);
        if (!ObjectUtils.isEmpty(defaultEnter)) {
            VoucherDetail voucherDetail = voucherDetailRepository.findVoucherDetailByCompanyId(defaultEnter.getCompayId());
            if (!ObjectUtils.isEmpty(voucherDetail)) {
                VoucherDetailData data = new VoucherDetailData();
                data.setTitle(voucherDetail.getTitle());
                List<VoucherUser> userList = voucherUserRepository.findAllByCompanyId(defaultEnter.getCompayId());
                if (userList.size() > 0) {
                    data.setUser_count(String.valueOf(userList.size()));
                } else {
                    data.setUser_count("78");
                }
                data.setPrice(voucherDetail.getPrice());
                response.setCode("0").setMsg("").setData(data);
            } else {
                response.setCode("1").setMsg("没有数据").setData(null);
            }
        }
        return response;
    }
}
