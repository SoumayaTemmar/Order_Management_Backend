package com.soumaya.orders_management_app.backend.AppControllers.product;

import com.soumaya.orders_management_app.backend.ExceptionHandling.OperationNotPermittedException;
import com.soumaya.orders_management_app.backend.Models.produit.Product;
import com.soumaya.orders_management_app.backend.Models.produit.ProductRepository;
import com.soumaya.orders_management_app.backend.common.PageResponse;
import com.soumaya.orders_management_app.backend.common.StandardResponse;
import com.soumaya.orders_management_app.backend.file.UploadFileService;
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
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UploadFileService uploadFileService;

    //get all products
    public PageResponse<ProductResponse> getAllProducts(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<Product> products = productRepository.findAllByDeletedFalse(pageable);

        List<ProductResponse> productResponses = products.stream()
                .map(productMapper::toProductResponse)
                .toList();

        return new PageResponse<>(
                productResponses,
                products.getNumber(),
                products.getSize(),
                products.getTotalPages(),
                products.getTotalElements()
        );
    }
    //get all deleted products
    public PageResponse<ProductResponse> getDeletedProducts(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<Product> products = productRepository.findAllByDeletedTrue(pageable);

        List<ProductResponse> productResponses = products.stream()
                .map(productMapper::toProductResponse)
                .toList();

        return new PageResponse<>(
                productResponses,
                products.getNumber(),
                products.getSize(),
                products.getTotalPages(),
                products.getTotalElements()
        );
    }
    //get product by id
    public ProductResponse getProductById(int id){

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "produit: "+id+ " n'existe pas"
                ));

        return productMapper.toProductResponse(product);
    }
    //add product
    @Transactional
    public StandardResponse addProduct(ProductRequest request){

        if (productRepository.existsByName(request.getName())){
            throw new OperationNotPermittedException(
                    "Produit: "+request.getName()+ " existe déjà" +
                    " ou pas supprimé définitivement, veuillez choisir un autre nom"
            );
        }

        Product product = productMapper.toProduct(request);

        return StandardResponse.builder()
                .id(productRepository.save(product).getId())
                .message("produit enregistré !")
                .build();
    }
    // soft delete
    @Transactional
    public StandardResponse softDeleteProduct(int id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "produit avec id: " + id + " n'existe pas"
                ));

        if (product.isDeleted()){
            throw new IllegalStateException("produit déjà supprime !");
        }

        product.setDeleted(true);
        product.getCmdItems().forEach(item -> item.setDeleted(true));
        productRepository.save(product);

        return StandardResponse.builder()
                .id(id)
                .message("produit supprimé !")
                .build();
    }
    // restore product
    @Transactional
    public StandardResponse restoreProduct(int id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "produit avec id: " + id + " n'existe pas"
                ));

        if (!product.isDeleted()){
            throw new IllegalStateException("produit déjà restoré !");
        }

        product.setDeleted(false);
        product.getCmdItems().forEach(item -> item.setDeleted(false));
        productRepository.save(product);

        return StandardResponse.builder()
                .id(id)
                .message("produit restoré !")
                .build();
    }
    // permanently delete product
    @Transactional
    public StandardResponse DeleteProduct(int id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "produit avec id: " + id + " n'existe pas"
                ));

        uploadFileService.deleteImage(product.getImagePath());
        productRepository.delete(product);

        return StandardResponse.builder()
                .id(id)
                .message("produit supprimé définitivement !")
                .build();
    }

    //update product
    public StandardResponse updateProduct(int id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "produit: "+id+" n'existe pas"
                ));

        if (product.isDeleted()){
            throw new IllegalStateException("vous ne pouvez pas modifier un produit supprmé");
        }

        productRepository.save(productMapper.updateProduct(request, product));
        return StandardResponse.builder()
                .id(id)
                .message("produit modifié !")
                .build();
    }
}

