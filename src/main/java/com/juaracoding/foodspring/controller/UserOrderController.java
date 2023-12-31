package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/25/2023 7:58 PM
@Last Modified 8/25/2023 7:58 PM
Version 1.0
*/

import com.foodspring.annotation.BasicAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.model.ShopOrder;
import com.juaracoding.foodspring.service.InvoiceService;
import com.juaracoding.foodspring.service.OrderService;
import com.juaracoding.foodspring.utils.MappingAttribute;
import com.juaracoding.foodspring.utils.PageProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = ServicePath.ORDER)
@BasicAccess
public class UserOrderController {

    @Autowired
    private MappingAttribute mappingAttribute;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InvoiceService invoiceService;

    private Map<String, Object> objectMapper = new HashMap<>();
    private String[] strExceptions = new String[2];

    public UserOrderController() {
        strExceptions[0] = "UserOrderController";
    }

    @GetMapping(value = "")
    public String showOrders(PageProperty pageProperty,
                              @RequestParam OrderStatus status,
                              Model model,
                              WebRequest request) {
        objectMapper = orderService.getAllOrderByStatus(pageProperty.getPageable(), status, request);
        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<ShopOrder> data = (List<ShopOrder>) objectMapper.get("content");
        model.addAttribute("userOrderStatus", status.toString());
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage", ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("shopOrders", data);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.USER_ORDER;
    }


}
