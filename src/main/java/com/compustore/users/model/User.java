package com.compustore.users.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="users",
  uniqueConstraints = {
    @UniqueConstraint(name="uk_user_username", columnNames="username"),
    @UniqueConstraint(name="uk_user_email", columnNames="email")
})
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length=50)
  private String username;

  @Column(nullable=false)
  private String password;

  @Column(length=120)
  private String email;

  @Column(nullable=false, length=20)
  private String role; // ADMIN | CLIENT

  @Column(nullable=false)
  private Boolean enabled = true;

  @Column(name="created_at", nullable=false)
  private LocalDateTime createdAt;

  @Column(name="updated_at", nullable=false)
  private LocalDateTime updatedAt;

  @PrePersist void prePersist(){ createdAt = updatedAt = LocalDateTime.now(); }
  @PreUpdate  void preUpdate(){ updatedAt = LocalDateTime.now(); }
}
