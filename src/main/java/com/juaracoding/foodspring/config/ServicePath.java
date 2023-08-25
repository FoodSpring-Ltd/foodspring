package com.juaracoding.foodspring.config;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/18/2023 1:56 PM
@Last Modified 8/18/2023 1:56 PM
Version 1.0
*/

public class ServicePath {
    public static final String AUTH = "/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String NEW_TOKEN = "/newToken";
    public static final String VERIFY = "/verify";
    public static final String FORGET_PASSWORD = "/forget-password";
    public static final String VERIFY_TOKEN_FORGET_PASSWORD = "/verify-token-forget-password";
    public static final String VERIFY_FORGET_PASSWORD = "/verify-forget-password";
    public static final String REQUEST_TOKEN_FORGET_PASSWORD = "/request-token-forget-password";
    public static final String LOGOUT = "/logout";
    public static final String ADMIN = "/admin";
    public static final String DASHBOARD_CATEGORY = "/dashboard/category";
    public static final String DASHBOARD_PRODUCT =  "/dashboard/product";
    public static final String DASHBOARD_PRODUCT_ADD_PRODUCT_FORM = "/dashboard/product/add-product-form";
    public static final String DASHBOARD_PRODUCT_UPDATE_PRODUCT_FORM = "/dashboard/product/update-product-form";
    public static final String PRODUCT_DETAIL = "/product/detail";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String HOME = "/";
    public static final String CATEGORY = "/category";
    public static final String REDIRECT_ADMIN_DASHBOARD_CATEGORY = "redirect:/admin/dashboard/category";
    public static final String CATEGORY_DELETE = "/category/delete";
    public static final String CATEGORY_UPDATE = "/category/update";
    public static final String PRODUCT = "/product";
    public static final String DASHBOARD_PRODUCT_UPDATE_PRODUCT_FORM_PRODUCT_ID =  "/dashboard/product/update-product-form/{productId}";
    public static final String REDIRECT_ADMIN_DASHBOARD_PRODUCT = "redirect:/admin/dashboard/product";
    public static final String PRODUCT_UPDATE = "/product/update";
    public static final String PRODUCT_DELETE = "/product/delete";
    public static final String DASHBOARD_DISCOUNT = "/dashboard/discount";
    public static final String DISCOUNT = "/discount";
    public static final String REDIRECT_ADMIN_DASHBOARD_DISCOUNT = "redirect:/admin/dashboard/discount";
    public static final String DISCOUNT_UPDATE = "/discount/update";
    public static final String DISCOUNT_DELETE = "/discount/delete";
    public static final String REDIRECT_HOME = "redirect:/home";
    public static final String PRODUCT_DETAIL_PRODUCT_ID = "/product/details/{productId}";
    public static final String CART = "/cart";
    public static final String REDIRECT = "redirect:";
    public static final String API_CART = "/api/cart";
    public static final String DELETE_CART_ITEM_ID = "/delete-item/{itemId}";
    public static final String REDIRECT_CART = "redirect:/cart";
    public static final String UPDATE_CART_ITEM_VARIANT = "/update-cart-item-variant/{itemId}";
    public static final String UPDATE_CART_ITEM_QUANTITY = "/update-cart-item-qty/{itemId}/{amount}";
    public static final String HOME_SEARCH = "/home/search";

    public static final String ORDER = "/order";
    public static final String UNPAID = "/unpaid";
    public static final String COMPLETED = "/completed";
    public static final String ON_PROCESS = "/onprocess";
}
