package com.buswe.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityHelper {
	
	  private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);


	    public static Authentication getCurrentAuthenticateObject()  {
	        return SecurityContextHolder.getContext().getAuthentication();
	    }

	    public static UserDetails getCurrentUserDetails()  {
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if (principal instanceof UserDetails) {
	            return (UserDetails)principal;
	        }
	        return null;
	    }

	    public static String getCurrentUsername()  {
	        //DEV MODE
	        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

}
}
