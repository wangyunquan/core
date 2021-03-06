package com.buswe.module.core.controller;

import com.buswe.module.core.entity.Function;
import com.buswe.module.core.pojo.Result;
import com.buswe.module.core.pojo.ResultCode;
import com.buswe.module.core.pojo.TreeNode;
import com.buswe.module.core.service.FunctionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "admin/function")
public class FunctionController {
    @Resource
  private   FunctionService functionService;


    @RequestMapping(method = RequestMethod.GET, value = "/tree")
    private String list() {

        return "admin/core/function_tree";
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public Iterable<Function> getAll() {

        return functionService.findAll();
    }


    @RequestMapping("/checkunique")
    @ResponseBody
    private Map<String, Boolean> unique( String levelCode)
    {
        Map<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("valid",  functionService.levelCodeUnique(levelCode));

        return  map;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Function function) {
        functionService.save(function);
        return new Result(true,"保存成功");
    }


    /**
     * getTreeData 构造bootstrap-treeview格式数据
     *
     * @return
     */
    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData() {

        return functionService.getTreeData();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Function get(@PathVariable("id") String id) {

        Function function = functionService.get(id);
        if (!StringUtils.isEmpty(function.getParentId())) {
            function.setParentName(functionService.get( function.getParentId()).getName());
        } else {
            function.setParentName("系统菜单");
        }
        return function;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        try {
            functionService.delete(id);
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            return new Result(false, "该菜单/功能已经被其他数据引用，不可删除");
        }
    }


}
