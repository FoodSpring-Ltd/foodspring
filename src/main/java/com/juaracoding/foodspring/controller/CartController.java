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

import com.foodspring.annotation.BasicAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.CartResponse;
import com.juaracoding.foodspring.service.CartService;
import com.juaracoding.foodspring.service.OrderService;
import com.juaracoding.foodspring.utils.MappingAttribute;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = ServicePath.CART)
@BasicAccess
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MappingAttribute mappingAttribute;

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
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("cart", cartResponse);
        return ViewPath.CART;
    }

    @GetMapping(value = ServicePath.DELETE_CART_ITEM_ID)
    public String deleteCartItem(Model model,
                                 @PathVariable Long itemId,
                                 RedirectAttributes redirectAttributes,
                                 WebRequest request) {
        objectMapper = cartService.deleteCartItemById(itemId, request);
        redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
        return ServicePath.REDIRECT_CART;
    }

    @PostMapping(value = ServicePath.UPDATE_CART_ITEM_VARIANT)
    public String updateCartItemVariant(Model model,
                                        @PathVariable Long itemId,
                                        @RequestParam Long selectedVariantId,
                                        RedirectAttributes redirectAttributes,
                                        WebRequest request){
        objectMapper = cartService.updateCartItemVariant(itemId, selectedVariantId, request);
        redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
        return ServicePath.REDIRECT_CART;
    }

    @GetMapping(value = ServicePath.UPDATE_CART_ITEM_QUANTITY)
    public String updateCartItemVariant(Model model,
                                        @PathVariable Long itemId,
                                        @PathVariable Integer amount,
                                        RedirectAttributes redirectAttributes,
                                        WebRequest request){
        objectMapper = cartService.updateCartItemQuantity(itemId, amount, request);
        redirectAttributes.addFlashAttribute("message", objectMapper.get("message").toString());
        return ServicePath.REDIRECT_CART;
    }

}
