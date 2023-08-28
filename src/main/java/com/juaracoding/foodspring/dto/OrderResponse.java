package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 9:22 PM
@Last Modified 8/27/2023 9:22 PM
Version 1.0
*/

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String shopOrderId;
    private List<OrderItemResponse> orderItems;
    private Double grandTotal;
    private String grandTotalIDR;
    private Boolean isPaid;
    private LocalDateTime createdAt;
    private String snapToken;
    private String adminUsername;
    private LocalDateTime updatedAt;
}