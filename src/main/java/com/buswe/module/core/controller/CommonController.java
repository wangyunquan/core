package com.buswe.module.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/common")
public class CommonController {

    @RequestMapping("/icon/select")
    private String index(String iconName, HttpServletRequest request) {
        request.setAttribute("iconName", iconName);
        return "admin/core/icon_selector";
    }




}
