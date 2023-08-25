package com.juaracoding.foodspring.handler;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/13/2023 3:07 PM
@Last Modified 8/13/2023 3:07 PM
Version 1.0
*/

import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.AppConfig;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

@ControllerAdvice
@Order(1)
public class ControllerAdvisor {
    private String[] strException = new String[2];

    public ControllerAdvisor() {
        strException[0] = "ControllerAdvisor";
    }
    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String Error404() {
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String Error500(Exception ex) {
        strException[1] = "error500() -- LINE 35";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return "error/error";
    }

    @ExceptionHandler(TemplateInputException.class)
    public String handleTemplateInputException(TemplateInputException ex) {
        // Handle the exception and return an appropriate view or error message
        return "error/404"; // Replace with your error view template
    }
}
