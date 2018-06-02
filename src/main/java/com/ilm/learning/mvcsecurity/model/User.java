package com.ilm.learning.mvcsecurity.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "first_name")
   @NotNull
   private String firstName;

   @Column(name = "last_name")
   @NotNull
   private String lastName;

   @Column(name = "username")
   @NotNull
   @Size(min = 3, max = 20)
   private String username;

   @Column(name = "password")
   @NotNull
   private String password;

   @NotEmpty
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "granted_authority",
           joinColumns = { @JoinColumn(name = "user_id") },
           inverseJoinColumns = { @JoinColumn(name = "role_id") })
   private Set<UserRole> userRoles = new HashSet<>();

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<UserRole> getUserRoles() {
      return userRoles;
   }

   public void setUserRoles(Set<UserRole> userRoles) {
      this.userRoles = userRoles;
   }
}
