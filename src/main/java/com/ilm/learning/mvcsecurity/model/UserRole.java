package com.ilm.learning.mvcsecurity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name = "user_role")
public class UserRole implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column
   @NotNull
   @Enumerated(EnumType.STRING)
   private UserRoleType role = UserRoleType.USER;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public UserRoleType getRole() {
      return role;
   }

   public void setRole(UserRoleType role) {
      this.role = role;
   }
}
