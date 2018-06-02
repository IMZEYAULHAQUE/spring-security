package com.ilm.learning.mvcsecurity.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsService implements UserDetailsService {

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return null;
   }

   public static void main(String[] args) {
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      String bcryptPassword = encoder.encode("zeyaul");
      System.out.println(bcryptPassword);
   }
}
