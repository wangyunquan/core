package com.buswe.module.core.entity;

import com.buswe.core.domain.IdEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="base_rolinfo")
public class Roleinfo   extends IdEntity
{
	@Column(name = "name", nullable = false, length = 100, unique = true)
	private String name;

	@ManyToMany(targetEntity = Userinfo.class)
	@JoinTable(name = "base_userinfo_roles", joinColumns = { @JoinColumn(name = "roleinfo_id") }, inverseJoinColumns = { @JoinColumn(name = "userinfo_id") })
	private Set<Userinfo> userinfos = new HashSet<>();
	@ManyToMany(targetEntity = Roleinfo.class)
	@JoinTable(name = "base_roleinfo_authority", joinColumns = { @JoinColumn(name = "roleinfo_id") }, inverseJoinColumns = { @JoinColumn(name = "authority_id") })
	private Set<Authority> authorities = new HashSet<>();
	@Column(name = "enable")
	private Boolean enable = Boolean.TRUE;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Userinfo> getUserinfos() {
		return userinfos;
	}
	public void setUserinfos(Set<Userinfo> userinfos) {
		this.userinfos = userinfos;
	}
	public Set<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
 
	public Userinfo addUser(Userinfo user) {
		if (user == null) {
			return null;
		}

		getUserinfos().add(user);
		user.getRoleinfos().add(this);

		return user;
	}

	public Userinfo removeUser(Userinfo user) {
		if (user == null) {
			return null;
		}
		getUserinfos().remove(user);
		user.getRoleinfos().remove(this);
		return user;
	}
	
	public Authority addAuthority(Authority role) {
		if (role == null) {
			return null;
		}

		getAuthorities().add(role);
		role.getRoleinfos().add(this);

		return role;
	}

	public Authority removeAuthority(Authority role) {
		if (role == null) {
			return null;
		}

		getAuthorities().remove(role);
		role.getRoleinfos().remove(this);

		return role;
	}
	
}