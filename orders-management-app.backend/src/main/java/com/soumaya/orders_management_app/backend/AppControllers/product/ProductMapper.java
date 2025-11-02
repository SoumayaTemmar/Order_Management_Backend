package com.soumaya.orders_management_app.backend.AppControllers.product;

import com.soumaya.orders_management_app.backend.ExceptionHandling.OperationNotPermittedException;
import com.soumaya.orders_management_app.backend.Models.User.User;
import com.soumaya.orders_management_app.backend.Models.User.UserRepository;
import com.soumaya.orders_management_app.backend.Models.produit.Product;
import com.soumaya.orders_management_app.backend.Models.produit.ProductRepository;
import com.soumaya.orders_management_app.backend.file.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final UploadFileService uploadFileService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Product toProduct(ProductRequest request){

        String imagePath = null;

        if(request.getImage() != null && !request.getImage().isEmpty()){
            String imageName = request.getImage().getOriginalFilename();

            if(imageName!= null && !imageName.isEmpty()){
                imagePath = uploadFileService.saveFile(request.getImage());
            }
        }

        return Product.builder()
                .name(request.getName())
                .imagePath(imagePath)
                .priceAtelier(request.getPriceAtelier())
                .shopPrice(request.getShopPrice())
                .build();
    }

    public ProductResponse toProductResponse(Product product){

        User user = userRepository.findById(product.getCreatedBy())
                .orElseThrow();

        //return a decent path : ex ---> http://localhost:8080/uploads/125751297.png or null

        String imagePath = product.getImagePath() != null
                ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/")
                .path(product.getImagePath())
                .toUriString()
                : null;

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .image(imagePath)
                .priceAtelier(product.getPriceAtelier())
                .shopPrice(product.getShopPrice())
                .CreatedBy(user.getFullName())
                .build();
    }

    public Product updateProduct(ProductRequest request, Product product){

        if (!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName())){
            throw new OperationNotPermittedException(
                    "produit: " + request.getName()+ " existe déjà," +
                            " ou pas supprimé définitivement, veuillez choisir un autre nom"
            );
        }
        product.setName(request.getName());
        product.setPriceAtelier(request.getPriceAtelier());
        product.setShopPrice(request.getShopPrice());

        //image path

        String newImagePath = null;

        if (request.getImage()!= null && !request.getImage().isEmpty()){
            String imageName = request.getImage().getOriginalFilename();
            if (imageName!=null && !imageName.isEmpty()){
                newImagePath = uploadFileService.saveFile(request.getImage());
            }
        }
        if (newImagePath != null){
            String oldImagePath = product.getImagePath();
            product.setImagePath(newImagePath);
            uploadFileService.deleteImage(oldImagePath);
        }

        return product;
    }

}
