package com.juaracoding.foodspring.repository;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/15/2023 9:25 PM
@Last Modified 8/15/2023 9:25 PM
Version 1.0
*/

import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.model.ShopOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, String> {

    Page<ShopOrder> findAllByUserUserIdAndOrderStatus(Long userId, OrderStatus orderStatus, Pageable pageable);
    Page<ShopOrder> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT COUNT(0) FROM ShopOrder so " +
            "WHERE CAST(so.createdAt AS java.sql.Date) = CURRENT_DATE " +
            "AND so.orderStatus = 'PAID'")
    Integer countTodayActiveOrder();

    @Query("SELECT COUNT(0) FROM ShopOrder so " +
            "WHERE CAST(so.createdAt AS java.sql.Date) = CURRENT_DATE " +
            "AND so.orderStatus <> 'UNPAID' AND so.orderStatus <> 'CANCELED'")
    Integer countTodayTotalOrder();

    @Query("SELECT COUNT(0) FROM ShopOrder so WHERE so.orderStatus = :orderStatus " +
            "AND CAST(so.createdAt AS java.sql.Date) = CURRENT_DATE")
    Integer countTodayOrderByOrderStatus(OrderStatus orderStatus);
}
