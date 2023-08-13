package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/10/2023 8:17 AM
@Last Modified 8/10/2023 8:17 AM
Version 1.0
*/

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @GetMapping(value = "")
    public String adminHome() {
        return "admin/adminHome";
    }

    @GetMapping(value = "/dashboard/category")
    public String categoryDashboard(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "limit", required = false) Integer limit,
                                    Model model) {
        model.addAttribute("selectedRow", limit);
        model.addAttribute("totalPages", 10);
        model.addAttribute("currentPage", page == null ? 1 : page);
        return "admin/category-dashboard";
    }

    @GetMapping(value = "/dashboard/product")
    public String productDashboard(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "limit", required = false) Integer limit,
                                    Model model) {
        model.addAttribute("selectedRow", limit);
        model.addAttribute("totalPages", 10);
        model.addAttribute("currentPage", page == null ? 1 : page);
        return "admin/product-dashboard";
    }

    @GetMapping(value = "/dashboard/product/add")
    public String addProductForm() {
        return "admin/add-product";
    }

}
