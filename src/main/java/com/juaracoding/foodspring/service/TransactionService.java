package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 12:55 PM
@Last Modified 8/22/2023 12:55 PM
Version 1.0
*/

import com.juaracoding.foodspring.handler.ResponseHandler;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private MidtransSnapApi midtransSnapApi;

   public Map<String, Object> createOrder(WebRequest request) throws MidtransError {
       UUID idRand = UUID.randomUUID();
       Map<String, Object> params = new HashMap<>();


       String token = midtransSnapApi.createTransactionToken(params);
       return new ResponseHandler().generateModelAttribut("Token Created", HttpStatus.OK, token, null, request);
   }


}
