package com.compustore.users.service.impl;

import com.compustore.users.dto.UserRegisterRequest;
import com.compustore.users.model.User;
import com.compustore.users.repository.UserRepository;
import com.compustore.users.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de usuarios que gestiona
 * el registro y la obtención de usuarios desde la base de datos.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /**
     * Registra un nuevo usuario validando que el username y email no estén repetidos.
     *
     * @param req DTO con datos de registro.
     * @return el usuario persistido.
     * @throws IllegalArgumentException si el username o email ya existen.
     */
    @Override
    @Transactional
    public User register(UserRegisterRequest req) {
        // Validar duplicados
        if (repo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario '" + req.getUsername() + "' ya existe.");
        }
        if (req.getEmail() != null && repo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico '" + req.getEmail() + "' ya está registrado.");
        }

        // Crear y guardar usuario
        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword())) // Encriptar contraseña
                .email(req.getEmail())
                .role(req.getRole())
                .enabled(true)
                .build();

        return repo.save(u);
    }

    /**
     * Obtiene un usuario por su username.
     *
     * @param username nombre de usuario.
     * @return el usuario encontrado.
     * @throws java.util.NoSuchElementException si no se encuentra el usuario.
     */
    @Override
    public User getByUsername(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario con username '" + username + "' no encontrado."));
    }
}
