package com.ilm.learning.mvcsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class MvcsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcsecurityApplication.class, args);
	}

   /*
   Not required as default is set in yml file
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth
              .inMemoryAuthentication()
              .withUser("zeyaul").password("password").roles("USER");
   }*/
}