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

import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.ProductSimpleResponse;
import com.juaracoding.foodspring.model.Product;
import com.juaracoding.foodspring.service.CategoryService;
import com.juaracoding.foodspring.service.ProductService;
import com.juaracoding.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.utils.MappingAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private MappingAttribute mappingAttribute;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private String[] strExceptions = new String[2];

    public MainController() {
        strExceptions[0] = "MainController";
    }

    private Map<String, Object> objectMapper = new HashMap<>();

    @GetMapping(value = "/home")
    public String home(@RequestParam(value = "sortBy", required = false) String sortBy,
                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                       @RequestParam(required = false) List<Long> selectedCategoryIds,
                       Model model,
                       WebRequest request) {
        if (Objects.isNull(selectedCategoryIds)) {
            selectedCategoryIds = new ArrayList<>();
        }
        objectMapper = productService.getAllProduct(PageRequest.of(page - 1, limit), selectedCategoryIds);
        Integer totalPages = (Integer) objectMapper.get("totalPages");
        Long totalElements = (Long) objectMapper.get("totalElements");
        List<ProductSimpleResponse> data = (List< ProductSimpleResponse>) objectMapper.get("data");
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("selectedCategoryIds", selectedCategoryIds);
        model.addAttribute("selectedRow", limit);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("currentPage", Math.min(page, totalPages));
        model.addAttribute("products", data);
        model.addAttribute("selectedSort", sortBy);
        model.addAttribute("productId", 1);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.MAIN_VIEW_PRODUCTS;
    }

    @GetMapping(value = "/")
    public String redirect(){
        return ServicePath.REDIRECT_HOME;
    }

    @GetMapping(value = ServicePath.PRODUCT_DETAIL_PRODUCT_ID)
    public String productDetail(@PathVariable String productId,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                WebRequest request) {
        mappingAttribute.setAttribute(model, request);
        try {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
            model.addAttribute("variants", product.getVariants());
        } catch (Exception ex) {
            strExceptions[1] = "productDetail(@RequestParam String id, Model model, WebRequest request) --LINE 91";
            LoggingFile.exceptionString(strExceptions, ex, "y");
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return ServicePath.REDIRECT_HOME;
        }
        return ViewPath.MAIN_PRODUCT_DETAILS;
    }
}
