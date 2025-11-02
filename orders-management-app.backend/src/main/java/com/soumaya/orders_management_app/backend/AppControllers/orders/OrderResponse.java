package com.soumaya.orders_management_app.backend.AppControllers.orders;

import com.soumaya.orders_management_app.backend.AppControllers.CmdItems.CmdItemResponse;
import com.soumaya.orders_management_app.backend.Models.order.OrderState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {

    private int id;
    private String clientFullName;
    private String tel;
    private List<CmdItemResponse> items;
    private LocalDate deliveryDate;
    private float totalPrice;
    private OrderState state;
    private boolean atelier;
}
