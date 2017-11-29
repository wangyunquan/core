package com.buswe.module.core.dao;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.module.core.entity.Authority;


public abstract interface AuthorityDao extends BaseRepository<Authority, String> {

	Authority findByName(String authority);
	
	
	
}
