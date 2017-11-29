package com.buswe.core.service;

import com.buswe.core.dao.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public   interface BaseService<T>
{
  public abstract T save(T entity);

  public abstract T get(String id);

  public abstract void delete(String id);

  public abstract Iterable<T>  findAll();

   BaseRepository<T, String> repository() ;

  public   Page<T> findAll(Specification<T> spec, Pageable pageable);
}
