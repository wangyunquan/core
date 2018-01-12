package com.buswe.core.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdEntity
  implements Persistable<String>
{
  /**
   * 主键ID自动生成策略
   */
  @Id
  @GenericGenerator(name = "id", strategy = "uuid")
  @GeneratedValue(generator = "id")
  @Column(name = "id", length = 36)
  @DocumentId ////表示这个对象的主键
  protected String id;

  public void setId(String id) {
    this.id = id;
  }

  public String getId()
  {
    return this.id;
  }

  
  public boolean isNew()
  {
    return StringUtils.isBlank(id);
  }
}
