package com.buswe.module.cms.controller;


import com.buswe.core.web.Filterable;
import com.buswe.module.cms.entity.Category;
import com.buswe.module.cms.service.CategoryService;
import com.buswe.module.core.pojo.Result;
import com.buswe.module.core.pojo.ResultCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @RequestMapping(value = "/list")
    private String page(Pageable page, Filterable filterable, Model model) {
        Page<Category> result= this.categoryService.findPage(page, filterable);
        model.addAttribute("page",result);
        return "/cms/cat_list";
    }


    @RequestMapping(value = "/input")

    public String getcat(String id,Model model) {
        model.addAttribute("entity",categoryService.get(id ));
        return "base/cms/cat";
    }


    @RequestMapping( value = "/delete/{id}")
    @ResponseBody
    private Result deleteUser(@PathVariable("id") String id) {
        try {
            categoryService.delete(id);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/save")
    public String addcat(Category category)
    {

        categoryService.save(category);

        return "redirect:list";
    }

}


