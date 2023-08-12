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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("")
public class MainController {

    @GetMapping(value = "/")
    public String home(@RequestParam(value = "sortBy", required = false) String sortBy, Model model) {
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("productId", 1);
        return "main/view-products";
    }

    @GetMapping(value = "/product/detail")
    public String productDetail(@RequestParam(value = "id") Integer id, Model model) {
        model.addAttribute("productId", id);
        return "main/product-details";
    }
}
