package com.soumaya.orders_management_app.backend.AppControllers.CmdItems;

import com.soumaya.orders_management_app.backend.AppControllers.orders.OrderResponse;
import com.soumaya.orders_management_app.backend.AppControllers.orders.OrdersMapper;
import com.soumaya.orders_management_app.backend.Models.CmdItem.CmdItem;
import com.soumaya.orders_management_app.backend.Models.CmdItem.CmdItemRepository;
import com.soumaya.orders_management_app.backend.Models.order.MonthlySalesDto;
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
public class CmdItemService {
    private final CmdItemRepository cmdItemRepository;
    private final OrderRepository orderRepository;
    private final OrdersMapper ordersMapper;

    @Transactional
    public StandardResponse checkItem(int id){
        CmdItem item = cmdItemRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "item "+ id + " n'exist pas"
                ));

        item.setDone(!item.isDone());
        cmdItemRepository.save(item);
        return StandardResponse.builder()
                .id(id)
                .message("item validé !").build();
    }
    //get done orders
    public PageResponse<OrderResponse> getAllDoneOrders(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("deliveryDate"));
        Page<Order> orders = orderRepository.findAllByStateTerminee(pageable);

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
    //get order en attente
    public PageResponse<OrderResponse> getAllEnAttenteOrders(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("deliveryDate"));
        Page<Order> orders = orderRepository.findAllByStateEnAttente(pageable);

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
    //get monthly sales Orders
    public List<MonthlySalesDto> getMonthlySales(){
        return orderRepository.getMonthlySales();
    }
    // done order
    @Transactional
    public StandardResponse updateStateToTerminee(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande avec id: "+id+" n'existe pas"
                ));

        order.setState(OrderState.valueOf("TERMINEE"));
        orderRepository.save(order);
        return StandardResponse.builder()
                .id(id)
                .message("Etat de commande Modifie!")
                .build();
    }

    // deliver Order
    @Transactional
    public StandardResponse updateStateToDelivered(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande avec id: "+id+" n'existe pas"
                ));

        order.setState(OrderState.valueOf("LIVREE"));
        orderRepository.save(order);
        return StandardResponse.builder()
                .id(id)
                .message("Etat de commande Modifie!")
                .build();
    }

    // en attente state
    @Transactional
    public StandardResponse updateStateToEnAttente(int id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "commande avec id: "+id+" n'existe pas"
                ));

        order.setState(OrderState.valueOf("EN_ATTENTE"));
        orderRepository.save(order);
        return StandardResponse.builder()
                .id(id)
                .message("Etat de commande Modifie!")
                .build();
    }
}

