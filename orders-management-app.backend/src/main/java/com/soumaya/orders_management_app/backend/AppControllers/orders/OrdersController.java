package com.soumaya.orders_management_app.backend.AppControllers.orders;

import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    // all get requests

    @GetMapping("/all")
    public ResponseEntity<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(ordersService.getAllOrders(page, size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable int id,
            @RequestParam(defaultValue = "SHOP") String option
    ){
        return ResponseEntity.ok(ordersService.getOrderById(id, option));
    }

    @GetMapping("/trash/all")
    public ResponseEntity<PageResponse<OrderResponse>> getTrash(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "SHOP") String option
    ){
        return ResponseEntity.ok(ordersService.getDeletedOrders(page, size,option));
    }
    // add and update
    @PostMapping("/add")
    public ResponseEntity<StandardResponse> addOrder(
            @RequestBody @Valid OrderRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordersService.addOrder(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponse> updateOrder(
            @PathVariable int id,
            @RequestBody @Valid UpdateRequest request
    ){
        return ResponseEntity.ok(ordersService.updateOrder(request, id));
    }

    // delete(soft & permanent) and restore
    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> softDeleteOrder(
            @PathVariable int id
    ){
        return ResponseEntity.ok(ordersService.deleteSoftOrder(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deleteOrder(
            @PathVariable int id
    ){
        return ResponseEntity.ok(ordersService.deleteOrder(id));
    }

    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restoreOrder(
            @PathVariable int id
    ){
        return ResponseEntity.ok(ordersService.restoreOrder(id));
    }

}