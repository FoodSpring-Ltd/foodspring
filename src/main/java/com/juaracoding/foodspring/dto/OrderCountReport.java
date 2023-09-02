package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/2/2023 2:26 PM
@Last Modified 9/2/2023 2:26 PM
Version 1.0
*/

import lombok.Data;

@Data
public class OrderCountReport {

    private int completedOrder;
    private int activeOrder;
    private int inProcessOrder;
    private int totalOrder;
}
