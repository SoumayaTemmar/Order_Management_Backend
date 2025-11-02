package com.soumaya.orders_management_app.backend.AppControllers.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginRequest {
    @NotBlank(message = "veuillez insérer le nom d'utilisateur")
    private String username;

    @NotBlank(message = "veuillez préciser le mot de passe")
    private String password;
}
