package com.buswe.module.cms.controller;


import com.buswe.core.web.Filterable;
import com.buswe.module.cms.entity.Category;
import com.buswe.module.cms.service.CategoryService;
import com.buswe.module.core.pojo.Result;
import com.buswe.module.core.pojo.ResultCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("admin/cms/cat")
public class CategoryController {
@Resource
    CategoryService categoryService;
    /**
     * 列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list() {
        return "base/cms/cat_list";
    }

    @RequestMapping( value = "/loaddata")
    @ResponseBody
    private  Page<Category>  page(Pageable page, Filterable filterable) {
        Page<Category> result= this.categoryService.findPage(page, filterable);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private Category getUser(String id) {

        return categoryService.get(id );
    }
    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @ResponseBody
    private Result deleteUser(@PathVariable("id") String id) {
        try {
            categoryService.delete(id);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(ResultCode.SUCCESS);
    }

}


