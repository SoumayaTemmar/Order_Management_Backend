package com.soumaya.orders_management_app.backend.AppControllers.CmdItems;

import com.soumaya.orders_management_app.backend.AppControllers.orders.OrderResponse;
import com.soumaya.orders_management_app.backend.Models.order.MonthlySalesDto;
import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cmd")
@RequiredArgsConstructor
public class CmdItemController {

    private final CmdItemService cmdItemService;

    @PatchMapping("/done/{id}")
    public ResponseEntity<StandardResponse> checkItem(
            @PathVariable int id
    ){
        return ResponseEntity.ok(cmdItemService.checkItem(id));
    }

    @GetMapping("/terminees")
    public ResponseEntity<PageResponse<OrderResponse>> getDoneOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(cmdItemService.getAllDoneOrders(page, size));
    }

    @GetMapping("/enAttente")
    public ResponseEntity<PageResponse<OrderResponse>> getEnAttenteOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(cmdItemService.getAllEnAttenteOrders(page, size));
    }
    @GetMapping("/history")
    public ResponseEntity<List<MonthlySalesDto>> geMonthlySales(){
        return ResponseEntity.ok(cmdItemService.getMonthlySales());
    }
    @PatchMapping("/terminee/{id}")
    public ResponseEntity<StandardResponse> orderDone(
            @PathVariable int id
    ){
        return ResponseEntity.ok(cmdItemService.updateStateToTerminee(id));
    }

    @PatchMapping("/deliver/{id}")
    public ResponseEntity<StandardResponse> orderDelivered(
            @PathVariable int id
    ){
        return ResponseEntity.ok(cmdItemService.updateStateToDelivered(id));
    }

    @PatchMapping("/enAttente/{id}")
    public ResponseEntity<StandardResponse> orderEnAttente(
            @PathVariable int id
    ){
        return ResponseEntity.ok(cmdItemService.updateStateToEnAttente(id));
    }
}
