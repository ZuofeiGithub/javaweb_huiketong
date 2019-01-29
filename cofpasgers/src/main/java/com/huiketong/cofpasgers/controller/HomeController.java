package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping(value = "home")
    public ModelAndView Agent(){
        ModelAndView mv = new ModelAndView(Constant.PREFIX+"home");
        return mv;
    }
}
