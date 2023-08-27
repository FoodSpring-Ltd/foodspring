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

import com.juaracoding.foodspring.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "ShopOrder")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ShopOrderId")
    private String shopOrderId;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(name = "OrderDate")
    @NotNull(message = "Order date can't be null")
    private LocalDateTime orderDate;

    @Transient
    private Double grandTotal;

    @Transient
    private String grandTotalIDR;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ShopOrderId", referencedColumnName = "ShopOrderId")
    private List<OrderItem> orderItems;

    @Column(name = "OrderStatus")
    @Enumerated(STRING)
    private OrderStatus orderStatus = OrderStatus.UNPAID;

    @Column(name = "PaymentType")
    private String paymentType;

    @Column(name = "MidtransTransactionId")
    private String midtransTransactionId;

    @Column(name = "SnapToken")
    private String snapToken;

    @Column(name = "ModifiedBy")
    private Long modifiedBy;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
