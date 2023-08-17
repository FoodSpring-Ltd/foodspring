package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 12:27 PM
@Last Modified 8/14/2023 12:27 PM
Version 1.0
*/

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrderItem")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemId")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "ShopOrderId")
    private ShopOrder shopOrder;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Variant variant;

    @Column(name = "Qty")
    @Comment(value = "Product Item Quantity")
    private Integer qty;

    @Column(name = "CreatedAt", columnDefinition = "datetime2 default getdate()")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", columnDefinition = "datetime2 default getdate()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
