package com.compustore.users.security;

import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Proveedor de utilidades para generar y validar tokens JWT.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey key;
    private final long expirationMs;

    /**
     * Constructor que inicializa la clave secreta y el tiempo de expiración.
     *
     * @param secret       Clave secreta usada para firmar los JWT.
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
     * Genera un token JWT con el usuario y rol especificados.
     *
     * @param username Nombre de usuario.
     * @param role     Rol del usuario (ADMIN o CLIENT).
     * @return Token JWT firmado.
     * @throws IllegalArgumentException Si los parámetros son nulos o vacíos.
     */
    public String generateToken(String username, String role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        logger.debug("Generando token para usuario: {} con rol: {}", username, role);

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
     * @param token Token JWT.
     * @return Los claims si el token es válido.
     * @throws JwtException Si el token es inválido o ha expirado.
     */
    public Jws<Claims> validate(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            logger.warn("El token JWT ha expirado: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            logger.error("Error al validar token JWT: {}", e.getMessage());
            throw e;
        }
    }
}
