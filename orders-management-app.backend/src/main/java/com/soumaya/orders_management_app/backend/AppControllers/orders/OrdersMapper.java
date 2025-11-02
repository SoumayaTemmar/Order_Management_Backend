package com.soumaya.orders_management_app.backend.AppControllers.orders;

import com.soumaya.orders_management_app.backend.AppControllers.CmdItems.CmdItemMapper;
import com.soumaya.orders_management_app.backend.AppControllers.CmdItems.CmdItemResponse;
import com.soumaya.orders_management_app.backend.Models.CmdItem.CmdItem;
import com.soumaya.orders_management_app.backend.Models.order.Order;
import com.soumaya.orders_management_app.backend.Models.order.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersMapper {
    private final CmdItemMapper cmdItemMapper;

    public Order toOrder(OrderRequest request){

        List<CmdItem> items = request.getItems().stream()
                .map(cmdItemMapper::toCmdItem)
                .toList();


        if(request.getDeliveryDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException(
                    "La date de recuperation n'est pas valide" +
                    " veuillez choisir une date  futur !"
            );
        }

        Order order = Order.builder()
                .clientFullName(request.getClientFullName())
                .tel(request.getTel())
                .atelier(request.isAtelier())
                .deliveryDate(request.getDeliveryDate())
                .totalPrice(request.getTotalPrice())
                .state(OrderState.valueOf("EN_ATTENTE"))
                .build();

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

       return order;
    }

    public OrderResponse toOrderResponse(Order order){

        String option;
        if (order.isAtelier()){
            option = "ATELIER";
        } else {
            option = "SHOP";
        }

        List<CmdItemResponse> items = order.getItems().stream()
                .map(item -> cmdItemMapper.toCmdItemResponse(item, option))
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .clientFullName(order.getClientFullName())
                .tel(order.getTel())
                .deliveryDate(order.getDeliveryDate())
                .items(items)
                .atelier(order.isAtelier())
                .totalPrice(order.getTotalPrice())
                .state(order.getState())
                .build();
    }
    public Order updateOrder(Order order, UpdateRequest request){

        OrderState state;
        try{
            state = OrderState.valueOf(request.getState().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("etat: "+ request.getState()+" n'est pas valid");
        }

        List<CmdItem> items = request.getItems().stream()
                .map(cmdItemMapper::toCmdItem)
                .collect(Collectors.toCollection(ArrayList::new));

        items.forEach(item -> item.setOrder(order));
        order.getItems().clear();
        order.getItems().addAll(items);

        order.setTel(request.getTel());
        order.setClientFullName(request.getClientFullName());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setAtelier(request.isAtelier());
        order.setTotalPrice(request.getTotalPrice());
        order.setState(state);
        return order;
    }
}
