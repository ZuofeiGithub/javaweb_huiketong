package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoucherController {

    @GetMapping("voucher")
    public String voucher(){
        return Constant.PREFIX+"voucher.html";
    }
}
