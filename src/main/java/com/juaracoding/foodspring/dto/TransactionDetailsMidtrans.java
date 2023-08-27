package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 12:59 PM
@Last Modified 8/27/2023 12:59 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionDetailsMidtrans implements Serializable {

    @JsonProperty("order_id")
    private String order_id;

    @JsonProperty("gross_amount")
    private Integer gross_amount;
}
