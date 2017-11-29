package com.buswe.core.security;

import com.buswe.module.core.dao.AuthorityDao;
import com.buswe.module.core.dao.RoleinfoDao;
import com.buswe.module.core.dao.UserinfoDao;
import com.buswe.module.core.entity.Authority;
import com.buswe.module.core.entity.Roleinfo;
import com.buswe.module.core.entity.Userinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by mason on 6/30/14.
 */

@Service
public class JPAUserdetailsManager extends JPAUserDetailsService implements UserDetailsManager, GroupManager,AuthorityManager {

	private final Logger logger = LoggerFactory.getLogger(JPAUserdetailsManager.class);
	
    @Autowired
    private UserinfoDao userinfoDao;

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private RoleinfoDao roleinfoDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean enableEncodePassword = true;
    
    private AuthenticationManager authenticationManager;

    private UserCache userCache = new NullUserCache();

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

 

    //~ GroupManager implementation ====================================================================================

    @Override
    public List<String> findAllGroups() {
        List<Roleinfo> groups = roleinfoDao.findAll();

        Set<String> groupNames = new HashSet<>();
        if(groups != null){
           for (Roleinfo roleinfo:groups)
           {
               groupNames.add(roleinfo.getName());
           }
        }

        return new ArrayList<>(groupNames);
    }

    @Override
    public List<String> findUsersInGroup(String groupName) {
        Assert.hasText(groupName);

        Roleinfo group = getGroupWithName(groupName);

        Set<String> users = new HashSet<>();
        for (Userinfo userinfo:group.getUserinfos())
        {
            users.add(userinfo.getUsername());
        }
        return new ArrayList<>(users);
    }

    @Override
    public void createGroup(String groupName, List<GrantedAuthority> authorities) {
        Assert.hasText(groupName);
        Assert.notNull(authorities);

        logger.debug("Creating new group '" + groupName + "' with authorities " +
                AuthorityUtils.authorityListToSet(authorities));

        Roleinfo group = new Roleinfo();
        group.setName(groupName);

        insertGroupAuthorities(group, authorities);

        roleinfoDao.save(group);
    }

    @Override
    public void deleteGroup(String groupName) {
        List<Roleinfo> models = roleinfoDao.findByName(groupName);
        if(models != null){
            roleinfoDao.deleteInBatch(models);
          //  roleinfoDao.deleteAll(models);
        }
    }

    @Override
    public void renameGroup(String oldName, String newName) {
    	Roleinfo model = getGroupWithName(oldName);

        model.setName(newName);
        roleinfoDao.saveAndFlush(model);
    }

    @Override
    public void addUserToGroup(String username, String groupName) {
    	Roleinfo group = getGroupWithName(groupName);

        Userinfo user = getUserWithName(username);
        group.addUser(user);
        roleinfoDao.saveAndFlush(group);
    }

    @Override
    public void removeUserFromGroup(String username, String groupName) {
        Roleinfo group = getGroupWithName(groupName);

        Userinfo user = getUserWithName(username);
        group.removeUser(user);
        roleinfoDao.saveAndFlush(group);
    }

    @Override
    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        logger.debug("Loading authorities for group '" + groupName + "'");
        Assert.hasText(groupName);

       Roleinfo group = getGroupWithName(groupName);

        List<GrantedAuthority> list=new ArrayList<GrantedAuthority>(0);
        for (Authority authority:group.getAuthorities())
        {
            list.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return list;
    }

    @Override
    public void addGroupAuthority(String groupName, GrantedAuthority authority) {
        Roleinfo group = getGroupWithName(groupName);

        Authority auth = this.getAuthorityWithName(authority.getAuthority());
        group.addAuthority(auth);
        roleinfoDao.saveAndFlush(group);
    }

    @Override
    public void removeGroupAuthority(String groupName, GrantedAuthority authority) {
        Roleinfo group = getGroupWithName(groupName);

        Authority auth = this.getAuthorityWithName(authority.getAuthority());
        group.removeAuthority(auth);
        roleinfoDao.saveAndFlush(group);
    }

    //~ UserDetailsManager implementation ==============================================================================
    @Override
    public void createUser(final UserDetails userDetails) {
        validateUserDetails(userDetails);

        JPAUserDetails jpaUserDetails = (JPAUserDetails) userDetails;

        Userinfo user = jpaUserDetails.getUserinfo();

        if(enableEncodePassword){
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

      //  setDefaultInformation(user);

        userinfoDao.save(user);
    }

    @Override
    public void updateUser(final UserDetails userDetails) {
    	validateUserDetails(userDetails);

        JPAUserDetails jpaUserDetails = (JPAUserDetails) userDetails;

        Userinfo model = jpaUserDetails.getUserinfo();

        userinfoDao.saveAndFlush(model);

    	userCache.removeUserFromCache(userDetails.getUsername());
    }

    @Override
    public void deleteUser(String username) {
    	 Userinfo  models = userinfoDao.findByUsername(username);
    		userinfoDao.delete(models);
    	userCache.removeUserFromCache(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    	Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current userDetails.");
        }
        String username = currentUser.getName();

     // If an authentication manager has been set, re-authenticate the userDetails with the supplied password.
        if (authenticationManager != null) {
            logger.debug("Reauthenticating userDetails '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        logger.debug("Changing password for userDetails '"+ username + "'");

        Userinfo model = getUserWithName(username);
        
        if(enableEncodePassword){
        	model.setPassword(passwordEncoder.encode(newPassword));
        } else {
            model.setPassword(newPassword);
        }
        
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));

        userCache.removeUserFromCache(username);
    }

	@Override
    public boolean userExists(String username) {
       Userinfo model = userinfoDao.findByUsername(username);
        if (model==null) {
            throw new IncorrectResultSizeDataAccessException("More than one userDetails found with name '" + username + "'", 1);
        }

        return true;
    }


    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        Assert.isInstanceOf(JPAUserDetails.class, user, "UserDetails is not JPAUserDetails or the subclass");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");

        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }




    public void deleteRole(String roleName) {
    	Roleinfo role =roleinfoDao.findByName(roleName).get(0);

        roleinfoDao.delete(role);
    }


    public boolean roleExists(String roleName){
    	Authority roles = authorityDao.findByName(roleName);

        if (roles==null) {
            throw new IncorrectResultSizeDataAccessException("More than one userDetails found with name '" + roleName + "'", 1);
        }

        return true;
    }



    //~ private method for internal use
    private Roleinfo getGroupWithName(String groupName) {
        List<Roleinfo> groups = roleinfoDao.findByName(groupName);
        return groups.get(0);
    }


    private Userinfo getUserWithName(String username) {

        return userinfoDao.findByUsername(username);
    }

    private void insertGroupAuthorities(Roleinfo group, List<GrantedAuthority> authorities) {
        Set<Authority> roles = getRolesWithAuthorities(authorities);
        group.setAuthorities(roles);
    }


    private Authority getAuthorityWithName(String authName){
        return authorityDao.findByName(authName);
    }

   

    private Set<Authority> getRolesWithAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<Authority> roles = new HashSet<>();
        for (GrantedAuthority auth : authorities) {
        	roles.add(authorityDao.findByName(auth.getAuthority()));
        }
        return roles;
    }

    private Authentication createNewAuthentication(Authentication currentUser,
                                                   String newPassword) {
        UserDetails user = loadUserByUsername(currentUser.getName());

        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentUser.getDetails());

        return newAuthentication;
    }

}

