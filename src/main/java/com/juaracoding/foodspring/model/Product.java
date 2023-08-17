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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductId")
    private Long productId;

    @Column(name = "ProductName")
    @NotBlank(message = "Product Name can't be blank")
    @Length(message = "Product Name can't be longer than 200", max = 200)
    private String productName;

    @Column(name = "Description")
    @Length(message = "Description max 1000", max = 1000)
    private String description;

    @Column(name = "ImageURL")
    @NotBlank(message = "Image URL can't be blank")
    private String imageURL;

    @Column(name = "Price")
    @NotNull(message = "Price can't be null")
    private Double price;

    @OneToMany
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    private List<Variant> variants;

    @OneToMany
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    private List<CartItem> cartItems;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "CategoryId")
    private Category category;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "DiscountId")
    private Discount discount;

    @Column(name = "CreatedAt", columnDefinition = "datetime2 default getdate()")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", columnDefinition = "datetime2 default getdate()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "ModifiedBy")
    private Long modifiedBy;

    @Column(name = "IsDelete", columnDefinition = "smallint default 0")
    @NotNull(message = "Flag isDelete can't be null")
    private Boolean isDelete;

    @Column(name = "IsAvailable", columnDefinition = "smallint default 1")
    @NotNull(message = "Flag isAvailable can't be null")
    private Boolean isAvailable;

}
