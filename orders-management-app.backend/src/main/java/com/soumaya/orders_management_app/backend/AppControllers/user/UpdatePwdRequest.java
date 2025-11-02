package com.soumaya.orders_management_app.backend.AppControllers.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePwdRequest {
    @NotBlank(message = "vous devez préciser l'ancien mot de passe")
    private String oldPassword;

    @NotBlank(message = "vous devez préciser le nouveau mot de passe")
    @Size(min = 10, message = "Pour un mot de passe solid; veuillez que ça soit au mois de longueur 10")
    private String newPassword;
}
