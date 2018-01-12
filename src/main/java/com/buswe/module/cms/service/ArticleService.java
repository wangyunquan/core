package com.buswe.module.cms.service;

import com.buswe.core.service.BaseService;
import com.buswe.core.web.Filterable;
import com.buswe.module.cms.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface ArticleService extends BaseService<Article>{
	
	public Article saveArticle(Article entity);
	
	public Article getArticle(String id);

	public Page<Article> findArticlePage(Pageable page, Filterable filter);


	void updateArticlesType(String[] ids, String category);

	void buildIndex();

	void fixedTop(String[] ids);

	void cancleFixed(String[] ids);

	void setCover(String[] ids, String resKey);

	void downloadArticleImg(String id);

	public Page<Article> getArticleList(Pageable pageable, Map<String, Object> args, Sort sort);
	public Page<Article> getArticleByLable(String lableName, String website, int page, int limit);
	public List<Article> getHotArticle(String websiteid, int limit);


}
