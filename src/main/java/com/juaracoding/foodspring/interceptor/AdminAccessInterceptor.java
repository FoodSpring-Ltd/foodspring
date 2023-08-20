package com.juaracoding.foodspring.interceptor;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/19/2023 11:18 AM
@Last Modified 8/19/2023 11:18 AM
Version 1.0
*/

import com.juaracoding.foodspring.accessannotation.AdminAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.utils.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;


@Component(value = "AdminAccessInterceptorImpl")
public class AdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.hasMethodAnnotation(AdminAccess.class) || handlerMethod.getBeanType().isAnnotationPresent(AdminAccess.class)) {
                // Check if the user has admin role in the session
                boolean isAdmin = !Objects.isNull(session.getAttribute("IS_ADMIN")) && (Boolean) session.getAttribute("IS_ADMIN");
                if (session.getAttribute("USR_ID") == null || !isAdmin) {
                    try {
                        if (session.getAttribute("USR_ID") == null) {
                            response.sendRedirect(ServicePath.AUTH_LOGOUT);
                        } else {
                            response.sendRedirect(ServicePath.HOME);
                        }
                    } catch (Exception e) {
                        LoggingFile.exceptionString(new String[]{"AdminAccessInterceptor", "Interceptor error"}, e, "y");
                    }
                    return false;
                }
            }
        }
        return true;
    }
}

