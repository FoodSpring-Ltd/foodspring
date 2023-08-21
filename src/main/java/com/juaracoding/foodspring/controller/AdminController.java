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

import com.juaracoding.foodspring.accessannotation.AdminAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.CategoryDTO;
import com.juaracoding.foodspring.dto.CategorySimpleResponse;
import com.juaracoding.foodspring.dto.ProductDTO;
import com.juaracoding.foodspring.dto.ProductSimpleResponse;
import com.juaracoding.foodspring.service.CategoryService;
import com.juaracoding.foodspring.service.DiscountService;
import com.juaracoding.foodspring.service.ProductService;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.utils.MappingAttribute;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = ServicePath.ADMIN)
@AdminAccess
public class AdminController {

    @Autowired
    private MappingAttribute mappingAttribute;


    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DiscountService discountService;

    private Map<String, Object> objectMapper = new HashMap<>();

    @GetMapping(value = "")
    public String adminHome(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_HOME;
    }

    @GetMapping(value = ServicePath.DASHBOARD_CATEGORY)
    public String categoryDashboard(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                    Model model,
                                    WebRequest request) {
        objectMapper = categoryService.getAllCategory(PageRequest.of(page - 1, limit));
        Integer totalPages = (Integer) objectMapper.get("totalPages");
        Long totalElements = (Long) objectMapper.get("totalElements");
        List<CategorySimpleResponse> data = (List<CategorySimpleResponse>) objectMapper.get("data");
        model.addAttribute("selectedRow", limit);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("currentPage", Math.min(page, totalPages));
        model.addAttribute("categories", data);
        model.addAttribute("category", new CategoryDTO());
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_CATEGORY_DASHBOARD;
    }

    @GetMapping(value = ServicePath.DASHBOARD_PRODUCT)
    public String productDashboard(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                    Model model,
                                   WebRequest request) {
        objectMapper = productService.getAllProduct(PageRequest.of(page - 1, limit));
        Integer totalPages = (Integer) objectMapper.get("totalPages");
        Long totalElements = (Long) objectMapper.get("totalElements");
        List<ProductSimpleResponse> data = (List< ProductSimpleResponse>) objectMapper.get("data");
        model.addAttribute("selectedRow", limit);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("currentPage", Math.min(page, totalPages));
        model.addAttribute("products", data);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_PRODUCT_DASHBOARD;
    }

    @GetMapping(value = ServicePath.DASHBOARD_PRODUCT_ADD_PRODUCT_FORM)
    public String addProductForm(Model model, WebRequest request) {
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("discounts", discountService.getAllDiscount());
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_ADD_PRODUCT_FORM;
    }

    @PostMapping(value = ServicePath.DASHBOARD_PRODUCT_ADD_PRODUCT_FORM)
    public String addProduct(@ModelAttribute("product") @Valid ProductDTO productDTO,
                             BindingResult bindingResult,
                             Model model,
                             WebRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestParam MultipartFile productImage
                             ) {
        mappingAttribute.setAttribute(model, request);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("discounts", discountService.getAllDiscount());
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDTO);
            return ViewPath.ADMIN_ADD_PRODUCT_FORM;
        }
        try {
            objectMapper = productService.createProduct(productDTO, productImage);
            if((Boolean) objectMapper.get("success")) {
                redirectAttributes.addFlashAttribute("message", ConstantMessage.SUCCESS_CREATED_PRODUCT);
                return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
            }
            redirectAttributes.addFlashAttribute("message", ConstantMessage.SUCCESS_CREATED_PRODUCT);
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
        } catch (Exception ex) {
            LoggingFile.exceptionString(new String[]{"AdminController", "addProduct"}, ex, "FC0001", "y");
        }
        model.addAttribute("product", productDTO);
        return ViewPath.ADMIN_ADD_PRODUCT_FORM;
    }

    @GetMapping(value = ServicePath.DASHBOARD_PRODUCT_UPDATE_PRODUCT_FORM_PRODUCT_ID)
    public String getUpdateProductForm(Model model,
                                       @PathVariable String productId,
                                       RedirectAttributes redirectAttributes,
                                       WebRequest request) {

        mappingAttribute.setAttribute(model, request);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("discounts", discountService.getAllDiscount());
        try {
            ProductDTO product = productService.getProductDTOById(productId);
            model.addAttribute("product", product);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
        }
        return ViewPath.ADMIN_UPDATE_PRODUCT_FORM;

    }

    @PostMapping(value = ServicePath.PRODUCT_UPDATE)
    public String updateProduct(@ModelAttribute(value = "product") ProductDTO productDTO,
                                BindingResult bindingResult,
                                @RequestParam MultipartFile productImage,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                WebRequest request) {
        mappingAttribute.setAttribute(model, request);
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("discounts", discountService.getAllDiscount());
        if (bindingResult.hasErrors()) {
            return ViewPath.ADMIN_UPDATE_PRODUCT_FORM;
        }
        objectMapper = productService.updateProduct(productDTO, productImage, request);
        if ((Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", ConstantMessage.PRODUCT_UPDATED);
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
        }
        redirectAttributes.addFlashAttribute("message", ConstantMessage.ERROR_PRODUCT_UPDATED);
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
    }

    @GetMapping(value = ServicePath.PRODUCT_DELETE)
    public String deleteProductById(Model model,
                                    RedirectAttributes redirectAttributes,
                                    @RequestParam String productId) {
        objectMapper = productService.softDeleteById(productId);
        redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
    }
    @PostMapping(value = ServicePath.CATEGORY)
    public String addCategory(@ModelAttribute("category") CategoryDTO categoryDTO,
                              BindingResult bindingResult,
                              Model model,
                              WebRequest request) {
        if (bindingResult.hasErrors()) {
            return ViewPath.ADMIN_CATEGORY_DASHBOARD;
        }

        objectMapper = categoryService.createCategory(categoryDTO, request);
        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            return ViewPath.REDIRECT_ADMIN_CATEGORY_DASHBOARD;
        }

        return ViewPath.ADMIN_CATEGORY_DASHBOARD;
    }

    @GetMapping(value = ServicePath.CATEGORY_DELETE)
    public String deleteCategoryById(Model model,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam Long categoryId) {
        objectMapper = categoryService.deleteCategoryById(categoryId);
        if ((Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully");
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_CATEGORY;
        }
        redirectAttributes.addFlashAttribute("message", "Can't delete selected category");
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_CATEGORY;
    }

    @PostMapping(value = ServicePath.CATEGORY_UPDATE)
    public String updateCategoryById(@ModelAttribute("category") @Valid CategoryDTO categoryDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_CATEGORY;
        }
        objectMapper = categoryService.updateCategoryNameById(categoryDTO);
        if ((Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", "Category updated successfully");
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_CATEGORY;
        }
        redirectAttributes.addFlashAttribute("message", "Can't update selected category");
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_CATEGORY;
    }
}
