package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/29/2023 1:42 PM
@Last Modified 8/29/2023 1:42 PM
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNotification implements Serializable {
    private Long adminId;
    private Long userId;
    private String orderId;
    private String status;
    public String targetURL;
    private String message;
}
