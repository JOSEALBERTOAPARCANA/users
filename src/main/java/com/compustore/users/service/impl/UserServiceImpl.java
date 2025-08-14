package com.compustore.users.service.impl;

import com.compustore.users.dto.UserRegisterRequest;
import com.compustore.users.model.User;
import com.compustore.users.repository.UserRepository;
import com.compustore.users.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repo; private final PasswordEncoder encoder;
  public UserServiceImpl(UserRepository repo, PasswordEncoder encoder){ this.repo=repo; this.encoder=encoder; }

  @Override @Transactional
  public User register(UserRegisterRequest req) {
    if (repo.existsByUsername(req.getUsername())) throw new IllegalArgumentException("username ya existe");
    if (req.getEmail()!=null && repo.existsByEmail(req.getEmail())) throw new IllegalArgumentException("email ya existe");
    User u = User.builder()
        .username(req.getUsername())
        .password(encoder.encode(req.getPassword()))
        .email(req.getEmail())
        .role(req.getRole())
        .enabled(true)
        .build();
    return repo.save(u);
  }

  @Override public User getByUsername(String username){ return repo.findByUsername(username).orElseThrow(); }
}
