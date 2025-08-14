package com.compustore.users.security;

import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Clase responsable de generar y validar tokens JWT.
 * Utiliza un secreto configurado en application.properties para firmar los tokens.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMs;

    /**
     * Constructor: inicializa la clave y el tiempo de expiración a partir de las propiedades.
     *
     * @param secret       Clave secreta para firmar el token (debe tener longitud adecuada para HS256).
     * @param expirationMs Tiempo de expiración en milisegundos.
     */
    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /**
     * Genera un token JWT firmado con el usuario y el rol.
     *
     * @param username Nombre del usuario autenticado.
     * @param role     Rol del usuario (ADMIN o CLIENT).
     * @return Token JWT como String.
     */
    public String generateToken(String username, String role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty when generating JWT");
        }

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

    /**
     * Valida y parsea un token JWT.
     *
     * @param token Token JWT recibido.
     * @return Claims del token si es válido.
     * @throws JwtException si el token es inválido o ha expirado.
     */
    public Jws<Claims> validate(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
