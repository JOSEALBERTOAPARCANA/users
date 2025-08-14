package com.compustore.users.controller;

import com.compustore.users.dto.*;
import com.compustore.users.model.User;
import com.compustore.users.security.JwtTokenProvider;
import com.compustore.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {
  private final AuthenticationManager authManager;
  private final JwtTokenProvider jwt;
  private final UserService userService;

  public AuthController(AuthenticationManager authManager, JwtTokenProvider jwt, UserService userService) {
    this.authManager = authManager; this.jwt = jwt; this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest req){
    User u = userService.register(req);
    return ResponseEntity.status(201).body(Map.of("username", u.getUsername(), "role", u.getRole()));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
    Authentication a = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
    var user = userService.getByUsername(a.getName());
    return ResponseEntity.ok(new AuthResponse(jwt.generateToken(user.getUsername(), user.getRole())));
  }
}
