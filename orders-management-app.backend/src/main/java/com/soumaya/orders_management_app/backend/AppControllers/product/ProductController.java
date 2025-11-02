package com.soumaya.orders_management_app.backend.AppControllers.product;

import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<PageResponse<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @GetMapping("/trash/all")
    public ResponseEntity<PageResponse<ProductResponse>> getAllDeleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(productService.getDeletedProducts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @PathVariable int id
    ){
        return ResponseEntity.ok(productService.getProductById(id));
    }
    // delete, restore, soft delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse> deleteProduct(
            @PathVariable int id
    ){
        return ResponseEntity.ok(productService.DeleteProduct(id));
    }

    @PatchMapping("/delete/soft/{id}")
    public ResponseEntity<StandardResponse> softDeleteProduct(
            @PathVariable int id
    ){
        return ResponseEntity.ok(productService.softDeleteProduct(id));
    }

    @PatchMapping("/trash/restore/{id}")
    public ResponseEntity<StandardResponse> restoreProduct(
            @PathVariable int id
    ){
        return ResponseEntity.ok(productService.restoreProduct(id));
    }

    //add, update
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StandardResponse> addProduct(
            @ModelAttribute @Valid ProductRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(request));
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StandardResponse> updateProduct(
            @PathVariable int id,
            @ModelAttribute @Valid ProductRequest request

    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.updateProduct(id, request));
    }


}
