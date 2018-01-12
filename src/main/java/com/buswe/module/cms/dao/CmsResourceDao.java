package com.buswe.module.cms.dao;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.module.cms.entity.CmsResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CmsResourceDao  extends BaseRepository<CmsResource, String> {

    public Page<CmsResource> findCmsResourceByResTypeAndResWebsiteid(String resType,String siteid, Pageable pageable);

    public List <String>  findResKeyByResWebsiteidAndResKeyIn(String resWebsiteid,String [] resKey);


    public  void deleteCmsResourceByResWebsiteidAndResKey(String resWebsiteid,String resKey);

}
