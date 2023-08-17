package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 12:39 PM
@Last Modified 8/14/2023 12:39 PM
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
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "OrderStatus")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderStatusId")
    private Long orderStatusId;

    @Column(name = "Status")
    @NotBlank(message = "Status can't be blank")
    @Length(message = "Status length can't be greater than 50", max = 50)
    private String status;

    @OneToMany
    @JoinColumn(name = "OrderStatusId", referencedColumnName = "OrderStatusId")
    private List<ShopOrder> shopOrders;

    @Column(name = "CreatedAt", columnDefinition = "datetime2 default getdate()")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", columnDefinition = "datetime2 default getdate()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
