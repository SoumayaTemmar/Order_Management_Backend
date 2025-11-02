package com.soumaya.orders_management_app.backend.Models.produit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    boolean existsByName(String name);

    Optional<Product> findByName(String Name);
    Page<Product> findAllByDeletedFalse(Pageable pageable);
    Page<Product> findAllByDeletedTrue(Pageable pageable);
}
