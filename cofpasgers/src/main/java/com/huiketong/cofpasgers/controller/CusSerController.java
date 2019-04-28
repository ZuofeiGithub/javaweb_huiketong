package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.json.layuidata.CusSerResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 客服管理
 */
@Controller
public class CusSerController {

    @GetMapping("cus_ser_list")
    public String cusSerList() {
        return "view/cus_ser/cus_ser_list.html";
    }

    @GetMapping("showcusser_list")
    @ResponseBody
    public CusSerResp showCusserList(String user_id) {
        CusSerResp resp = new CusSerResp();
        return resp;
    }
}
