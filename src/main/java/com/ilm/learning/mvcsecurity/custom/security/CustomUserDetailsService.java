package com.ilm.learning.mvcsecurity.custom.security;

import com.ilm.learning.mvcsecurity.model.User;
import com.ilm.learning.mvcsecurity.model.UserRole;
import com.ilm.learning.mvcsecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

   Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

   @Autowired
   private UserService userService;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      logger.info("loadUserByUsername called");
      User user = userService.findByUsername(username);
      return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, this.getGrantedAuthorities(user.getUserRoles()));
   }

   private Collection<? extends GrantedAuthority> getGrantedAuthorities(Set<UserRole> userRoles) {
      return userRoles.stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name())).collect(Collectors.toSet());
   }

}
