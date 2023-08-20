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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {
    private final String[] strExceptionArr = new String[2];

    @Autowired
    private ModelMapper modelMapper;

    private DiscountRepository discountRepository;

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
}
