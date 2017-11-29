package com.buswe.module.core.entity;

import com.buswe.core.domain.IdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name="base_userinfo")
public class Userinfo extends IdEntity
{
	 @Column
	private   String username;
	 @Column
	private String email;
	 @Column
	private String password;
	 @Column
	private   boolean accountNonExpired;
	 @Column
	private   boolean accountNonLocked;
	 @Column
	private   boolean credentialsNonExpired;
	 @Column
	private   boolean enabled;
   @Column(name="online", length=1)
	  private Boolean isOnline;

	@ManyToMany(mappedBy = "userinfos")
	private Set<Roleinfo> roleinfos = new HashSet<Roleinfo>();
//用户状态
    private Short userState;
//上次登录时间
    private Date userLastlogin;
    //邮箱是否已激活
    private Boolean userEmailActivate;

  private boolean usersex;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	public Set<Roleinfo> getRoleinfos() {
		return roleinfos;
	}
	public void setRoleinfos(Set<Roleinfo> roleinfos) {
		this.roleinfos = roleinfos;
	}
   


 
 
	public Roleinfo addGroup(Roleinfo group) {
		if (group == null) {
			return null;
		}
		getRoleinfos().add(group);
		group.getUserinfos().add(this);

		return group;
	}

	public Roleinfo removeGroup(Roleinfo group) {
		if (group == null) {
			return null;
		}

		getRoleinfos().remove(group);
		group.getUserinfos().remove(this);

		return group;
	}
	
	
}
