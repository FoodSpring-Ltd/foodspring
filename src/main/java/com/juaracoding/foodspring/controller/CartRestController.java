package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 3:08 PM
@Last Modified 8/22/2023 3:08 PM
Version 1.0
*/

import com.juaracoding.foodspring.accessannotation.BasicAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.dto.CartItemDTO;
import com.juaracoding.foodspring.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@RestController
@RequestMapping(value = ServicePath.API_CART)
@BasicAccess
public class CartRestController {

    @Autowired
    private CartService cartService;

    private Map<String, Object> objectMapper;

    @PostMapping(value = "")
    public ResponseEntity<Map<String, Object>> addItem(@ModelAttribute("cartItem") @Valid CartItemDTO cartItemDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes,
                                  WebRequest request) {
        objectMapper = cartService.addToCart(cartItemDTO, request);
        if ((Boolean) objectMapper.get("success")) {
            return new ResponseEntity<>(objectMapper, HttpStatus.CREATED);
        }
        int status = (int) objectMapper.get("status");
        return new ResponseEntity<>(objectMapper, HttpStatus.valueOf(status));
    }
}
