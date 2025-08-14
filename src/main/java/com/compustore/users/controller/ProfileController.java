package com.compustore.users.controller;

import com.compustore.users.model.User;
import com.compustore.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class ProfileController {
  private final UserService service;
  public ProfileController(UserService service){ this.service = service; }

  @GetMapping("/profile")
  public ResponseEntity<?> profile(Authentication authentication){
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(401).build();
    }
    String username = authentication.getName(); // <- aquí ya viene el username
    User u = service.getByUsername(username);
    return ResponseEntity.ok(Map.of(
        "username", u.getUsername(),
        "email", u.getEmail(),
        "role", u.getRole()
    ));
  }
}
