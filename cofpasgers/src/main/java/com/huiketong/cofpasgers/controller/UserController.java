package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: 左飞
 * @Date: 2018/12/30 8:48
 * @Version 1.0
 */
@Controller
public class UserController {

    @GetMapping(value = "user_info")
    public ModelAndView UserInfo() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "user_info");
        return mv;
    }
}
