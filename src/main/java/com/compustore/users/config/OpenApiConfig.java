package com.compustore.users.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI usersOpenAPI() {
    return new OpenAPI()
      .info(new Info().title("CompuStore Users API").version("v1")
        .description("Autenticación y emisión de JWT"))
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .components(new Components().addSecuritySchemes("bearerAuth",
        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
  }
}
