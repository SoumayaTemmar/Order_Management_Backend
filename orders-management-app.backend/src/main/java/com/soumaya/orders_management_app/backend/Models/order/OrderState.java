package com.soumaya.orders_management_app.backend.Models.order;

import lombok.Getter;

@Getter
public enum OrderState {
    EN_ATTENTE("commande en attente","EN_ATTENTE"),
    TERMINEE("commande Teminée","TERMINEE"),
    LIVREE("commande livrée", "LIVREE");

    private final String description;
    private final String value;

    OrderState(String description, String value){
        this.description = description;
        this.value = value;
    }


}
