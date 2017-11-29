package com.buswe.core.service;

import com.buswe.core.dao.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public abstract class BaseServiceImpl<T> implements BaseService<T>{

    @Override
    public T save(T entity) {
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


}
