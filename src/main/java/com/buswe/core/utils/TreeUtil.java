package com.buswe.core.utils;


import com.buswe.module.core.pojo.TreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by billJiang on 2017/2/12.
 * e-mail:jrn1012@petrochina.com.cn qq:475572229
 */
public class TreeUtil {

    public static List<TreeNode> getNodeList(Map<String, TreeNode> nodelist) {
        List<TreeNode> tnlist=new ArrayList<>();
        for (String id : nodelist.keySet()) {
            TreeNode node = nodelist.get(id);
            if (StringUtils.isEmpty(node.getParentId())) {
                tnlist.add(node);
            } else {
                if (nodelist.get(node.getParentId()).getNodes() == null)
                    nodelist.get(node.getParentId()).setNodes(new ArrayList<TreeNode>());
                nodelist.get(node.getParentId()).getNodes().add(node);
            }
        }
        return tnlist;
    }
}
