package com.buswe.core.security;

import com.buswe.module.core.dao.UserinfoDao;
import com.buswe.module.core.entity.Authority;
import com.buswe.module.core.entity.Roleinfo;
import com.buswe.module.core.entity.Userinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * Created by mason on 6/30/14.
 */
@Transactional("jpaTransaction")
public class JPAUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(JPAUserDetailsService.class);
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private UserinfoDao userinfoDao;
   

    private boolean enableRoles=true;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Userinfo user = userinfoDao.findByUsername(username);
        if (user == null) {
            logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");

            throw new UsernameNotFoundException(
                    messages.getMessage("JPAUserDetailsService.noAuthority",
                            new Object[] {username}, "User {0} has no GrantedAuthority"));
        }
        List<GrantedAuthority> auths = loadUserAuthorities(user);
        JPAUserDetails userDetails=new JPAUserDetails(user);
        userDetails.setAuths(auths);
        return userDetails;
    }
 
    protected JPAUserDetails loadUserDetailsByUsername(String username){
      return new JPAUserDetails(userinfoDao.findByUsername(username));
    }
    protected List<GrantedAuthority> loadUserAuthorities(UserDetails userDetails){
        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

        if(userDetails instanceof JPAUserDetails){
            JPAUserDetails jPAUserDetails = (JPAUserDetails) userDetails;

            dbAuthsSet.addAll(loadUserAuthorities(((JPAUserDetails) userDetails).getUserinfo()));

            if(enableRoles){
                for (Roleinfo roleinfo : jPAUserDetails.getUserinfo().getRoleinfos()){
                    dbAuthsSet.addAll(loadRoleinfoAuthorities(roleinfo));
                }
            }
        }
        return new ArrayList<>(dbAuthsSet);
    }

    protected List<GrantedAuthority> loadUserAuthorities(Userinfo user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(user != null){
            for (Roleinfo roleinfo:user.getRoleinfos()) {
                for (Authority authority : roleinfo.getAuthorities()) {
                    if (!authority.getEnable()) {
                        continue;
                    }
                    authorities.add(new SimpleGrantedAuthority(authority.getName()));
                }
            }
        }

        return authorities;
    }

    protected List<GrantedAuthority> loadRoleinfoAuthorities(Roleinfo roleinfo){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(enableRoles && roleinfo != null && roleinfo.getEnable()){
            for (Authority authority : roleinfo.getAuthorities()){
                if(!authority.getEnable()){
                    continue;
                }
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
        }
        return authorities;
    }

}