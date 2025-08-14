package com.compustore.users.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Clase responsable de validar y parsear tokens JWT.
 * Se asegura de que el token esté firmado correctamente y no haya expirado.
 */
@Component
public class JwtTokenValidator {

    private final SecretKey key;

    /**
     * Constructor que inicializa la clave secreta desde application.properties.
     *
     * @param secret Clave secreta para la firma del token.
     */
    public JwtTokenValidator(@Value("${app.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Parsea y valida un token JWT.
     *
     * @param token El token JWT a validar.
     * @return Los claims del token si es válido.
     * @throws JwtException Si el token es inválido o ha expirado.
     * @throws IllegalArgumentException Si el token es nulo o vacío.
     */
    public Jws<Claims> parse(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired", e);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }
}
