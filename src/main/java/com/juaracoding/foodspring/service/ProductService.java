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
import com.juaracoding.foodspring.handler.ResourceNotFoundException;
import com.juaracoding.foodspring.model.Category;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.model.Product;
import com.juaracoding.foodspring.model.Variant;
import com.juaracoding.foodspring.repository.CategoryRepository;
import com.juaracoding.foodspring.repository.DiscountRepository;
import com.juaracoding.foodspring.repository.ProductRepository;
import com.juaracoding.foodspring.repository.VariantRepository;
import com.juaracoding.foodspring.utils.CloudinaryUtils;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.LoggingFile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
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
            CompletableFuture<List<Variant>> variants = stringToListVariant(productDTO.getVariants());

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
            CompletableFuture<Void> addVariants = addVariant(variants.get(), newProduct);

            // Upload image only when everything's okay
            Map<String, String> uploadResult = cloudinaryUtils.uploadFileToCloudinary(imageFile, newProduct.getProductId());
            String secureURL = uploadResult.get("secure_url");
            newProduct.setImageURL(secureURL);
            CompletableFuture.allOf(addVariants).join();
            response.put("success", true);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            strExceptionArr[1] = "createProduct(ProductDTO productDTO, MultipartFile imageFile) --LINE 99";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateProduct(ProductDTO productDTO, MultipartFile imageFile, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<Category> category = null;
        CompletableFuture<Optional<Discount>> discount = null;
        try {
            CompletableFuture<Optional<Product>> product = CompletableFuture.supplyAsync(
                    () -> productRepository.findById(productDTO.getProductId()));
            CompletableFuture<Void> deleteVariants = deleteVariantByProductId(productDTO.getProductId());
            if (productDTO.getCategoryId() != null) {
                category = CompletableFuture.supplyAsync(
                        () -> categoryRepository.findByCategoryId(productDTO.getCategoryId()));
            }
            if (productDTO.getDiscountId() != null) {
                discount = CompletableFuture.supplyAsync(
                        () -> discountRepository.findById(productDTO.getDiscountId()));
            }
            CompletableFuture<List<Variant>> variants = stringToListVariant(productDTO.getVariants());

            if (product.get().isEmpty()) {
                throw new ResourceNotFoundException("Product doesn't exist");
            }
            Product oldProduct = product.get().get();
            if (!Objects.isNull(category)) {
                oldProduct.setCategory(category.get());
            } else {
                oldProduct.setCategory(null);
            }

            if (!Objects.isNull(discount)) {
                if (discount.get().isPresent()) {
                    oldProduct.setDiscount(discount.get().get());
                }
            } else {
                oldProduct.setDiscount(null);
            }

            CompletableFuture<Void> addVariants = addVariant(variants.get(), oldProduct);
            oldProduct.setIsAvailable(productDTO.getIsAvailable());
            oldProduct.setModifiedBy((Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION));
            oldProduct.setUpdatedAt(LocalDateTime.now());
            oldProduct.setDescription(productDTO.getDescription());
            productRepository.save(oldProduct);
            CompletableFuture.allOf(deleteVariants, addVariants).join();
            if (!imageFile.isEmpty()) {
                Map<String, String> uploadResult = cloudinaryUtils.uploadFileToCloudinary(imageFile, oldProduct.getProductId());
                String secureURL = uploadResult.get("secure_url");
                oldProduct.setImageURL(secureURL);
            }
            response.put("success", true);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            strExceptionArr[1] = "updateProduct(ProductDTO productDTO, MultipartFile imageFile) --LINE 160";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    public Map<String, Object> getAllProduct(Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<Product> results = productRepository.findAllByIsDeleteFalse(pageable);
            List<ProductSimpleResponse> products = convertToProductSimpleResponseList(results.getContent());
            response.put("data", products);
            response.put("totalPages", results.getTotalPages());
            response.put("totalElements", results.getTotalElements());
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllProduct(Pageable pageable) --LINE 122";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
        }
        return response;
    }

    public ProductDTO getProductDTOById(String productId) throws ResourceNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        ProductDTO productDTO = null;
        if (product.isPresent()) {
            Category category = product.get().getCategory();
            Discount discount = product.get().getDiscount();
            productDTO = modelMapper.map(product.get(), new TypeToken<ProductDTO>() {
            }.getType());
            if (!Objects.isNull(category)) {
                productDTO.setCategoryId(category.getCategoryId());
            }
            if (!Objects.isNull(discount)) {
                productDTO.setDiscountId(discount.getDiscountId());
            }
            productDTO.setVariants(productVariantToString(product.get().getVariants()));
            return productDTO;
        } else {
            throw new ResourceNotFoundException("Product doesn't exist");
        }
    }

    @Async
    private CompletableFuture<List<Variant>> stringToListVariant(String variant) {
        if (Objects.isNull(variant) || variant.length() == 0) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        String[] variantsName = variant.trim().split(",");
        if (variantsName.length == 0) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        List<Variant> results = Arrays.stream(variantsName).parallel()
                .map(name -> {
                    Variant newVariant = new Variant();
                    newVariant.setName(name);
                    return newVariant;
                })
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(results);
    }


    private String productVariantToString(List<Variant> variants) {
        if (Objects.isNull(variants) || variants.size() == 0) {
            return "";
        }
        return variants.parallelStream()
                .map(Variant::getName)
                .collect(Collectors.joining(","));
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    private CompletableFuture<Void> deleteVariantByProductId(String productId) {
        variantRepository.deleteByProductProductId(productId);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional
    private CompletableFuture<Void> addVariant(List<Variant> variants, Product product) {
        for (Variant variant : variants) {
            variant.setProduct(product);
            variantRepository.save(variant);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> softDeleteById(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        Map<String, Object> response = new HashMap<>();
        if (product.isPresent()) {
            product.get().setIsDelete(true);
            response.put("success", true);
            response.put("message", ConstantMessage.PRODUCT_DELETION_SUCCESS);
            return response;
        }
        response.put("success", false);
        response.put("message", ConstantMessage.ERROR_DELETE_PRODUCT);
        return response;
    }

    private List<ProductSimpleResponse> convertToProductSimpleResponseList(List<Product> products) {
        List<ProductSimpleResponse> results = Optional.ofNullable(products).orElse(Collections.emptyList()).stream()
                .map(item -> {
                    ProductSimpleResponse res = ProductSimpleResponse.builder()
                            .productId(item.getProductId())
                            .category(item.getCategory())
                            .description(item.getDescription())
                            .isAvailable(item.getIsAvailable())
                            .imageURL(item.getImageURL())
                            .productName(item.getProductName())
                            .price(item.getPrice()).build();
                    res.setVariants(productVariantToString(item.getVariants()));
                    return res;
                }).toList();
        return results;
    }

}
