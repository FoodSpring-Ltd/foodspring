package com.juaracoding.foodspring.config;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/29/2023 10:33 AM
@Last Modified 8/29/2023 10:33 AM
Version 1.0
*/

import com.juaracoding.foodspring.handler.UserHandshakeHandler;
import com.juaracoding.foodspring.interceptor.AdminHandshakeInterceptor;
import com.juaracoding.foodspring.interceptor.BasicHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserHandshakeHandler userHandshakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/new-order")
               .addInterceptors(new AdminHandshakeInterceptor())
               .setHandshakeHandler(userHandshakeHandler)
               .withSockJS();
       registry.addEndpoint("/notify-user")
               .addInterceptors(new BasicHandshakeInterceptor())
               .setHandshakeHandler(userHandshakeHandler)
               .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/ws");
    }
}
