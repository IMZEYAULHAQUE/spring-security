package com.ilm.learning.mvcsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

   @Autowired
   @Qualifier("customUserDetailsService")
   private UserDetailsService userDetailsService;

   @Autowired
   public void configureGlobalSecurity(AuthenticationManagerBuilder builder) throws Exception {
      builder.userDetailsService(userDetailsService);
      builder.authenticationProvider(this.getAuthenticationProvider());
   }

   @Bean
   public AuthenticationProvider getAuthenticationProvider() {
      DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
      authenticationProvider.setUserDetailsService(userDetailsService);
      authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
      return authenticationProvider;
   }

   @Bean
   public AuthenticationTrustResolver getAuthenticationTrustResolver() {
      return new AuthenticationTrustResolverImpl();
   }

   /*@Override
   protected void configure(HttpSecurity http) throws Exception {
            http
              .formLogin().loginPage("/login.html").successForwardUrl("/home")*//*.failureForwardUrl("/userLogin")*//*
              .loginProcessingUrl("/userLogin").usernameParameter("username").passwordParameter("password").permitAll()
              .and()
              .logout()
              .logoutSuccessUrl("/home")
              .logoutUrl("/logout")
              .and()
              .csrf()
              .and()
              .exceptionHandling()
              .accessDeniedPage("/accessDenied")
              .and()
              .authorizeRequests()
              .antMatchers(HttpMethod.GET, "/user").hasAnyRole()
              .antMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
              .antMatchers(HttpMethod.DELETE, "/user").hasRole("ADMIN")
              .antMatchers("/home").permitAll()
              .antMatchers("/accessDenied").permitAll()
                    .antMatchers("/login").permitAll()
              .antMatchers("/**").authenticated();
   }*/

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http
              .csrf().and()
              //.authenticationProvider(this.getAuthenticationProvider())
              .authorizeRequests()
              .antMatchers("/home", "/accessDenied", "/login", "/loginFailed").permitAll()
              .and()
              //.formLogin().loginPage("/login").permitAll().successForwardUrl("/dashboard").failureForwardUrl("/loginFailed")
              .oauth2Login().loginPage("/oauth-login").permitAll().defaultSuccessUrl("/login/oauth2/dashboard-outh", true).failureUrl("/oauth-loginFailed")
              .and()
              .logout()
              .logoutSuccessUrl("/home")
              .logoutUrl("/logout")
              .and()
              .exceptionHandling()
              .accessDeniedPage("/accessDenied")
              .and()
              .authorizeRequests()
              .antMatchers(HttpMethod.GET, "/users/user/{userId}").hasAnyRole("ADMIN", "USER", "DBA")
              .antMatchers(HttpMethod.GET, "/users/createUser").hasRole("ADMIN")
              .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
              .antMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")
              .anyRequest().authenticated();


   }

   public static void  main(String[] args) {
      BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
      String pass = encoder.encode("zeyaul");
      System.out.println(pass);
   }
}
