package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 9:24 PM
@Last Modified 8/27/2023 9:24 PM
Version 1.0
*/

import lombok.Data;

@Data
public class OrderItemResponse {


    private Long orderItemId;


    private String productName;


    private String productCategory;


    private String productImageURL;


    private String variant;


    private String discountName;


    private Float discountPercentage;


    private Double unitPrice;
    private String unitPriceIDR;
    private String totalPriceIDR;
    private Double totalPrice;
    private Integer qty;


    private String note;



}

