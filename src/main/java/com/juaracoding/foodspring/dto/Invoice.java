package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/3/2023 7:08 AM
@Last Modified 9/3/2023 7:08 AM
Version 1.0
*/

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Invoice {
    private String grandTotalIDR;
    private List<InvoiceItem> invoiceItems;
    private String subTotalIDR;
    private LocalDateTime datetime;
    private String customerName;
    private String customerEmail;
}
