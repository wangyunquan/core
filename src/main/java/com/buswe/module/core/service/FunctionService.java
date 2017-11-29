package com.buswe.module.core.service;

import com.buswe.core.service.BaseService;
import com.buswe.module.core.entity.Function;
import com.buswe.module.core.pojo.TreeNode;

import java.util.List;

public interface FunctionService extends  BaseService<Function> {

    public List<TreeNode> getTreeData();



}
