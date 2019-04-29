package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.entity.Commodity;
import com.huiketong.cofpasgers.entity.CommodityImg;
import com.huiketong.cofpasgers.json.data.SpecialofferProductDetailData;
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
        if (ObjectUtils.isNotEmpty(goods_id)) {
            Commodity commodity = commodityRepository.findCommodityById(goods_id);
            if (ObjectUtils.isNotNull(commodity)) {
                SpecialofferProductDetailData data = new SpecialofferProductDetailData();
                List<CommodityImg> commodityImgList = commodityImgRepository.findCommodityImgsByCommodityd(goods_id);
                List<String> images = new ArrayList<>();
                if (commodityImgList.size() > 0) {

                    for (CommodityImg commodityImg : commodityImgList) {
                        images.add(commodityImg.getCommodityImgUrl());
                    }
                    data.setImage(images);
                    if (ObjectUtils.isNotEmpty(images.get(0))) {
                        data.setLogo(images.get(0));
                    }
                } else {
                    data.setImage(new ArrayList<>());
                    data.setLogo("");
                }
                data.setTitle(commodity.getCommodityName());
                data.setId(commodity.getId().toString());
                data.setUnit(commodity.getDanwei());
                data.setSale_price(commodity.getActivityPrice().toString());
                data.setRaw_price(commodity.getOriginalPrice().toString());
                data.setConcern(commodity.getConcernedPeople().toString());
                data.setActivity(commodity.getActivityDescription());
                data.setDetail(commodity.getProductDetails());
                data.setDeposit(commodity.getDepositMoney().toString());
                response.setData(data).setMsg("获取商品详情").setCode("1");
            } else {
                response.setMsg("商品不存在").setCode("2");
            }
        } else {
            response.setMsg("商品不存在").setCode("2");
        }
        return response;
    }
}
