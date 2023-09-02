package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/29/2023 1:53 PM
@Last Modified 8/29/2023 1:53 PM
Version 1.0
*/

import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.dto.OrderNotification;
import com.juaracoding.foodspring.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ApiController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/notify")
    public String notifyAdmin() {
        UUID uuid = UUID.randomUUID();
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(uuid.toString());
        notification.setMessage("");
        notification.setStatus(OrderStatus.PAID.toString());
        notification.setTargetURL(ServicePath.ADMIN_ORDER_MANAGEMENT.concat(OrderStatus.PAID.toString()));
        messagingTemplate.convertAndSend("/topic/new-order", notification);
        return "OK";
    }
}
