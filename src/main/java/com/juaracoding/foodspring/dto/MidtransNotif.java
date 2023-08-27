package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 2:34 PM
@Last Modified 8/27/2023 2:34 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MidtransNotif implements Serializable {

    @JsonProperty("transaction_status")
    private String transactionStatus;

    @JsonProperty("transaction_id")
    private String transactionId;

    private String acquirer;

    @JsonProperty("gross_amount")
    private Double grossAmount;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("payment_type")
    private String paymentType;
}
