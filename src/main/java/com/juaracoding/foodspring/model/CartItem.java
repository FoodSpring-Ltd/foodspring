package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 11:40 AM
@Last Modified 8/14/2023 11:40 AM
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
@Table(name = "CartItem")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartItemId")
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "ShoppingCartId")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "VariantId")
    private Variant variant;

    @Column(name = "VariantName")
    private String variantName;

    @Column(name = "Price")
    private Double price;

    @Column(name = "PercentDiscount")
    private Float percentDiscount;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "Qty")
    @Comment(value = "Product Item Quantity")
    private Integer qty;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
