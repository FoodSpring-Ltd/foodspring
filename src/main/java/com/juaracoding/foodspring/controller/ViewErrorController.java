package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/26/2023 10:28 PM
@Last Modified 8/26/2023 10:28 PM
Version 1.0
*/

import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.AppConfig;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.exceptions.EmailPublisherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.exceptions.TemplateInputException;

@ControllerAdvice
@Order(1)
public class ViewErrorController {

    private String[] strException = new String[2];

    @Autowired
    public ViewErrorController() {
        strException[0] = "ViewErrorController";
    }

    @ExceptionHandler({EmailPublisherException.class})
    public String errorSendEmail(Exception ex,
                                 RedirectAttributes redirectAttributes) {
        strException[1] = "errorSendEmail(Exception ex, RedirectAttributes redirectAttributes) -- LINE 32";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        return ServicePath.REDIRECT_HOME;
    }

    @ExceptionHandler({DataIntegrityViolationException.class, Exception.class})
    public String error500(Exception ex) {
        strException[1] = "error500() -- LINE 32";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return "error/error";
    }

    @ExceptionHandler(TemplateInputException.class)
    public String handleTemplateInputException(TemplateInputException ex) {
        strException[1] = "handleTemplateInputException(TemplateInputException ex) -- LINE 40";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return "error/404"; // Replace with your error view template
    }
}
