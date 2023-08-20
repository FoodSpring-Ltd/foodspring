package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 12:28 PM
@Last Modified 8/14/2023 12:28 PM
Version 1.0
*/

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ShopOrder")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShopOrderId")
    private Long shopOrderId;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(name = "OrderDate")
    @NotNull(message = "Order date can't be null")
    private LocalDateTime orderDate;

    @Transient
    private Double grandTotal;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ShopOrderId", referencedColumnName = "ShopOrderId")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "OrderStatusId")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "PaymentTypeId")
    private PaymentType paymentType;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
