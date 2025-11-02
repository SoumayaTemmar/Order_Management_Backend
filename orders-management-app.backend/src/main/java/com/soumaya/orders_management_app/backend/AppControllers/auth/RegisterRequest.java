package com.soumaya.orders_management_app.backend.AppControllers.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class RegisterRequest {

    @NotBlank(message = "veuillez préciser le prénom")
    private String firstName;

    @NotBlank(message = "veuillez préciser le nom")
    private String lastName;

    @NotBlank(message = "le mot de passe est primordial")
    @Size(min = 10, message = "Pour un mot de passe solid; veuillez que ça soit au mois de longueur 10")
    private String password;

    @Size(min = 10, max = 10, message = "le numero de tel doit contenir 10 chiffres")
    private String tel;
    @NotBlank(message = "veuillez préciser le nom d'utilisateur")
    private String username;

    @NotNull(message = "le role de l'utilisateur est primordial")
    @Size(min = 1, message = "l'utilisateur doit avoir au moins un role")
    private Set<@NotBlank(message = "veuillez préciser le role") String> roles;
}
