package com.buswe.module.cms.controller;

import com.buswe.core.web.FilterColum;
import com.buswe.core.web.Filterable;
import com.buswe.module.cms.entity.Article;
import com.buswe.module.cms.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;


@Controller
@RequestMapping("admin/cms/article")
public class ArticleConttoler {
	
	@Resource
	ArticleService service;
		  @RequestMapping("")
		  public String list(Pageable page,@FilterColum(value="title",type="String") Filterable filter, Model model)
		  {
			  model.addAttribute("page", service.findArticlePage(page, filter));
	 	    return "articlelist";
		  }
		  
		  @RequestMapping({"/save"})
		  public String save(@Valid Article entity, BindingResult bindingResult)
		  {
		    if (!bindingResult.hasErrors()) {
		      try
		      {
		    	  service.saveArticle(entity)
		    	  ;
		      }
		      catch (Exception localException) {}
		    }
		    return "redirect:";
		  }
		  
		  @RequestMapping({"/input"})
		  public String input(String id, Model model)
		  {
			  Article entity = null;
		    if (StringUtils.isNotBlank(id)) {
		      entity = service.getArticle(id);
		    } else {
		      entity = new Article();
		    }
		    model.addAttribute("entity", entity);
		    return "article_input";
		  }
		  
		  @RequestMapping({"/delete"})
		  public String delete(String [] id)
		  {
		    for (String ids : id) {
		      this.service.deletArticle(ids);
		    }
		    return "redirect:";
		  }

}
