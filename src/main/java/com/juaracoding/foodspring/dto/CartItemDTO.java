package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 9:28 AM
@Last Modified 8/22/2023 9:28 AM
Version 1.0
*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    @NotBlank(message = "Product ID can't be black")
    private String productId;

    private Long variantId;

    @Min(message = "Quantity should be greater than 0", value = 0)
    private Integer qty;

    @Length(message = "Note can't be longer than 500", max = 500)
    private String note;
}
