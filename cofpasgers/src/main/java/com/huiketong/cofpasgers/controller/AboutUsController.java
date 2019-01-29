package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: 左飞
 * @Date: 2019/1/11 15:18
 * @Version 1.0
 */
@RestController
public class AboutUsController {

    @GetMapping(value = "about")
    public ModelAndView AboutUs(){
        return new ModelAndView(Constant.PREFIX+"about");
    }
}
