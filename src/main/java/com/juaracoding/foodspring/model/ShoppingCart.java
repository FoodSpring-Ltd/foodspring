package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 11:37 AM
@Last Modified 8/14/2023 11:37 AM
Version 1.0
*/

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ShoppingCart")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShoppingCartId")
    private Long shoppingCartId;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @OneToMany
    @JoinColumn(name = "ShoppingCartId", referencedColumnName = "ShoppingCartId")
    private List<CartItem> cartItems;

    @Column(name = "CreatedAt", columnDefinition = "datetime2 default getdate()")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", columnDefinition = "datetime2 default getdate()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
