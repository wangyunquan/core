package com.buswe.module.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/common")
public class CommonController {

    @RequestMapping("/icon/select")
    private String index(String iconName, HttpServletRequest request) {
        request.setAttribute("iconName", iconName);
        return "admin/core/icon_selector";
    }



}
