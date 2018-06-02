package com.ilm.learning.mvcsecurity.repository;

import com.ilm.learning.mvcsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
   public Optional<User> findByUsername(String username);
}
