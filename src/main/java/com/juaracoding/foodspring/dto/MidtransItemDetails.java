package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 12:37 PM
@Last Modified 8/27/2023 12:37 PM
Version 1.0
*/

import lombok.Data;

@Data
public class MidtransItemDetails {

    private String Id;
    private Integer price;
    private Integer quantity;
    private String category;
    private String name;
    private String brand;
}
