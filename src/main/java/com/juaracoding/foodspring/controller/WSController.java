package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/29/2023 10:44 AM
@Last Modified 8/29/2023 10:44 AM
Version 1.0
*/

import com.juaracoding.foodspring.dto.OrderNotification;
import com.juaracoding.foodspring.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WSController {
    private final Logger logger = LoggerFactory.getLogger(WSController.class);

    @Autowired
    private InvoiceService invoiceService;

    private Map<String, Object> objectMapper = new HashMap<>();

    @MessageMapping("/new-order")
    @SendTo("/topic/new-order")
    public OrderNotification getMessage(OrderNotification orderNotification) {
        return orderNotification;
    }

    @GetMapping("/test/ws")
    public String testSocket() {
        return "index";
    }


}
