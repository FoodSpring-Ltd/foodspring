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

import com.foodspring.utils.CurrencyFormatter;
import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.dto.ProductDTO;
import com.juaracoding.foodspring.dto.ProductSimpleResponse;
import com.juaracoding.foodspring.handler.ResourceNotFoundException;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.Category;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.model.Product;
import com.juaracoding.foodspring.model.Variant;
import com.juaracoding.foodspring.repository.*;
import com.juaracoding.foodspring.utils.CalcUtils;
import com.juaracoding.foodspring.utils.CloudinaryUtils;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.TransformToDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final CartItemRepository cartItemRepository;

    @Autowired
    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository, VariantRepository variantRepository,
                          DiscountRepository discountRepository,
                          CartItemRepository cartItemRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        strExceptionArr[0] = "ProductService";
        this.discountRepository = discountRepository;
        this.cartItemRepository = cartItemRepository;
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

    public Map<String, Object> searchProductByName(String productName, Pageable pageable, WebRequest request) {
        Page<Product> products = productRepository.findAllByIsDeleteFalseAndProductNameContainsIgnoreCase(productName, pageable);
        TransformToDTO<Product, ProductSimpleResponse> transformer = new TransformToDTO<>();
        Map<String, Object> result = transformer.transformObject(new HashMap<>(), convertToProductSimpleResponseList(products.getContent()), products);
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY.concat("Product Name"),
                HttpStatus.OK, result, null, request);
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
            oldProduct.setPrice(productDTO.getPrice());
            oldProduct.setProductName(productDTO.getProductName());
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

    public Map<String, Object> getAllProduct(Pageable pageable, WebRequest request) {
        Map<String, Object> result;
        try {
            Page<Product> results = productRepository.findAllByIsDeleteFalse(pageable);
            List<ProductSimpleResponse> products = convertToProductSimpleResponseList(results.getContent());
            TransformToDTO<Product, ProductSimpleResponse> transformer = new TransformToDTO<>();
            result = transformer.transformObject(new HashMap<>(), products, results);
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllProduct(Pageable pageable, WebRequest request) --LINE 122";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_RETRIEVE_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0006", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, result, null, request);
    }

    public Map<String, Object> getAllProduct(Pageable pageable, List<Long> categoryIds, WebRequest request) {
        Map<String, Object> result;
        try {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return getAllProduct(pageable, request);
            }
            Page<Product> results = productRepository.findAllByIsDeleteFalseAndCategoryCategoryIdIn(categoryIds, pageable);
            List<ProductSimpleResponse> products = convertToProductSimpleResponseList(results.getContent());
            TransformToDTO<Product, ProductSimpleResponse> transformer = new TransformToDTO<>();
            result = transformer.transformObject(new HashMap<>(), convertToProductSimpleResponseList(results.getContent()), results);
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllProduct(Pageable pageable, List<Long> categoryIds, WebRequest request) --LINE 201";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_RETRIEVE_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0006", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, result, null, request);
    }

    public ProductDTO getProductDTOById(String productId) throws ResourceNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        ProductDTO productDTO = null;
        if (product.isPresent()) {
            Category category = product.get().getCategory();
            Discount discount = product.get().getDiscount();
            productDTO = modelMapper.map(product.get(), new TypeToken<ProductDTO>() {}.getType());
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

    public Product getProductById(String productId) throws ResourceNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setPriceIDR(CurrencyFormatter.toRupiah(product.get().getPrice()));
            if (!Objects.isNull(product.get().getDiscount())) {
                if (isDiscountApplicable(product.get().getDiscount())) {
                    product.get().setDiscountedPriceIDR(CalcUtils.getDiscountedPriceIDR(product.get().getPrice(),
                           product.get().getDiscount().getPercentDiscount()));
                }
            }
            return product.get();
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
        if (variants.isEmpty()) {
            Variant variant = new Variant();
            variant.setName("Original");
            variant.setProduct(product);
            variantRepository.save(variant);
        }
        for (Variant variant : variants) {
            variant.setProduct(product);
            variantRepository.save(variant);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> softDeleteById(String productId, WebRequest request) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setIsDelete(true);
            cartItemRepository.deleteByProductProductId(productId);
            return new ResponseHandler().generateModelAttribut(ConstantMessage.PRODUCT_DELETION_SUCCESS,
                    HttpStatus.OK, null, null, request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_DELETE_PRODUCT,
                HttpStatus.NOT_FOUND, null, null, request);
    }

    private List<ProductSimpleResponse> convertToProductSimpleResponseList(List<Product> products) {
        List<ProductSimpleResponse> results = Optional.ofNullable(products).orElse(Collections.emptyList()).stream()
                .map(item -> {
                    ProductSimpleResponse res = ProductSimpleResponse.builder()
                            .productId(item.getProductId())
                            .category(item.getCategory())
                            .discount(item.getDiscount())
                            .description(item.getDescription())
                            .isAvailable(item.getIsAvailable())
                            .imageURL(item.getImageURL())
                            .productName(item.getProductName())
                            .priceIDR(CurrencyFormatter.toRupiah(item.getPrice()))
                            .price(item.getPrice()).build();
                    if (!Objects.isNull(res.getDiscount())) {
                        if (isDiscountApplicable(res.getDiscount())) {
                            res.setDiscountedPriceIDR(CalcUtils.getDiscountedPriceIDR(res.getPrice(),
                                    res.getDiscount().getPercentDiscount()));
                        }
                    }
                    res.setVariants(productVariantToString(item.getVariants()));
                    return res;
                }).toList();
        return results;
    }

    private boolean isDiscountApplicable(Discount discount) {
       boolean isStarted = discount.getStartAt().equals(LocalDateTime.now()) || discount.getStartAt().isBefore(LocalDateTime.now());
       boolean isEnded = discount.getEndAt().isBefore(LocalDateTime.now());
       return isStarted && !isEnded;
    }

}
