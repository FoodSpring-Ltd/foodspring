package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/13/2023 11:45 PM
@Last Modified 8/13/2023 11:45 PM
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

@Entity
@Table(name = "Variant")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VariantId")
    private Long variantId;

    @Column(name = "Name")
    @NotBlank(message = "Name can't be blank")
    @Length(message = "Variant Name length can't be exceeded 255", max = 255)
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ProductId")
    private Product product;

    @Column(name = "CreatedAt", columnDefinition = "datetime2 default getdate()")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", columnDefinition = "datetime2 default getdate()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
