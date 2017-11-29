package com.buswe.core.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public   interface BaseRepository<T, ID extends Serializable>
  extends   JpaRepository<T, ID>, JpaSpecificationExecutor<T>
{
  public abstract EntityManager getEntityManager();
  
  public abstract Class<T> getDomainClass();
  
  public abstract List<T> findList(Collection<String> paramCollection);
  

}
