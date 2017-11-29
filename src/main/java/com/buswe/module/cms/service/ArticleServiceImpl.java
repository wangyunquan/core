package com.buswe.module.cms.service;


import com.buswe.core.dao.jpa.QueryHelper;
import com.buswe.core.web.Filterable;
import com.buswe.module.cms.dao.ArticleDao;
import com.buswe.module.cms.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional("jpaTransaction")
public class ArticleServiceImpl  implements ArticleService{

	@Resource
	ArticleDao articleDao;
	
	@Override
	public Article saveArticle(Article entity) {
		return articleDao.save(entity);
	}

	@Override
	public Article getArticle(String id) {
		return articleDao.getOne(id);
	}

	@Override
	public Page<Article> findArticlePage(Pageable page, Filterable filter) {
		Specification<Article> spec= QueryHelper.filterable(filter);
		return articleDao.findAll(spec, page);
	}

	@Override
	public Boolean deletArticle(String id) {
		articleDao.delete(id);
		return true;
	}

}
