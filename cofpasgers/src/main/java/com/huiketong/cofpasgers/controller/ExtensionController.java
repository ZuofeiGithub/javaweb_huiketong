package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * 推广赚佣金
 */
@Controller
public class ExtensionController {

    @GetMapping(value = "exten")
    public ModelAndView Extension(){
        ModelAndView mv = new ModelAndView(Constant.PREFIX+"extension");
        return mv;
    }

    @GetMapping(value = "addexten")
    public ModelAndView AddExten(){
        ModelAndView mv = new ModelAndView(Constant.PREFIX+"addExtension");
        return mv;
    }
}
