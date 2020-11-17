package com.yang.crowd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalController {
    @RequestMapping("/")
    public String showPortalPage(){

        //实际开发需要加载主页所需数据

        return "portal";
    }
}
