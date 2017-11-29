package com.buswe.core.security;

import com.buswe.module.core.entity.Userinfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class JPAUserDetails  implements UserDetails{
	private Userinfo userinfo;
	 List<GrantedAuthority> auths=new ArrayList<>();
	public JPAUserDetails(Userinfo userinfo)
	{
		this.userinfo=userinfo;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		    return auths;
	}

	public void setAuths(List<GrantedAuthority> auths) {
		this.auths = auths;
	}


	@Override
	public String getPassword() {
		return userinfo.getPassword();
	}

	@Override
	public String getUsername() {
		return userinfo.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return userinfo.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return userinfo.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return userinfo.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return userinfo.isEnabled();
	}


	public Userinfo getUserinfo() {
		return userinfo;
	}


 
}
