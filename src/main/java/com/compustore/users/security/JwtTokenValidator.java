package com.compustore.users.security;

import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {
  private final SecretKey key;
  public JwtTokenValidator(@Value("${app.jwt.secret}") String secret){
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }
  public Jws<Claims> parse(String token){
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
  }
}
