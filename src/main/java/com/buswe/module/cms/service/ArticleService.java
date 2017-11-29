package com.buswe.module.cms.service;

import com.buswe.core.web.Filterable;
import com.buswe.module.cms.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
	
	public Article saveArticle(Article entity);
	
	public Article getArticle(String id);

	public Page<Article> findArticlePage(Pageable page, Filterable filter);

	public Boolean deletArticle(String id);
}
