package com.huiketong.cofpasgers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HPlusController {

    @GetMapping("hplus")
    public String hplus(){
        return "hplus/index";
    }
}
