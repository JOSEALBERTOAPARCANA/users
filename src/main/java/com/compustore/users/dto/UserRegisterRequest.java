package com.compustore.users.dto;

import jakarta.validation.constraints.*;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class UserRegisterRequest {
  @NotBlank @Size(min=3, max=50) private String username;
  @NotBlank @Size(min=4, max=120) private String password;
  @Pattern(regexp="ADMIN|CLIENT", message="role must be ADMIN or CLIENT")
  private String role;
  @Email @Size(max=120) private String email;
}
