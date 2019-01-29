package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.json.JSONResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExceptionController {

    @GetMapping(value = "hello")
    public void hello(){
        int a = 1/0;
    }

    @GetMapping(value = "/aerator")
    public String error(){
        return "aerator";
    }

    @PostMapping(value = "/getAjaxerror")
    @ResponseBody
    public JSONResult getAjaxerror(){
        int a = 1/0;
        return JSONResult.ok();
    }
}
