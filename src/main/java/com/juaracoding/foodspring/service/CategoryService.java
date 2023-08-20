package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 11:16 AM
@Last Modified 8/20/2023 11:16 AM
Version 1.0
*/

import com.juaracoding.foodspring.dto.CategoryDTO;
import com.juaracoding.foodspring.dto.CategorySimpleResponse;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.Category;
import com.juaracoding.foodspring.repository.CategoryRepository;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.LoggingFile;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {


    private CategoryRepository categoryRepository;

    private final String[] strExceptionArr = new String[2];

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        strExceptionArr[0] = "CategoryService";
    }

    public Map<String, Object> createCategory(CategoryDTO categoryDTO, WebRequest request) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setCreatedAt(LocalDateTime.now());
        category.setModifiedBy((Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION));
        category.setUpdatedAt(LocalDateTime.now());

        try {
            categoryRepository.save(category);
        } catch (Exception ex) {
            strExceptionArr[1] = "createCategory(CategoryDTO categoryDTO, WebRequest request) --LINE 47";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATING_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0001", request);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CREATE_CATEGORY, HttpStatus.CREATED, category, null, request);
    }

    public Map<String, Object> getAllCategory(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try{
            Page<Category> results = categoryRepository.findAll(pageable);
            List<CategorySimpleResponse> categories = modelMapper.map(results.getContent(), new TypeToken<List<CategorySimpleResponse>>() {}.getType());
            response.put("data", categories);
            response.put("totalPages", results.getTotalPages());
            response.put("totalElements", results.getTotalElements());
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllCategory(Pageable pageable) --LINE 78";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    @Transactional
    public Map<String, Object> deleteCategoryById(Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        try{
            categoryRepository.deleteById(categoryId);
            response.put("success", true);
        } catch (Exception ex) {
            response.put("success", false);
            strExceptionArr[1] = "deleteCategoryById(Long categoryId) --LINE 92";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    @Transactional
    public Map<String, Object> updateCategoryNameById(CategoryDTO categoryDTO) {
        Map<String, Object> response = new HashMap<>();
        try{
            Category category = categoryRepository.findByCategoryId(categoryDTO.getCategoryId());
            category.setName(categoryDTO.getName());
            response.put("success", true);
        } catch (Exception ex) {
            response.put("success", false);
            strExceptionArr[1] = "updateCategoryNameById(CategoryDTO categoryDTO) --LINE 108";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> results = modelMapper.map(categories,
                new TypeToken<List<CategoryDTO>>() {}.getType());
        return results;
    }

}
