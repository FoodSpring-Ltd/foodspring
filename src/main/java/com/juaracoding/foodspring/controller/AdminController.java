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

import com.foodspring.annotation.AdminAccess;
import com.foodspring.utils.CurrencyFormatter;
import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.*;
import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.service.*;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.MappingAttribute;
import com.juaracoding.foodspring.utils.PageProperty;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SalesReportService salesReportService;
    @Autowired
    private AdminOrderService adminOrderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DiscountService discountService;

    private Map<String, Object> objectMapper = new HashMap<>();

    @GetMapping(value = "")
    public String adminHome(Model model, WebRequest request) {
        mappingAttribute.setAttribute(model, request);
        objectMapper = salesReportService.getTodayOrderCountReport(request);
        OrderCountReport report = null;
        if (!(Boolean) objectMapper.get("success")) {
            model.addAttribute("message", objectMapper.get("message"));
        }else {
            report = (OrderCountReport) objectMapper.get("data");
        }
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("totalIncome", CurrencyFormatter.toRupiah(salesReportService.getTodayTotalIncome()));
        model.addAttribute("currentMonthIncome", CurrencyFormatter.toRupiah(salesReportService.getCurrentMonthTotalIncome()));
        model.addAttribute("orderReport", report);
        return ViewPath.ADMIN_HOME;
    }

    @GetMapping(value = ServicePath.DASHBOARD_CATEGORY)
    public String categoryDashboard(PageProperty pageProperty,
                                    Model model,
                                    WebRequest request) {
        objectMapper = categoryService.getAllCategory(pageProperty.getPageable(), request);
        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<CategorySimpleResponse> data = (List<CategorySimpleResponse>) objectMapper.get("content");
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage", ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("categories", data);
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_CATEGORY_DASHBOARD;
    }

    @GetMapping(value = ServicePath.DASHBOARD_PRODUCT)
    public String productDashboard(PageProperty pageProperty,
                                   Model model,
                                   WebRequest request) {

        objectMapper = productService.getAllProduct(pageProperty.getPageable(), request);
        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<ProductSimpleResponse> products = (List<ProductSimpleResponse>) objectMapper.get("content");
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage", ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("products", products);
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_PRODUCT_DASHBOARD;
    }

    @GetMapping(value = ServicePath.DASHBOARD_PRODUCT_ADD_PRODUCT_FORM)
    public String addProductForm(Model model, WebRequest request) {
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("discounts", discountService.getAllDiscount());
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
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
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("discounts", discountService.getAllDiscount());
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDTO);
            return ViewPath.ADMIN_ADD_PRODUCT_FORM;
        }
        try {
            objectMapper = productService.createProduct(productDTO, productImage);
            if ((Boolean) objectMapper.get("success")) {
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
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
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
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
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
                                    @RequestParam String productId,
                                    WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        objectMapper = productService.softDeleteById(productId, request);
        redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_PRODUCT;
    }

    @PostMapping(value = ServicePath.CATEGORY)
    public String addCategory(@ModelAttribute("category") CategoryDTO categoryDTO,
                              BindingResult bindingResult,
                              Model model,
                              WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
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
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
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
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
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


    @GetMapping(value = ServicePath.DASHBOARD_DISCOUNT)
    public String discountDashboard(PageProperty pageProperty,
                                    Model model,
                                    WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        objectMapper = discountService.getAllDiscount(pageProperty.getPageable(), request);
        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<DiscountDTO> data = (List<DiscountDTO>) objectMapper.get("content");
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage", ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("discounts", data);
        model.addAttribute("discount", new DiscountDTO());
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_DISCOUNT_DASHBOARD;
    }

    @PostMapping(value = ServicePath.DISCOUNT)
    public String addDiscount(@ModelAttribute("discount") DiscountDTO discountDTO,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        if (bindingResult.hasErrors()) {
            return ViewPath.ADMIN_DISCOUNT_DASHBOARD;
        }
        objectMapper = discountService.createDiscount(discountDTO, request);
        redirectAttributes.addFlashAttribute("message",
                objectMapper.get("message").toString());
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_DISCOUNT;
    }

    @PostMapping(value = ServicePath.DISCOUNT_UPDATE)
    public String updateDiscountById(@ModelAttribute("discount") @Valid DiscountDTO discountDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("messages", ConstantMessage.ERROR_UPDATE_DISCOUNT);
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_DISCOUNT;
        }
        objectMapper = discountService.updateDiscount(discountDTO, request);
        if ((Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_DISCOUNT;
        }
        redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_DISCOUNT;
    }

    @GetMapping(value = ServicePath.DISCOUNT_DELETE)
    public String deleteCategoryById(Model model,
                                     RedirectAttributes redirectAttributes,
                                     WebRequest request,
                                     @RequestParam Long discountId) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        objectMapper = discountService.softDeleteDiscountById(discountId, request);
        if ((Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", "Discount deleted successfully");
            return ServicePath.REDIRECT_ADMIN_DASHBOARD_DISCOUNT;
        }
        redirectAttributes.addFlashAttribute("message", "Can't delete selected discount");
        return ServicePath.REDIRECT_ADMIN_DASHBOARD_DISCOUNT;
    }

    @GetMapping(value = ServicePath.ORDER_MANAGEMENT)
    public String ordersManagement(PageProperty pageProperty,
                             @RequestParam OrderStatus status,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             WebRequest request) {
        pageProperty.setDefaultSortBy("createdAt");
        objectMapper = adminOrderService.getAllOrderByStatus(pageProperty.getPageable(), status, request);
        if (!(Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", objectMapper.get("message"));
            return ServicePath.REDIRECT_ADMIN;
        }
        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<OrderResponse> data = (List<OrderResponse>) objectMapper.get("content");
        model.addAttribute("button", adminOrderService.getOrderUpdateStatusPath(status));
        model.addAttribute("status", status.toString());
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage", ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("shopOrders", data);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ADMIN_ORDERS_PAGE;
    }

    @PostMapping(value = ServicePath.UPDATE_ORDER_STATUS)
    public String updateOrderStatus(OrderStatusDTO orderStatusDTO,
                                    WebRequest request) {
        objectMapper = adminOrderService.updateOrderStatus(orderStatusDTO, request);
        return ServicePath.REDIRECT_ORDER_MANAGEMENT_STATUS.concat(orderStatusDTO.getOrderStatus().toString());
    }

}
