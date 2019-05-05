package com.huiketong.cofpasgers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

    @GetMapping("list")
    public String list() {
        return "view/list/list";
    }

    @GetMapping("listform")
    public String listForm() {
        return "view/list/listform";
    }
}
