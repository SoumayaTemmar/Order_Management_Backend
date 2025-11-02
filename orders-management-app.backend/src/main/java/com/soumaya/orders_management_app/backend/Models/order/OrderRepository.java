package com.soumaya.orders_management_app.backend.Models.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    Optional<Order> findByClientFullName(String name);

    Page<Order> findAllByDeletedFalseAndStateNot(OrderState state,Pageable pageable);
    Page<Order> findAllByDeletedTrue(Pageable pageable);

    @Query("""
            SELECT o from Order o
            WHERE o.state = TERMINEE
            """)
    Page<Order> findAllByStateTerminee(Pageable pageable);

    @Query("""
            SELECT o FROM Order o
            WHERE o.state = EN_ATTENTE
            """)
    Page<Order> findAllByStateEnAttente(Pageable pageable);

    @Query(value = """
            SELECT TO_CHAR(o.delivery_date, 'YYYY-MM') AS month,
                   SUM(o.total_price) AS totalSales
            FROM orders o
            WHERE o.state = 'LIVREE'
            GROUP BY TO_CHAR(o.delivery_date, 'YYYY-MM')
            ORDER BY month
            """, nativeQuery = true)
    List<MonthlySalesDto> getMonthlySales();

}
