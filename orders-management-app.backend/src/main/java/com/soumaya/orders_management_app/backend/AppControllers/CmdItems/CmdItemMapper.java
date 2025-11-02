package com.soumaya.orders_management_app.backend.AppControllers.CmdItems;

import com.soumaya.orders_management_app.backend.AppControllers.product.ProductMapper;
import com.soumaya.orders_management_app.backend.AppControllers.product.ProductResponse;
import com.soumaya.orders_management_app.backend.Models.CmdItem.CmdItem;
import com.soumaya.orders_management_app.backend.Models.CmdItem.CmdItemRepository;
import com.soumaya.orders_management_app.backend.Models.CmdItem.CmdOptions;
import com.soumaya.orders_management_app.backend.Models.produit.Product;
import com.soumaya.orders_management_app.backend.Models.produit.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CmdItemMapper {
    private final CmdItemRepository cmdItemRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CmdItem toCmdItem(CmdItemRequest request){
        Product product = productRepository.findByName(request.getProductName())
                .orElseThrow(()-> new EntityNotFoundException(
                        "produit: "+request.getProductName()+" n'existe pas"
                ));

        if (product.isDeleted()){
            throw new IllegalStateException("produit: "+product.getName()+" suppimé," +
                    " veuillez tout d'abord le restorer");
        }

        if (request.getQuantity() <= 0){
            throw new IllegalArgumentException("la quantité n'est pas valide");
        }
        return CmdItem.builder()
                .product(product)
                .quantity(request.getQuantity())
                .done(false)
                .deleted(false)
                .build();
    }

    public CmdItemResponse toCmdItemResponse(CmdItem item,String option){

        CmdOptions options;
        try{
            options = CmdOptions.valueOf(option.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("option: "+option+" n'est pas valide");
        }
        ProductResponse productResponse =
                productMapper.toProductResponse(item.getProduct());

        return CmdItemResponse.builder()
                .id(item.getId())
                .product(productResponse)
                .quantity(item.getQuantity())
                .done(item.isDone())
                .total(item.calculateTotal(options.getValue())).build();
    }
}
