package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 10:27 AM
@Last Modified 8/22/2023 10:27 AM
Version 1.0
*/

import com.juaracoding.foodspring.accessannotation.BasicAccess;
import com.juaracoding.foodspring.config.MidtransConfig;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.CartResponse;
import com.juaracoding.foodspring.service.CartService;
import com.juaracoding.foodspring.service.TransactionService;
import com.juaracoding.foodspring.utils.MappingAttribute;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = ServicePath.CART)
@BasicAccess
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private MappingAttribute mappingAttribute;

    @Autowired
    private TransactionService transactionService;

    private String[] strExceptions = new String[2];

    private Map<String, Object> objectMapper = new HashMap<>();

    public CartController() {
        strExceptions[1] = "CartController";
    }

    @GetMapping("")
    public String cartPage(Model model, WebRequest request) throws MidtransError {
        mappingAttribute.setAttribute(model, request);
        objectMapper = cartService.getAllCartItem(request);
        CartResponse cartResponse = (CartResponse) objectMapper.get("data");
        model.addAttribute("cart", cartResponse);
        model.addAttribute("SNAP_URL", MidtransConfig.getSnapURL());
        model.addAttribute("MIDTRANS_CLIENT_KEY", MidtransConfig.getClientKey());
        model.addAttribute("snapToken", "transactionService.createOrder(request");
        return ViewPath.CART;
    }



}
