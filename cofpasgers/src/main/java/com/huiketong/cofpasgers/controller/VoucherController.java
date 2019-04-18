package com.huiketong.cofpasgers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoucherController {

    @GetMapping("voucher_share")
    public String voucher(){
        return "view/vouchers/vouchers_share_list.html";
    }
}
