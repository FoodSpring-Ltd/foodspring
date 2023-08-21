package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 9:16 PM
@Last Modified 8/20/2023 9:16 PM
Version 1.0
*/

import com.juaracoding.foodspring.dto.DiscountDTO;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.repository.DiscountRepository;
import com.juaracoding.foodspring.utils.LoggingFile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscountService {
    private final String[] strExceptionArr = new String[2];

    @Autowired
    private ModelMapper modelMapper;

    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        strExceptionArr[0] = "DiscountService";
        this.discountRepository = discountRepository;
    }

    public List<DiscountDTO> getAllDiscount() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountDTO> results = modelMapper.map(discounts,
                new TypeToken<List<DiscountDTO>>() {}.getType());
        return results;
    }

    public Map<String, Object> getAllDiscount(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try{
            Page<Discount> results = discountRepository.findAll(pageable);
            List<DiscountDTO> discounts = modelMapper.map(results.getContent(), new TypeToken<List<DiscountDTO>>() {}.getType());
            response.put("data", discounts);
            response.put("totalPages", results.getTotalPages());
            response.put("totalElements", results.getTotalElements());
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllDiscount(Pageable pageable) --LINE 60";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    public Map<String, Object> createDiscount(DiscountDTO discountDTO, WebRequest request) {
        return null;
    }
}
