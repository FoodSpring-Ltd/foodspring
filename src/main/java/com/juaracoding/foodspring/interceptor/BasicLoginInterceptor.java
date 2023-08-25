package com.juaracoding.foodspring.interceptor;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/19/2023 11:32 AM
@Last Modified 8/19/2023 11:32 AM
Version 1.0
*/

import com.foodspring.annotation.BasicAccess;
import com.juaracoding.foodspring.config.ServicePath;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component(value = "BasicLoginInterceptorImpl")
public class BasicLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
       Long usrId = (Long) session.getAttribute("USR_ID");
        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.hasMethodAnnotation(BasicAccess.class) || handlerMethod.getBeanType().isAnnotationPresent(BasicAccess.class)) {
                if (usrId == null) {
                    if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
                    } else {
                        response.sendRedirect(ServicePath.AUTH_LOGOUT);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}