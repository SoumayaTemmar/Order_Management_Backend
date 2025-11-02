package com.soumaya.orders_management_app.backend.AppControllers.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductRequest {

    private MultipartFile image;

    @NotBlank(message = "le nom est primordial")
    private String name;

    @NotNull(message = "le prix Atelier est primordial")
    private float priceAtelier;

    @NotNull(message = "prix Magasin est primordial")
    private float shopPrice;

}
