package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 4:44 PM
@Last Modified 8/20/2023 4:44 PM
Version 1.0
*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private String productId;

    @NotBlank(message = "Product Name can't be blank")
    @Length(message = "Product name can't be longer than 200", max = 200)
    private String productName;


    @Length(message = "Description max 1000 character", max = 1000)
    private String description;


    @NotNull(message = "Price can't be null")
    @Min(value = 0, message = "Price can't be negative")
    private Double price;



    private Long categoryId;

    private String variants;


    private Long discountId;

    private Boolean isDelete = false;
    private Boolean isAvailable = true;

}
