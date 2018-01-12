package com.buswe.module.cms.service;

import com.buswe.module.cms.entity.CmsResource;
import org.springframework.data.domain.Page;

public interface ResouceService {
    /**
     * 上传图片到七牛
     * @param filepath

     * @return
     * @throws Exception
     */
    String uploadFile(String filepath, String filename, String filetype)throws Exception;

    /**
     * 得到资源列表
     * @param actionCode 图片/附件
     * @param pageindex
     * @return
     * @throws Exception
     */
    String getResList(int actionCode, int pageindex)throws Exception;

    /**
     * 资源列表管理
     * @param pageindex
     * @param limit

     * @return
     * @throws Exception
     */
    Page<CmsResource> getResList(int pageindex, int limit, String resname)throws Exception;

    /**
     * 删除资源
     * @param keys
     * @throws Exception
     */
    int delRes(String[] keys)throws Exception;
    /**
     * 资源重命名
     * @param id
     * @param name

     * @throws Exception
     */
    void rename(String id, String name)throws Exception;

}
