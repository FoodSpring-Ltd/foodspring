package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/17/2023 3:51 PM
@Last Modified 8/17/2023 3:51 PM
Version 1.0
*/

import com.juaracoding.foodspring.dto.ForgetPasswordDTO;
import com.juaracoding.foodspring.dto.UserDTO;
import com.juaracoding.foodspring.model.User;
import com.juaracoding.foodspring.utils.MappingAttribute;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("")
public class CheckPageController {

    private Map<String, Object> objectMapper = new HashMap<String, Object>();
    private MappingAttribute mappingAttribute = new MappingAttribute();

    @GetMapping("/login")
    public String pageOne(Model model) {
        User user = new User();
        model.addAttribute("usr", user);
        return "auth/login-page";
    }

    @GetMapping("/register")
    public String pageTwo(Model model) {
        UserDTO users = new UserDTO();
        model.addAttribute("usr", users);
        return "auth/register-page";
    }

    @GetMapping("/verify")
    public String pageThree(Model model) {
        model.addAttribute("usr", new User());
        return "auth/auth-verify";
    }



    @GetMapping("/forgetpassword")
    public String pageSeven(Model model) {
        ForgetPasswordDTO forgetPasswordDTO = new ForgetPasswordDTO();
        model.addAttribute("forgetpwd", forgetPasswordDTO);
        return "auth/forget-pwd-email";
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/api/check/signin";
    }
}
