package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/14/2023 6:50 AM
@Last Modified 8/14/2023 6:50 AM
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
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MstDiscount")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiscountId")
    private Long discountId;

    @Column(name = "DiscountName")
    @Length(message = "Discount name length can't be more than 150", max = 150)
    private String discountName;

    @Column(name = "PercentDiscount")
    @NotNull(message = "Percent Discount can't be null")
    @Comment(value = "Discount in percent")
    private Float percentDiscount;

    @OneToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "DiscountId", referencedColumnName = "DiscountId")
    private List<Product> products;

    @Column(name = "StartAt")
    @Comment(value = "Discount start time")
    private LocalDateTime startAt;

    @Column(name = "EndAt")
    @Comment(value = "Discount end time")
    private LocalDateTime endAt;

    @Column(name = "IsDelete")
    private Boolean isDelete = false;

    @Column(name = "ModifiedBy")
    private Long modifiedBy;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();
}
