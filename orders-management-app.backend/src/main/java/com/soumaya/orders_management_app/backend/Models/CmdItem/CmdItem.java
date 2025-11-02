package com.soumaya.orders_management_app.backend.Models.CmdItem;

import com.soumaya.orders_management_app.backend.Models.order.Order;
import com.soumaya.orders_management_app.backend.Models.produit.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CmdItem {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;
    private boolean deleted;

    private boolean done;

    @Transient
    public float calculateTotal(String Option){
        if (Option.equals("ATELIER")){
            return this.quantity * product.getPriceAtelier();

        }else if(Option.equals("SHOP")){
            return this.quantity * product.getShopPrice();
        }else {
            return -1; //invalid option
        }

    }

    //auditing
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;



}
