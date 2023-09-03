package com.juaracoding.foodspring.controller;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/3/2023 10:06 AM
@Last Modified 9/3/2023 10:06 AM
Version 1.0
*/

import com.foodspring.annotation.BasicAccess;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = ServicePath.FILE)
public class FileController {

    @Autowired
    private InvoiceService invoiceService;

    private Map<String, Object> objectMapper = new HashMap<>();

    @GetMapping(value = ServicePath.INVOICE + ServicePath.ORDER_ID)
    @BasicAccess
    public ResponseEntity<byte[]> getOrderInvoice(@PathVariable String orderId,
                                                  WebRequest request) {
        objectMapper = invoiceService.generateOrderInvoice(orderId, request);
        if (!(Boolean) objectMapper.get("success")) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> data = (Map<String, Object>) objectMapper.get("data");
        byte[] file = (byte[]) data.get("file");
        String fileName = (String) data.get("fileName");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", fileName.concat(".pdf"));
        return  new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
}
