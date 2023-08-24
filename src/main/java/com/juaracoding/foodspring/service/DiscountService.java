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
import com.juaracoding.foodspring.handler.ResourceNotFoundException;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.model.Product;
import com.juaracoding.foodspring.repository.DiscountRepository;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.utils.TransformToDTO;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class DiscountService {
    private final String[] strExceptionArr = new String[2];
    private final DiscountRepository discountRepository;
    @Autowired
    private ModelMapper modelMapper;

    private TransformToDTO<Discount, DiscountDTO> transformer = new TransformToDTO<>();

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        strExceptionArr[0] = "DiscountService";
        this.discountRepository = discountRepository;
    }

    public List<DiscountDTO> getAllDiscount() {
        List<Discount> discounts = discountRepository.findAllByIsDeleteFalse();
        List<DiscountDTO> results = modelMapper.map(discounts,
                new TypeToken<List<DiscountDTO>>() {
                }.getType());
        return results;
    }

    public Map<String, Object> getAllDiscount(Pageable pageable, WebRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<Discount> page = discountRepository.findAllByIsDeleteFalse(pageable);
            List<DiscountDTO> discounts = modelMapper.map(page.getContent(), new TypeToken<List<DiscountDTO>>() {
            }.getType());
            result = transformer.transformObject(new HashMap<>(), discounts, page);
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllDiscount(Pageable pageable, WebRequest request) --LINE 63";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR, result, "FSD0001", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, result, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createDiscount(DiscountDTO discountDTO, WebRequest request) {
        Long adminId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        Discount discount = modelMapper.map(discountDTO, Discount.class);
        discount.setModifiedBy(adminId);

        try {
            if (!isValidStartEndDateRange(discount.getStartAt(), discount.getEndAt())) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.INVALID_DATE_RANGE,
                        HttpStatus.BAD_REQUEST, null, "FS0003", request);
            }
            discountRepository.save(discount);
        } catch (Exception ex) {
            strExceptionArr[1] = "createDiscount(DiscountDTO discountDTO, WebRequest request) --LINE 79";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_DISCOUNT,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0003", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CREATE_DISCOUNT,
                HttpStatus.CREATED, discount, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateDiscount(DiscountDTO discountDTO, WebRequest request) {
        Long adminId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        ModelMapper localMapper = new ModelMapper();
        localMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        try {
            if (!isValidStartEndDateRange(discountDTO.getStartAt(), discountDTO.getEndAt())) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.INVALID_DATE_RANGE,
                        HttpStatus.BAD_REQUEST, null, "FS0003", request);
            }
            Optional<Discount> discount = discountRepository.findById(discountDTO.getDiscountId());
            if (discount.isEmpty()) {
                throw new ResourceNotFoundException("discount doesn't exist");
            }
            localMapper.map(discountDTO, discount.get());
            discount.get().setModifiedBy(adminId);
        } catch (Exception ex) {
            strExceptionArr[1] = "updateDiscount(DiscountDTO discountDTO, WebRequest request) --LINE 104";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_DISCOUNT,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0003", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_UPDATE_DISCOUNT,
                HttpStatus.OK, null, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> softDeleteDiscountById(Long discountId, WebRequest request) {
        Long adminId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        try {
            Optional<Discount> discount = discountRepository.findById(discountId);
            if (discount.isEmpty()) {
                throw new ResourceNotFoundException("discount doesn't exist");
            }
            discount.get().setIsDelete(true);
            for (Product product: discount.get().getProducts()){
                product.setDiscount(null);
            }
        } catch (Exception ex) {
            strExceptionArr[1] = "softDeleteDiscountById(Long discountId, WebRequest request) --LINE 124";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_DELETE_DISCOUNT,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0004", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_DELETE_DISCOUNT,
                HttpStatus.OK, null, null, request);
    }

    private boolean isValidStartEndDateRange(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }
}
