package com.juaracoding.foodspring.interceptor;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 11:00 AM
@Last Modified 8/20/2023 11:00 AM
Version 1.0
*/

import com.foodspring.annotation.NoLogin;
import com.juaracoding.foodspring.config.ServicePath;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component(value = "NoLoginInterceptorImpl")
public class NoLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("USR_ID");
        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.hasMethodAnnotation(NoLogin.class) || handlerMethod.getBeanType().isAnnotationPresent(NoLogin.class)) {
                if (userId != null) {
                    response.sendRedirect(ServicePath.HOME);
                    return false;
                }
            }
        }
        return true;
    }
}