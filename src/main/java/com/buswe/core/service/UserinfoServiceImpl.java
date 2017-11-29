package com.buswe.core.service;

import com.buswe.core.dao.jpa.QueryHelper;
import com.buswe.core.web.Filterable;
import com.buswe.module.core.dao.UserinfoDao;
import com.buswe.module.core.entity.Userinfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Transactional("jpaTransaction")
@Service
public class UserinfoServiceImpl implements UserinfoService {
	@Resource
	private UserinfoDao userinfoDao;
	@Override
	public Userinfo getUserinfo(String id) {
		return userinfoDao.getOne(id);
	}

	@Override
	public Userinfo saveUserinfo(Userinfo entity) {
		return userinfoDao.saveAndFlush(entity);
	}

	@Override
	public Page<Userinfo> userPage(Pageable page, Filterable filter) {
		Specification<Userinfo> spec=	QueryHelper.filterable(filter);
		return userinfoDao.findAll(spec, page);
	}

	@Override
	public Boolean deleteUserinfo(String id) {
		userinfoDao.delete(id);
	return true;
	}

 

}
