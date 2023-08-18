package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/10/2023 4:11 PM
@Last Modified 8/10/2023 4:11 PM
Version 1.0
*/

import com.juaracoding.foodspring.config.AppConfig;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.utils.LoggingFile;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("")
public class MainController {

//    @RequestMapping(value = "/error", method = RequestMethod.GET)
//    public String handlerError(Exception e) {
//        LoggingFile.exceptionString(new String[]{"MainController", e.getMessage()}, e, AppConfig.getFlagLogging());
//        return "error/error";
//    }

    @GetMapping(value = "/")
    public String home(@RequestParam(value = "sortBy", required = false) String sortBy, Model model) {
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("productId", 1);
        return ViewPath.MAIN_VIEW_PRODUCTS;
    }

    @GetMapping(value = ServicePath.PRODUCT_DETAIL)
    public String productDetail(@RequestParam(value = "id") Integer id, Model model) {
        model.addAttribute("productId", id);
        return ViewPath.MAIN_PRODUCT_DETAILS;
    }
}
