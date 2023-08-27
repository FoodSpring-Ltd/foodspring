package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 8:46 PM
@Last Modified 8/27/2023 8:46 PM
Version 1.0
*/

import com.juaracoding.foodspring.enums.OrderStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatusDTO implements Serializable {

    private OrderStatus orderStatus;
    private String orderId;
}
