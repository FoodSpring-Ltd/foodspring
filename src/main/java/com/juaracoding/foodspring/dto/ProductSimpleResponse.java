package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/21/2023 1:48 AM
@Last Modified 8/21/2023 1:48 AM
Version 1.0
*/

import com.juaracoding.foodspring.model.Category;
import com.juaracoding.foodspring.model.Discount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSimpleResponse {
    private String productId;
    private String productName;
    private Double price;
    private Category category;
    private Boolean isAvailable;
    private String description;
    private String imageURL;
    private String variants;
    private Discount discount;
    private String priceIDR;
    private String discountedPriceIDR;
}
