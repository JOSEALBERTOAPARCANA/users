package com.compustore.users.security;

import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private final SecretKey key;
  private final long expirationMs;

  public JwtTokenProvider(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-ms}") long expirationMs
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMs = expirationMs;
  }

  public String generateToken(String username, String role){
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
        .subject(username)
        .claim("role", role)
        .issuedAt(now)
        .expiration(exp)
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  public Jws<Claims> validate(String token){
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token);
  }
}
