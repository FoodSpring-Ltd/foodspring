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

import com.juaracoding.foodspring.accessannotation.BasicAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.ProductSimpleResponse;
import com.juaracoding.foodspring.service.ProductService;
import com.juaracoding.foodspring.utils.MappingAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private MappingAttribute mappingAttribute;

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/")
    public String home(@RequestParam(value = "sortBy", required = false) String sortBy, Model model, WebRequest request) {
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("productId", 1);
        model.addAttribute("products", (List< ProductSimpleResponse>) productService.getAllProduct(PageRequest.of(0, 10)).get("data"));
        mappingAttribute.setAttribute(model, request);
        return ViewPath.MAIN_VIEW_PRODUCTS;
    }

    @GetMapping(value = ServicePath.PRODUCT_DETAIL)
    @BasicAccess
    public String productDetail(@RequestParam(value = "id") Integer id, Model model, WebRequest request) {
        model.addAttribute("productId", id);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.MAIN_PRODUCT_DETAILS;
    }
}
