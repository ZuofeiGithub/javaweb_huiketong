package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: 左飞
 * @Date: 2019/1/8 9:07
 * @Version 1.0
 */
@Controller
public class PrivacyController {
    @GetMapping("yinsi.html")
    public String privacyView() {
        return Constant.PREFIX + "yinsi";
    }
}
