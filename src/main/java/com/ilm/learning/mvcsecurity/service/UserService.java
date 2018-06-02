package com.ilm.learning.mvcsecurity.service;

import com.ilm.learning.mvcsecurity.model.User;
import com.ilm.learning.mvcsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

   @Autowired
   private UserRepository repository;

   public User findByUsername(String username) {
      return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username = " + username + " not found."));
   }

   public User findById(Long userId) {
      return repository.findById(userId).orElseThrow(() -> new RuntimeException("User with Id = " + userId + " not found."));
   }

   public List<User> findAll() {
      return repository.findAll();
   }
}
