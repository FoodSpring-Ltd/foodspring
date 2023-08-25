package com.juaracoding.foodspring.publisher;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/25/2023 8:28 AM
@Last Modified 8/25/2023 8:28 AM
Version 1.0
*/

import com.foodspring.model.EmailVerification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MailPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailMessage(EmailVerification emailVerification) {
        rabbitTemplate.convertAndSend("email-verification-queue", emailVerification);
    }
}