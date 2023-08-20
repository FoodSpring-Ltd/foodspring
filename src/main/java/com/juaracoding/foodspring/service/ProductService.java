package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 4:42 PM
@Last Modified 8/20/2023 4:42 PM
Version 1.0
*/

import com.juaracoding.foodspring.dto.ProductDTO;
import com.juaracoding.foodspring.dto.ProductSimpleResponse;
import com.juaracoding.foodspring.model.Category;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.model.Product;
import com.juaracoding.foodspring.model.Variant;
import com.juaracoding.foodspring.repository.CategoryRepository;
import com.juaracoding.foodspring.repository.DiscountRepository;
import com.juaracoding.foodspring.repository.ProductRepository;
import com.juaracoding.foodspring.repository.VariantRepository;
import com.juaracoding.foodspring.utils.CloudinaryUtils;
import com.juaracoding.foodspring.utils.LoggingFile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private final String[] strExceptionArr = new String[2];
    private final DiscountRepository discountRepository;
    @Autowired
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private VariantRepository variantRepository;

    @Autowired
    private CloudinaryUtils cloudinaryUtils;

    @Autowired
    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository, VariantRepository variantRepository,
                          DiscountRepository discountRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        strExceptionArr[0] = "ProductService";
        this.discountRepository = discountRepository;
    }

    @Transactional
    public Map<String, Object> createProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<Category> category = null;
        CompletableFuture<Optional<Discount>> discount = null;
        try {
            if (productDTO.getCategoryId() != null) {
                category = CompletableFuture.supplyAsync(
                        () -> categoryRepository.findByCategoryId(productDTO.getCategoryId()));
            }
            if (productDTO.getDiscountId() != null) {
                discount = CompletableFuture.supplyAsync(
                        () -> discountRepository.findById(productDTO.getDiscountId()));
            }
            CompletableFuture<List<Variant>> variants = CompletableFuture.supplyAsync(
                    () -> stringToListVariant(productDTO.getVariants()));

            Product newProduct = Product.builder()
                    .productName(productDTO.getProductName())
                    .description(productDTO.getDescription())
                    .price(productDTO.getPrice())
                    .isDelete(false)
                    .isAvailable(true)
                    .build();
            if (!Objects.isNull(category)) {
                newProduct.setCategory(category.get());
            }
            if (!Objects.isNull(discount)) {
                if (discount.get().isPresent()) {
                    newProduct.setDiscount(discount.get().get());
                }
            }
            productRepository.save(newProduct);
            for (Variant variant : variants.get()) {
                variant.setProduct(newProduct);
                variantRepository.save(variant);
            }
            // Upload image only when everything's okay
            Map<String, String> uploadResult = cloudinaryUtils.uploadFileToCloudinary(imageFile, newProduct.getProductId());
            String secureURL = uploadResult.get("secure_url");
            newProduct.setImageURL(secureURL);
            response.put("success", true);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            strExceptionArr[1] = "createProduct(ProductDTO productDTO, MultipartFile imageFile) --LINE 99";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    public Map<String, Object> getAllProduct(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try{
            Page<Product> results = productRepository.findAllByIsDeleteFalse(pageable);
            List<ProductSimpleResponse> products = modelMapper.map(results.getContent(), new TypeToken<List<ProductSimpleResponse>>() {}.getType());
            response.put("data", products);
            response.put("totalPages", results.getTotalPages());
            response.put("totalElements", results.getTotalElements());
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllProduct(Pageable pageable) --LINE 122";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }
    private List<Variant> stringToListVariant(String variant) {
        if (Objects.isNull(variant) || variant.length() == 0) {
            return new ArrayList<>();
        }
        String[] variantsName = variant.trim().split(",");
        if (variantsName.length == 0) {
            return new ArrayList<>();
        }
        List<Variant> results = new ArrayList<>();
        for (String name : variantsName) {
            Variant newVariant = new Variant();
            newVariant.setName(name);
            results.add(newVariant);
        }
        return results;
    }
}
