package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.Merchants;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.MerchantsRepository;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MerchantController {
    @Autowired
    MerchantsRepository merchantsRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;

    @GetMapping(value = "merchant")
    public ModelAndView Merchant() {
        ModelAndView mv = new ModelAndView("merchant");
        return mv;
    }

    @PostMapping(value = "getMerInfo")
    @ResponseBody
    public Object GetMerInfo(HttpServletRequest request) {
        String login_id = request.getParameter("login_id");
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isNotEmpty(login_id)) {
            Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_id);
            if (ObjectUtils.isNotNull(enterprise)) {
                List<Merchants> merchantsList = merchantsRepository.findMerchantsByEnterId(enterprise.getId());
                if (merchantsList.size() > 0) {
                    map.put("data", merchantsList);
                } else {
                    map.put("data", "");
                }
            }

        }
        return map;
    }

    @PostMapping(value = "addMerchant")
    @ResponseBody
    public Object AddMerchant(HttpServletRequest request) {
        String merName = request.getParameter("merName");
        String merAddress = request.getParameter("merAddress");
        String merUrl = request.getParameter("merUrl");
        String merOrder = request.getParameter("merOrder");
        String merLogo = request.getParameter("merLogo");
        String enterId = request.getParameter("enterId");
        BaseJsonResponse response = new BaseJsonResponse();
        Merchants merchants = new Merchants();
        if (merName != null) {
            merchants.setMerName(merName);
        }
        if (merAddress != null) {
            merchants.setMerAddress(merAddress);
        }
        if (merUrl != null) {
            merchants.setMerUrl(merUrl);
        }
        if (merOrder != null && !merOrder.isEmpty()) {
            merchants.setMerOrder(Integer.parseInt(merOrder));
        }
        if (merLogo != null) {
            merchants.setMerLogo(merLogo);
        }
        if (enterId != null) {
            Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(enterId);
            if (enterprise != null) {
                // merchants.setCompanyId(enterprise.getId());
                merchants.setEnterId(enterprise.getId());
                merchantsRepository.save(merchants);
                response.setMsg("添加合作商家信息成功");
                response.setCode("1");
            } else {
                response.setCode("200");
                response.setMsg("企业不存在");
            }
        }
        return response;
    }
}
