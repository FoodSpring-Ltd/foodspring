package com.juaracoding.foodspring.interceptor;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/29/2023 12:36 PM
@Last Modified 8/29/2023 12:36 PM
Version 1.0
*/

import com.juaracoding.foodspring.config.ServicePath;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpHeaders;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class BasicHandshakeInterceptor implements HandshakeInterceptor {
    private final Logger logger = LoggerFactory.getLogger(BasicHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                                   @NotNull Map attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession session = servletRequest.getServletRequest().getSession();
            if (session.getAttribute("USR_ID") == null) {
                logger.info("Not logged in");
                response.setStatusCode(HttpStatus.SEE_OTHER);
                response.getHeaders().set(HttpHeaders.LOCATION, ServicePath.AUTH_LOGOUT);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            }
            logger.info((String) session.getAttribute("USR_ID"));
        }
        return true;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                               Exception ex) {
    }
}