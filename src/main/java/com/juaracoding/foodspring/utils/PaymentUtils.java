package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 1:20 PM
@Last Modified 8/27/2023 1:20 PM
Version 1.0
*/

import com.juaracoding.foodspring.dto.CustomerDetails;
import com.juaracoding.foodspring.dto.MidtransItemDetails;
import com.juaracoding.foodspring.dto.TransactionDetailsMidtrans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentUtils {


    public static Map<String, Object> getPaymentRequestBody(TransactionDetailsMidtrans transactionDetailsMidtrans,
                                                      CustomerDetails customerDetails,
                                                      List<MidtransItemDetails> midtransItemDetails) {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Boolean> creditCard = new HashMap<>();
        Map<String, Object> expiry = new HashMap<>();
        expiry.put("expiry_duration", 5);
        expiry.put("unit", "minute");
        creditCard.put("secure", true);
        payload.put("transaction_details", transactionDetailsMidtrans);
        payload.put("credit_card", creditCard);
        payload.put("customer_details", customerDetails);
        payload.put("item_details", midtransItemDetails);
        payload.put("custom_expiry", expiry);

        return  payload;
    }
}
