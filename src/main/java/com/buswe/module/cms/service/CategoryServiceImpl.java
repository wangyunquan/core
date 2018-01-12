package com.buswe.module.cms.service;


import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.core.service.BaseServiceImpl;
import com.buswe.module.cms.dao.CategoryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.buswe.module.cms.entity.Category;

import javax.annotation.Resource;

@Service
@Transactional("jpaTransaction")
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {
    @Resource
   private CategoryDao categoryDao;
    @Override
    public BaseRepository<Category, String> repository() {
        return categoryDao;
    }
}
