package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 9:21 AM
@Last Modified 8/22/2023 9:21 AM
Version 1.0
*/

import com.juaracoding.foodspring.dto.CartItemDTO;
import com.juaracoding.foodspring.dto.CartItemResponse;
import com.juaracoding.foodspring.dto.CartResponse;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.*;
import com.juaracoding.foodspring.repository.*;
import com.juaracoding.foodspring.utils.CalcUtils;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.LoggingFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class CartService {

    private final String[] strExceptionArr = new String[2];
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private VariantRepository variantRepository;

    public CartService() {
        strExceptionArr[0] = "CartService";
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addToCart(CartItemDTO cartItemDTO, WebRequest request) {
        Long userId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        try {
            CartItem cartItem = new CartItem();
            CompletableFuture<Optional<Variant>> variant = cartItemDTO.getVariantId() != null
                    ? CompletableFuture.supplyAsync(() -> variantRepository.findById(cartItemDTO.getVariantId()))
                    : CompletableFuture.completedFuture(Optional.empty());
            CompletableFuture<Optional<Product>> product = CompletableFuture.supplyAsync(() -> productRepository.findById(cartItemDTO.getProductId()));
            CompletableFuture<Optional<User>> user = CompletableFuture.supplyAsync(() -> userRepository.findById(userId));

            CompletableFuture.allOf(variant, product, user).join();

            if (product.get().isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.FAILED_ADD_TO_CART_PRODUCT,
                        HttpStatus.BAD_REQUEST, null, "FS0004", request);
            }

            if (user.get().isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.UNAUTHORIZED, null, "FS0004", request);
            }
            if (variant.get().isPresent()) {
                cartItem.setVariant(variant.get().get());
            }
            cartItem.setQty(cartItemDTO.getQty());
            cartItem.setCart(user.get().get().getCart());
            cartItem.setProduct(product.get().get());
            cartItem.setNote(cartItemDTO.getNote());
            cartItemRepository.save(cartItem);

        } catch (Exception ex) {
            strExceptionArr[1] = "addToCart(CartItemDTO cartItemDTO, WebRequest request) -- LINE 86";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.FAILED_ADD_TO_CART,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0004", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_ADD_TO_CART,
                HttpStatus.OK, null, null, request);
    }

    public Map<String, Object> getAllCartItem(WebRequest request) {
        Long userId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        List<CartItemResponse> cartItemsResponses;
        CartResponse cartResponse = new CartResponse();
        try {
            Cart userCart = cartRepository.findByUserUserId(userId);
            cartItemsResponses = cartItemToCartItemResponse(userCart.getCartItems());
            cartResponse.setCartItems(cartItemsResponses);
            double grandTotal = cartItemsResponses.stream().mapToDouble(CartItemResponse::getTotalPrice).sum();
            cartResponse.setGrandTotal(grandTotal);
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllCartItem(WebRequest request) -- LINE 103";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.FAILED_GET_CART_ITEMS,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0004", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_FETCH_DATA,
                HttpStatus.OK, cartResponse, null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteCartItemById(Long cartItemId, WebRequest request) {
        cartItemRepository.deleteById(cartItemId);
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_DELETE_DATA,
                HttpStatus.OK, null, null, request);
    }

    private List<CartItemResponse> cartItemToCartItemResponse(List<CartItem> cartItems){
        List<CartItemResponse> results = new ArrayList<>();
        for(CartItem item: cartItems) {
            CartItemResponse res = CartItemResponse.builder()
                    .cartItemId(item.getCartItemId())
                    .qty(item.getQty())
                    .note(item.getNote())
                    .productImg(item.getProduct().getImageURL())
                    .productName(item.getProduct().getProductName())
                    .unitPrice(item.getProduct().getPrice())
                    .build();
            Discount discount = item.getProduct().getDiscount();
            if (!Objects.isNull(discount)){
                res.setDiscountAmount(discount.getPercentDiscount());
                Double newUnitPrice = CalcUtils.getDiscountedPrice(res.getUnitPrice(), discount.getPercentDiscount());
                res.setTotalPrice(newUnitPrice * res.getQty());
            }else {
                res.setTotalPrice(res.getUnitPrice() * res.getQty());
            }
            results.add(res);
        }
        return results;
    }


}
