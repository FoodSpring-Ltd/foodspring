package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/10/2023 4:11 PM
@Last Modified 8/10/2023 4:11 PM
Version 1.0
*/

import com.foodspring.model.EmailVerification;
import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.ViewPath;
import com.juaracoding.foodspring.dto.CartItemDTO;
import com.juaracoding.foodspring.dto.ProductSimpleResponse;
import com.juaracoding.foodspring.exceptions.EmailPublisherException;
import com.juaracoding.foodspring.model.Product;
import com.juaracoding.foodspring.publisher.MailPublisher;
import com.juaracoding.foodspring.service.CategoryService;
import com.juaracoding.foodspring.service.ProductService;
import com.juaracoding.foodspring.utils.MappingAttribute;
import com.juaracoding.foodspring.utils.PageProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private MappingAttribute mappingAttribute;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MailPublisher mailPublisher;

    private String[] strExceptions = new String[2];
    private Map<String, String> mapProps = new HashMap<>();


    public MainController() {
        mapProps.put("priceasc", "Price Asc");
        mapProps.put("pricedesc", "Price Desc");
        mapProps.put("updatedatdesc", "Latest");
        mapProps.put("updatedatasc", "Oldest");
        mapProps.put("default", "Sort By");
        strExceptions[0] = "MainController";
    }

    private Map<String, Object> objectMapper = new HashMap<>();

    @GetMapping("/test")
    public String test() throws EmailPublisherException {
        mailPublisher.sendEmailMessage(new EmailVerification(""));
        return ServicePath.REDIRECT_HOME;
    }
    @GetMapping(value = "/home")
    public String home(PageProperty pageProperty,
                       @RequestParam(required = false) List<Long> selectedCategoryIds,
                       Model model,
                       WebRequest request) {
        if (Objects.isNull(selectedCategoryIds)) {
            selectedCategoryIds = new ArrayList<>();
        }

        objectMapper = productService.getAllProduct(pageProperty.getPageable(), selectedCategoryIds, request);

        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<ProductSimpleResponse> products = (List<ProductSimpleResponse>) objectMapper.get("content");
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("selectedCategoryIds", selectedCategoryIds);
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage",  ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("products", products);
        model.addAttribute("selectedSort", mapSelectedSort(pageProperty));

        mappingAttribute.setAttribute(model, request);
        return ViewPath.MAIN_VIEW_PRODUCTS;
    }

    @GetMapping(value = "/")
    public String redirect(){
        return ServicePath.REDIRECT_HOME;
    }

    @GetMapping(value = ServicePath.PRODUCT_DETAIL_PRODUCT_ID)
    public String productDetail(@PathVariable String productId,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                WebRequest request) {
        model.addAttribute("HIDE_TOP_SEARCH_BAR", true);
        mappingAttribute.setAttribute(model, request);
        model.addAttribute("cartItem", new CartItemDTO());
        try {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
            model.addAttribute("variants", product.getVariants());
        } catch (Exception ex) {
            strExceptions[1] = "productDetail(@RequestParam String id, Model model, WebRequest request) --LINE 91";
            LoggingFile.exceptionString(strExceptions, ex, "y");
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return ServicePath.REDIRECT_HOME;
        }
        return ViewPath.MAIN_PRODUCT_DETAILS;
    }

    @GetMapping(value = ServicePath.HOME_SEARCH)
    public String searchProductByName(PageProperty pageProperty,
                                      @RequestParam String productName,
                                      Model model,
                                      WebRequest request) {

        objectMapper = productService.searchProductByName(productName,
                pageProperty.getPageable(), request);

        objectMapper = (Map<String, Object>) objectMapper.get("data");
        List<ProductSimpleResponse> products = (List<ProductSimpleResponse>) objectMapper.get("content");
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("selectedCategoryIds", new ArrayList<>());
        model.addAttribute("selectedRow", pageProperty.getLimit());
        model.addAttribute("totalPages", objectMapper.get("totalPages"));
        model.addAttribute("totalElements", objectMapper.get("totalItems"));
        model.addAttribute("currentPage",  ((int) objectMapper.get("currentPage")) + 1);
        model.addAttribute("products", products);
        model.addAttribute("selectedSort", mapSelectedSort(pageProperty));

        mappingAttribute.setAttribute(model, request);
        return ViewPath.MAIN_VIEW_PRODUCTS;
    }

    private String mapSelectedSort(PageProperty props) {
       if (!Objects.isNull(props.getSortBy()) && !Objects.isNull(props.getSortTypeString())) {
           String key = props.getSortBy().toLowerCase().concat(props.getSortTypeString().toLowerCase());
           return  mapProps.get(key);
       }
       return mapProps.get("default");
    }

}
