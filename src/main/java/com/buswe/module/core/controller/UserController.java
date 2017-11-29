package com.buswe.module.core.controller;

import com.buswe.core.security.JPAUserDetails;
import com.buswe.core.service.UserinfoService;
import com.buswe.core.web.FilterColum;
import com.buswe.core.web.Filterable;
import com.buswe.module.core.entity.Userinfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 
 * @author wangyunquan
 *
 */
@Controller
public class UserController {
@Resource
UserinfoService   service;
	  @RequestMapping("admin/core/user")
	  public String list(Pageable page,@FilterColum(value="username",type="String") Filterable filter, Model model)
	  {
		  model.addAttribute("page", service.userPage(page, filter));
 	    return "userlist";
	  }
	  
	  @RequestMapping({"admin/core/user/save"})
	  public String save(@Valid Userinfo entity, BindingResult bindingResult)
	  {
	    if (!bindingResult.hasErrors()) {
	      try
	      {
	    	  JPAUserDetails userdetail=new JPAUserDetails(entity);
	    	  service.saveUserinfo(userdetail.getUserinfo());
	      }
	      catch (Exception localException) {}
	    }
	    return "redirect:";
	  }
	  
	  @RequestMapping({"/admin/core/user/input"})
	  public String input(String id, Model model)
	  {
		  Userinfo entity = null;
	    if (StringUtils.isNotBlank(id)) {
	      entity = service.getUserinfo(id);
	    } else {
	      entity = new Userinfo();
	    }
	    model.addAttribute("entity", entity);
	    return "core/user/userInput";
	  }
	  
	  @RequestMapping({"/admin/core/user/delete"})
	  public String delete(String [] id)
	  {
	    for (String ids : id) {
	      this.service.deleteUserinfo(ids);
	    }
	    return "redirect:";
	  }
}