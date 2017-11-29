package com.buswe.core.dao.jpa;

import com.buswe.core.security.SecurityHelper;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public class AuditorAwareImpl
  implements AuditorAware<UserDetails>
{
  public  UserDetails getCurrentAuditor()
  {
    UserDetails userDetails=SecurityHelper.getCurrentUserDetails();
    return  userDetails;
  }
}
