package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 10:18 AM
@Last Modified 8/27/2023 10:18 AM
Version 1.0
*/

import com.foodspring.annotation.BasicAccess;
import com.juaracoding.foodspring.config.MidtransConfig;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.OrderResponse;
import com.juaracoding.foodspring.service.OrderService;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.MappingAttribute;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MappingAttribute mappingAttribute;
    private Map<String, Object> objectMapper = new HashMap<>();


    @GetMapping(value = ServicePath.CHECKOUT_PAYMENT)
    @BasicAccess
    public String checkoutPayment(Model model,
                              RedirectAttributes redirectAttributes,
                              WebRequest request) {
        objectMapper = orderService.createOrder(request);
        if (objectMapper.get("message").equals(ConstantMessage.CART_EMPTY)) {
            return ServicePath.REDIRECT_ORDER_UNPAID;
        }
        if (!(Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", objectMapper.get("message"));
            return ServicePath.REDIRECT_HOME;
        }
        OrderResponse shopOrder = (OrderResponse) objectMapper.get("data");
        if (!(Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", objectMapper.get("message"));
            return ServicePath.REDIRECT_ORDER_UNPAID;
        }
        model.addAttribute("SNAP_URL", MidtransConfig.getSnapURL());
        model.addAttribute("MIDTRANS_CLIENT_KEY", MidtransConfig.getClientKey());
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("shopOrders", List.of(shopOrder));
        mappingAttribute.setAttribute(model, request);
        return ViewPath.CHECKOUT_PAGE;
    }

    @GetMapping(value = ServicePath.CHECKOUT_PAYMENT + ServicePath.ORDER_ID)
    @BasicAccess
    public String checkoutByOrderId(Model model,
                                  @PathVariable String orderId,
                                  RedirectAttributes redirectAttributes,
                                  WebRequest request) throws MidtransError {
        objectMapper = orderService.getShopOrderById(orderId, request);
        OrderResponse shopOrder = (OrderResponse) objectMapper.get("data");
        if (!(Boolean) objectMapper.get("success")) {
            redirectAttributes.addFlashAttribute("message", objectMapper.get("message"));
            return ServicePath.REDIRECT_ORDER_UNPAID;
        }
        if (shopOrder.getSnapToken() == null || shopOrder.getSnapToken().length() == 0) {
            String newToken = orderService.createSnapToken(shopOrder.getShopOrderId(), shopOrder.getGrandTotal().intValue());
            shopOrder.setSnapToken(newToken);
        }
        model.addAttribute("SNAP_URL", MidtransConfig.getSnapURL());
        model.addAttribute("MIDTRANS_CLIENT_KEY", MidtransConfig.getClientKey());
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        model.addAttribute("shopOrders", List.of(shopOrder));
        mappingAttribute.setAttribute(model, request);
        return ViewPath.CHECKOUT_PAGE;
    }

}
