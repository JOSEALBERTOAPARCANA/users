package com.compustore.users.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa a un usuario en el sistema.
 * Contiene información de autenticación, rol y fechas de creación/actualización.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_user_email", columnNames = "email")
    }
)
public class User {

    /** Identificador único del usuario (autogenerado). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único (obligatorio). */
    @Column(nullable = false, length = 50)
    private String username;

    /** Contraseña encriptada (obligatoria). */
    @Column(nullable = false)
    private String password;

    /** Correo electrónico único (opcional). */
    @Column(length = 120)
    private String email;

    /** Rol del usuario (ADMIN o CLIENT). */
    @Column(nullable = false, length = 20)
    private String role;

    /** Estado de activación del usuario. */
    @Column(nullable = false)
    private Boolean enabled = true;

    /** Fecha de creación del usuario. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Fecha de última actualización. */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** Se ejecuta antes de insertar un nuevo registro para establecer las fechas iniciales. */
    @PrePersist
    void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    /** Se ejecuta antes de actualizar un registro para refrescar la fecha de actualización. */
    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
