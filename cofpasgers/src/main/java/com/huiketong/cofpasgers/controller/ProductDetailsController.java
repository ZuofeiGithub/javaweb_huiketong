package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.entity.Commodity;
import com.huiketong.cofpasgers.entity.CommodityImg;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.CommodityImgRepository;
import com.huiketong.cofpasgers.repository.CommodityRepository;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductDetailsController {
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private CommodityImgRepository commodityImgRepository;

    @GetMapping("/product_details")
    public String productDetails() {
        return "view/prodetails/detail.html";
    }

    @PostMapping("/productdetails")
    @ResponseBody
    public BaseJsonResponse pro(Integer goods_id) {
        BaseJsonResponse response = new BaseJsonResponse();
        if (!ObjectUtils.isEmpty(goods_id)) {
            Commodity commodity = commodityRepository.findCommodityById(goods_id);
            if (!ObjectUtils.isEmpty(commodity)) {
                List<CommodityImg> commodityImgList = commodityImgRepository.findCommodityImgsByCommodityd(commodity.getId());
                List<String> imageUrl = new ArrayList<>();
                if(commodityImgList.size() > 0) {
                    for(CommodityImg commodityImg:commodityImgList) {
                        imageUrl.add(commodityImg.getCommodityImgUrl());
                    }
                    response.setData(imageUrl).setMsg("获取图片列表成功").setCode("0");
                }
            } else {
                response.setCode("1").setMsg("没有该货物").setData(null);
            }
            response.setCode("0").setMsg("获取成功").setData(null);
        } else {
            response.setCode("1").setMsg("没有该货物详情页").setData(null);
        }
        return response;
    }
}
