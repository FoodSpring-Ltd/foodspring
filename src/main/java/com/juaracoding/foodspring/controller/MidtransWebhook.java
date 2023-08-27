package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 1:26 PM
@Last Modified 8/27/2023 1:26 PM
Version 1.0
*/

import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.dto.MidtransNotif;
import com.juaracoding.foodspring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = ServicePath.MIDTRANS)
public class MidtransWebhook {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = ServicePath.NOTIFICATION)
    public ResponseEntity<Map<String, Object>> handleNotification(@RequestBody MidtransNotif notification, WebRequest request){
        orderService.handleMidtransStatusUpdate(notification, request);
        return ResponseEntity.ok(new HashMap<>());
    }
}
