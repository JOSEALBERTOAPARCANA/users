package com.compustore.users.security;

import com.compustore.users.model.User;
import com.compustore.users.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository repo;
  public CustomUserDetailsService(UserRepository repo){ this.repo = repo; }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    return org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
        .password(u.getPassword()).roles(u.getRole()).disabled(!u.getEnabled()).build();
  }
}
