package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 4:49 PM
@Last Modified 8/22/2023 4:49 PM
Version 1.0
*/

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> cartItems;
    private Double grandTotal;
    private String grandTotalIDR;
}
