package com.buswe.module.core.dao;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.module.core.entity.Userinfo;

public abstract interface UserinfoDao extends BaseRepository<Userinfo, String> {
	
	public Userinfo findByUsername(String username);
		
}
