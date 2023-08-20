package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 12:44 PM
@Last Modified 8/14/2023 12:44 PM
Version 1.0
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MstPaymentType")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentTypeId")
    private Long paymentTypeId;

    @Column(name = "name")
    @NotBlank(message = "Payment type name can't be blank")
    private String name;

    @OneToMany
    @JoinColumn(name = "PaymentTypeId", referencedColumnName = "PaymentTypeId")
    private List<ShopOrder> shopOrders;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();
}
