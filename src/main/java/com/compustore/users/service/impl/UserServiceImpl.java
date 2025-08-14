package com.compustore.users.service.impl;

import com.compustore.users.dto.UserRegisterRequest;
import com.compustore.users.model.User;
import com.compustore.users.repository.UserRepository;
import com.compustore.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la lógica de negocio para usuarios.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /**
     * Registra un nuevo usuario validando duplicados.
     */
    @Override
    @Transactional
    public User register(UserRegisterRequest req) {
        logger.info("Iniciando registro de usuario: {}", req.getUsername());

        if (repo.existsByUsername(req.getUsername())) {
            logger.warn("Intento de registro con username duplicado: {}", req.getUsername());
            throw new IllegalArgumentException("El username ya existe");
        }

        if (req.getEmail() != null && repo.existsByEmail(req.getEmail())) {
            logger.warn("Intento de registro con email duplicado: {}", req.getEmail());
            throw new IllegalArgumentException("El email ya existe");
        }

        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role(req.getRole())
                .enabled(true)
                .build();

        logger.debug("Usuario listo para guardar en BD: {}", u.getUsername());
        return repo.save(u);
    }

    /**
     * Obtiene un usuario por su nombre.
     */
    @Override
    public User getByUsername(String username) {
        logger.debug("Buscando usuario por username: {}", username);
        return repo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado: {}", username);
                    return new IllegalArgumentException("Usuario no encontrado");
                });
    }
}
