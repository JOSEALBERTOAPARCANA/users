package com.compustore.users.service;

import com.compustore.users.dto.UserRegisterRequest;
import com.compustore.users.model.User;

/**
 * Servicio para gestión de usuarios.
 */
public interface UserService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param req datos del usuario a registrar.
     * @return Usuario creado.
     */
    User register(UserRegisterRequest req);

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param username nombre de usuario.
     * @return Usuario encontrado.
     */
    User getByUsername(String username);
}
