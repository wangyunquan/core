package com.buswe.module.core.controller;

import com.buswe.module.core.entity.Function;
import com.buswe.module.core.service.FunctionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "admin/function")
public class FunctionController {
    @Resource
  private   FunctionService functionService;
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public Iterable<Function> getAll() {

        return functionService.findAll();
    }

}
