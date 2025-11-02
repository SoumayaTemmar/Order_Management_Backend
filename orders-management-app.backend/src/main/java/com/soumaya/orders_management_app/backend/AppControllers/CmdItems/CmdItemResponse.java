package com.soumaya.orders_management_app.backend.AppControllers.CmdItems;

import com.soumaya.orders_management_app.backend.AppControllers.product.ProductResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CmdItemResponse {
    private int id;
    private ProductResponse product;
    private int quantity;
    private float total;
    private boolean done;
}
