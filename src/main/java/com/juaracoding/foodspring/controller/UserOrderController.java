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
import com.juaracoding.foodspring.utils.MappingAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(value = ServicePath.ORDER)
@BasicAccess
public class UserOrderController {

    @Autowired
    private MappingAttribute mappingAttribute;

    private String[] strExceptions = new String[2];

    public UserOrderController() {
        strExceptions[0] = " UserOrderController";
    }

    @GetMapping(value = ServicePath.UNPAID)
    public String unpaidOrder(Model model, WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ORDER_UNPAID;
    }

    @GetMapping(value = ServicePath.COMPLETED)
    public String completedOrder(Model model, WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ORDER_COMPLETED;
    }

    @GetMapping(value = ServicePath.ON_PROCESS)
    public String onProcessOrder(Model model, WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        mappingAttribute.setAttribute(model, request);
        return ViewPath.ORDER_ON_PROCESS;
    }
}
