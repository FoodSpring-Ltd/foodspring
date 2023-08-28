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
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrderItem")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemId")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "ShopOrderId")
    private ShopOrder shopOrder;


    @Column(name = "ProductName")
    private String productName;

    @Column(name = "ProductCategory")
    private String productCategory;

    @Column(name = "ProductImageURL")
    private String productImageURL;

    @Column(name = "Variant")
    private String variant;

    @Column(name = "DiscountName")
    private String discountName;

    @Column(name = "DiscountPercentage")
    private Integer discountPercentage;

    @Column(name = "UnitPrice")
    private Double unitPrice;

    @Column(name = "Qty")
    @Comment(value = "Product Item Quantity")
    private Integer qty;

    @Column(name = "Note")
    @Length(message = "Note can't be longer than 500", max = 500)
    private String note;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();


}
