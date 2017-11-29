package com.buswe.module.core.dao;

import java.util.List;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.module.core.entity.Roleinfo;

public abstract interface RoleinfoDao extends BaseRepository<Roleinfo, String> {

	List<Roleinfo> findByName(String groupName);
	
	
	
}
