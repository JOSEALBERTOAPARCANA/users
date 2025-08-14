package com.compustore.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.compustore.users.model.User;

/**
 * Repositorio JPA para la entidad {@link User}.
 * Proporciona métodos para buscar usuarios por username o email.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su username.
     * 
     * @param username nombre de usuario.
     * @return Optional con el usuario si existe.
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el username especificado.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email especificado.
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario con username o email (útil para login flexible).
     *
     * @param keyword username o email a buscar.
     * @return Optional con el usuario si existe.
     */
    @Query("SELECT u FROM User u WHERE u.username = :keyword OR u.email = :keyword")
    Optional<User> findByUsernameOrEmail(@Param("keyword") String keyword);
}
