package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 4:35 PM
@Last Modified 8/22/2023 4:35 PM
Version 1.0
*/

import com.juaracoding.foodspring.model.Variant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {

    private Long cartItemId;
    private Double unitPrice;
    private String unitPriceIDR;
    private Double totalPrice;
    private String totalPriceIDR;
    private Integer discountAmount;
    private String productName;
    private String note;
    private String productImg;
    private Integer qty;
    private String variantName;
    private List<Variant> productVariants;
}
