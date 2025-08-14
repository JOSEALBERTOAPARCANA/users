package com.compustore.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Filtro que intercepta cada petición HTTP para validar el JWT en la cabecera Authorization.
 */
@Component
public class JwtAuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtTokenValidator validator;

    public JwtAuthFilter(JwtTokenValidator validator) {
        this.validator = validator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Claims claims = validator.parse(token).getPayload();
                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                logger.debug("JWT válido para usuario: {} con rol: {}", username, role);

                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                logger.error("Error al validar token JWT: {}", e.getMessage());

                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                new ObjectMapper().writeValue(res.getOutputStream(),
                        Map.of("error", "Invalid or expired token", "details", e.getMessage()));
                return;
            }
        } else {
            logger.debug("No se encontró cabecera Authorization o formato inválido.");
        }

        chain.doFilter(request, response);
    }
}
