package com.soumaya.orders_management_app.backend.AppControllers.orders;

import com.soumaya.orders_management_app.backend.AppControllers.CmdItems.CmdItemRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class UpdateRequest {
    @NotBlank(message = "veuillez preciser le nom du client")
    private String clientFullName;

    @NotBlank(message = "veuillez insérer le contact du client")
    private String tel;

    private LocalDate deliveryDate;
    @NotNull(message = "veuillez préciser d'ou la commande sera récuperée: Magasin / Atelier")
    private boolean atelier;

    @NotNull(message = "list items est primordiale")
    @Size(min = 1, message = "veuillez préciser au moins un produit, pour passer la commande")
    private List<CmdItemRequest> items;

    @NotNull(message = "veuillez préciser l'etat de la commande")
    private String state;

    @NotNull(message = "veuillez preciser le prix total")
    private float totalPrice;
}
