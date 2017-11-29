package com.buswe.module.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.buswe.core.domain.IdEntity;


@Entity
@Table(name="base_authority")
public class Authority
  extends IdEntity
{
	@Column(name = "name", length = 64, unique = true)
	private String name;

	@Column(name = "enable")
	private Boolean enable = Boolean.TRUE;

	@ManyToMany(mappedBy = "authorities")
	private Set<Roleinfo> roleinfos = new HashSet<Roleinfo>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}



	public Set<Roleinfo> getRoleinfos() {
		return roleinfos;
	}

	public void setRoleinfos(Set<Roleinfo> roleinfos) {
		this.roleinfos = roleinfos;
	}
  
 
}
