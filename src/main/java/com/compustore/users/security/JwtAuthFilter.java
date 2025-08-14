package com.compustore.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Filtro de autenticación JWT que intercepta todas las solicitudes HTTP
 * y valida el token enviado en el encabezado Authorization.
 *
 * Si el token es válido, se establece la autenticación en el contexto de Spring Security.
 */
@Component
public class JwtAuthFilter implements Filter {

    private final JwtTokenValidator validator;

    public JwtAuthFilter(JwtTokenValidator validator) {
        this.validator = validator;
    }

    /**
     * Intercepta cada solicitud, extrae el token JWT del encabezado y valida su autenticidad.
     * Si es válido, asigna la autenticación al contexto de seguridad.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Obtener el header Authorization
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();

            // Validar que el token no esté vacío
            if (!token.isEmpty()) {
                try {
                    Claims claims = validator.parse(token).getPayload();
                    String username = claims.getSubject();
                    String role = claims.get("role", String.class);

                    // Crear la autenticación con el rol asignado
                    var auth = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

                    // Guardar en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(auth);

                } catch (Exception e) {
                    // Token inválido o expirado → devolver 401
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                    new ObjectMapper().writeValue(
                            res.getOutputStream(),
                            Map.of("error", "Invalid or expired token")
                    );
                    return;
                }
            }
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
}
