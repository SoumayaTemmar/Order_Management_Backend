package com.soumaya.orders_management_app.backend.AppControllers.orders;

import com.soumaya.orders_management_app.backend.ExceptionHandling.OperationNotPermittedException;
import com.soumaya.orders_management_app.backend.Models.order.Order;
import com.soumaya.orders_management_app.backend.Models.order.OrderRepository;
import com.soumaya.orders_management_app.backend.Models.order.OrderState;
import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;
    private final OrdersMapper ordersMapper;

    //add new order
    @Transactional
    public StandardResponse addOrder(OrderRequest request){
        return StandardResponse.builder()
                .id(orderRepository.save(ordersMapper.toOrder(request)).getId())
                .message("commande ajoutée !")
                .build();
    }
    //soft delete
    @Transactional
    public StandardResponse deleteSoftOrder(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande "+ id + " n'existe pas"
                ));

        if (order.isDeleted()){
            throw new IllegalStateException("commande déjà supprimée !");
        }
        order.setDeleted(true);
        order.getItems().forEach(item -> item.setDeleted(true));
        orderRepository.save(order);

        return StandardResponse.builder()
                .id(id)
                .message("commande supprimée !")
                .build();
    }
    //restore order
    @Transactional
    public StandardResponse restoreOrder(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande "+ id + " n'existe pas"
                ));

        if (!order.isDeleted()){
            throw new IllegalStateException("commande déjà restorée !");
        }
        order.setDeleted(false);
        order.getItems().forEach(item -> item.setDeleted(false));
        orderRepository.save(order);

        return StandardResponse.builder()
                .id(id)
                .message("commande restorée !")
                .build();
    }
    //delete permanently
    @Transactional
    public StandardResponse deleteOrder(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande "+ id + " n'existe pas"
                ));

        orderRepository.delete(order);

        return StandardResponse.builder()
                .id(id)
                .message("commande supprimée définitivement !")
                .build();
    }
    //get by id
    public OrderResponse getOrderById(int id, String option){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande "+ id + " n'existe pas"
                ));

        return ordersMapper.toOrderResponse(order);
    }
    //get all
    public PageResponse<OrderResponse> getAllOrders(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("deliveryDate"));
        Page<Order> orders = orderRepository.findAllByDeletedFalseAndStateNot(OrderState.LIVREE,pageable);

        List<OrderResponse> orderResponses = orders.stream()
                .map(ordersMapper::toOrderResponse)
                .toList();

        return new PageResponse<>(
                orderResponses,
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalPages(),
                orders.getTotalElements()
        );
    }
    //get deleted orders
    public PageResponse<OrderResponse> getDeletedOrders(int page, int size,String option){
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate"));
        Page<Order> orders = orderRepository.findAllByDeletedTrue(pageable);

        List<OrderResponse> orderResponses = orders.stream()
                .map(ordersMapper::toOrderResponse)
                .toList();

        return new PageResponse<>(
                orderResponses,
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalPages(),
                orders.getTotalElements()
        );
    }

    //update order
    @Transactional
    public StandardResponse updateOrder(UpdateRequest request,int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande : "+ id + " n'existe pas"
                ));
        if(order.getState() == OrderState.LIVREE){
            throw new OperationNotPermittedException(
                    "vous ne pouvez pas modifier une commande déjà livrée"
            );
        }
        order = ordersMapper.updateOrder(order, request);
        orderRepository.save(order);

        return StandardResponse.builder()
                .id(id)
                .message("commande modifiée !")
                .build();

    }

}
