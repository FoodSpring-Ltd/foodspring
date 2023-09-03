package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/3/2023 7:06 AM
@Last Modified 9/3/2023 7:06 AM
Version 1.0
*/

import lombok.Data;

@Data
public class InvoiceItem {
    private String productName;
    private String variant;
    private Integer discount;
    private Integer qty;
    private String unitPriceIDR;
    private String totalPriceIDR;
    private Double totalPrice;
}
