package com.soumaya.orders_management_app.backend.AppControllers.CmdItems;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CmdItemRequest {
    private String productName;
    private int quantity;
}
