package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: 左飞
 * @Date: 2019/1/3 9:08
 * @Version 1.0
 */
@RestController
public class AppDownLoadController {

    @GetMapping(value = "appdownload")
    public ModelAndView AppDownLoad() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "app_download");
        return mv;
    }
}
