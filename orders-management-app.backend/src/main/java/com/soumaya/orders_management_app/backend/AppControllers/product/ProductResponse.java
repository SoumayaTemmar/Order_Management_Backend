package com.soumaya.orders_management_app.backend.AppControllers.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponse {

    private int id;
    private String name;
    private String image;
    private float priceAtelier;
    private float shopPrice;

    private String CreatedBy;
}
