package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 2:39 PM
@Last Modified 8/22/2023 2:39 PM
Version 1.0
*/

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtils {

    public static String getCurrentUrl() {
        HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuffer requestUrl = currentRequest.getRequestURL();
        String queryString = currentRequest.getQueryString();

        if (queryString != null) {
            return requestUrl.append('?').append(queryString).toString();
        } else {
            return requestUrl.toString();
        }
    }

    public static String getCurrentPath() {
        HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return currentRequest.getRequestURI(); // This will give you the path without the host
    }


}
