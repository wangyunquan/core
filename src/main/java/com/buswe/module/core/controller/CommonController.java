package com.buswe.module.core.controller;

import com.buswe.module.cms.editor.ActionEnter;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/admin/common")
public class CommonController {

    @RequestMapping("/icon/select")
    private String index(String iconName, HttpServletRequest request) {
        request.setAttribute("iconName", iconName);
        return "admin/core/icon_selector";
    }
    @RequestMapping("/upload")
public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        try {
            request.setCharacterEncoding( "utf-8" );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

}

}
