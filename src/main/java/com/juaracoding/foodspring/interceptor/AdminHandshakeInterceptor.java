package com.juaracoding.foodspring.interceptor;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/29/2023 11:55 AM
@Last Modified 8/29/2023 11:55 AM
Version 1.0
*/

import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Objects;

@Component
public class AdminHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                                   @NotNull Map attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession session = servletRequest.getServletRequest().getSession();
            boolean isAdmin = !Objects.isNull(session.getAttribute("IS_ADMIN")) && (Boolean) session.getAttribute("IS_ADMIN");
            return session.getAttribute("USR_ID") != null && isAdmin;
        }
        return true;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                               Exception ex) {
    }
}