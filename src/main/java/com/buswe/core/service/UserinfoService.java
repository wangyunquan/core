package com.buswe.core.service;

import com.buswe.core.web.Filterable;
import com.buswe.module.core.entity.Userinfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserinfoService  {
	
	public Userinfo getUserinfo(String id);
	
	public Userinfo saveUserinfo(Userinfo entity);
	
	public Page<Userinfo> userPage(Pageable page, Filterable filter);
	
	public Boolean deleteUserinfo(String id);


}
