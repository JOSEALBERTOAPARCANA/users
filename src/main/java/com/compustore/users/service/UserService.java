package com.compustore.users.service;

import com.compustore.users.dto.UserRegisterRequest;
import com.compustore.users.model.User;

/**
 * Servicio que gestiona la lógica de negocio relacionada con usuarios.
 * Define las operaciones principales de registro y búsqueda de usuarios.
 */
public interface UserService {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param req DTO con datos de registro: username, password, email y rol.
     * @return el usuario persistido en la base de datos.
     * @throws IllegalArgumentException si el username o el email ya existen.
     */
    User register(UserRegisterRequest req);

    /**
     * Obtiene un usuario a partir de su nombre de usuario.
     *
     * @param username nombre de usuario a buscar.
     * @return el usuario encontrado.
     * @throws java.util.NoSuchElementException si no se encuentra.
     */
    User getByUsername(String username);
}
