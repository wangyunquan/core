package com.buswe.module.core.service;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.core.service.BaseServiceImpl;
import com.buswe.core.utils.TreeUtil;
import com.buswe.module.core.dao.FunctionDao;
import com.buswe.module.core.entity.Function;
import com.buswe.module.core.pojo.TreeNode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional("jpaTransaction")
@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function> implements FunctionService {
    @Resource
    FunctionDao functionDao;

    @Override
    public BaseRepository<Function, String> repository() {
        return functionDao;
    }


    @Override
    public List<TreeNode> getTreeData() {
        List<Function> allFunction = functionDao.findAll(new Sort("levelCode") {
        });
        Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
        for (Function func : allFunction) {
            TreeNode node = new TreeNode();
            node.setText(func.getName());
            node.setId(func.getId());
            node.setParentId(func.getParentId());
            node.setLevelCode(func.getLevelCode());
            node.setIcon(func.getIcon());
            nodelist.put(node.getId(), node);
        }
        // 构造树形结构
        return TreeUtil.getNodeList(nodelist);
    }

    @Override
    public Boolean levelCodeUnique(String levelCode) {
        boolean exist = functionDao.existsByLevelCode(levelCode);
         return exist?false:true;
    }
}
