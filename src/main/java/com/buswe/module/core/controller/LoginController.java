package com.buswe.module.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author Raysmond<i@raysmond.com>
 */
@Controller
public class LoginController {
    @RequestMapping("signin")
    public String signin(Principal principal, RedirectAttributes ra) {

        return principal == null ? "login" : "redirect:/";
    }
    @RequestMapping("admin/main")
    public  String main(Principal principal,HttpServletRequest request)
    {

        return "main";
    }
    @RequestMapping("admin/homepage")
    public  String homepage(Principal principal, HttpServletRequest request)
    {

        return "homepage";
    }
}