package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/13/2023 11:51 PM
@Last Modified 8/13/2023 11:51 PM
Version 1.0
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MstProduct")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ProductId")
    private String productId;

    @Column(name = "ProductName")
    @NotBlank(message = "Product Name can't be blank")
    @Length(message = "Product Name can't be longer than 200", max = 200)
    private String productName;

    @Column(name = "Description")
    @Length(message = "Description max 1000", max = 1000)
    private String description;

    @Column(name = "ImageURL")
    private String imageURL = "https://placehold.co/600x400?text=Product+Image&font=roboto";

    @Column(name = "Price")
    @NotNull(message = "Price can't be null")
    private Double price;

    @OneToMany
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    private List<Variant> variants;

    @OneToMany
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CartItem> cartItems;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "CategoryId")
    private Category category;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "DiscountId")
    private Discount discount;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "ModifiedBy")
    private Long modifiedBy;

    @Column(name = "IsDelete")
    private Boolean isDelete = false;

    @Column(name = "IsAvailable")
    private Boolean isAvailable = true;

    @Transient
    private String priceIDR;

    @Transient
    private String discountedPriceIDR;

}
