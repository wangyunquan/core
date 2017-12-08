package com.buswe.core.service;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.core.dao.jpa.QueryHelper;
import com.buswe.core.domain.IdEntity;
import com.buswe.core.web.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public abstract class BaseServiceImpl<T extends IdEntity> implements BaseService<T>{

    @Override
    public T save(T entity) {
        entity.setUpdateDateTime(new Date());
        return repository().save(entity);
    }

    @Override
    public T get(String id) {
        return repository().getOne(id);
    }

    @Override
    public void delete(String id) {
        repository().delete(id);
    }

    @Override
    public Iterable<T> findAll() {
        return repository().findAll();
    }



    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return repository().findAll(spec,pageable);
    }


   public abstract BaseRepository<T, String> repository() ;


    public Page<T> findPage(Pageable page, Filterable filterable)
    {
        Specification<T> specification = QueryHelper.bySearchFilter(filterable.getFilters());
        return repository().findAll(specification, page);
    }

}
