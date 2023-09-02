package com.juaracoding.foodspring.repository;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/15/2023 9:22 PM
@Last Modified 8/15/2023 9:22 PM
Version 1.0
*/

import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.shopOrder.orderStatus = :status " +
            "AND CAST(oi.shopOrder.updatedAt AS java.sql.Date) = CURRENT_DATE")
    List<OrderItem> findAllOrderedItemToday(OrderStatus status);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.shopOrder.orderStatus = :status " +
            "AND EXTRACT(MONTH FROM oi.shopOrder.updatedAt) = EXTRACT(MONTH FROM :date)")
    List<OrderItem> findAllOrderedItemByOrderStatusAndMonth(OrderStatus status, Date date);
}
