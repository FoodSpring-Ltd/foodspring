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

import com.foodspring.utils.CurrencyFormatter;
import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.dto.CartItemDTO;
import com.juaracoding.foodspring.dto.CartItemResponse;
import com.juaracoding.foodspring.dto.CartResponse;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.*;
import com.juaracoding.foodspring.model.mapper.CartItemMapper;
import com.juaracoding.foodspring.repository.*;
import com.juaracoding.foodspring.utils.ConstantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        Cart userCart = new Cart();
        try {
            CartItem cartItem = new CartItem();
            CompletableFuture<Optional<Variant>> variant = cartItemDTO.getVariantId() != null
                    ? CompletableFuture.supplyAsync(() -> variantRepository.findById(cartItemDTO.getVariantId()))
                    : CompletableFuture.supplyAsync(() -> variantRepository.findFirstByProductProductId(cartItemDTO.getProductId()));
            CompletableFuture<Optional<Product>> product = CompletableFuture.supplyAsync(() -> productRepository.findById(cartItemDTO.getProductId()));
            CompletableFuture<Optional<User>> user = CompletableFuture.supplyAsync(() -> userRepository.findById(userId));
            CompletableFuture<Cart> persistedCart = CompletableFuture.supplyAsync(() -> cartRepository.findByUserUserId(userId));

            CompletableFuture.allOf(variant, product, user, persistedCart).join();

            if (product.get().isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.FAILED_ADD_TO_CART_PRODUCT,
                        HttpStatus.BAD_REQUEST, null, "FS0004", request);
            }

            if (user.get().isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.UNAUTHORIZED, null, "FS0004", request);
            }
            if (product.get().isPresent()) {
                if (product.get().get().getIsDelete() || !product.get().get().getIsAvailable()){
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.PRODUCT_NOT_AVAILABLE,
                            HttpStatus.OK, null, "FS0004", request);
                }
            }
            if (variant.get().isPresent()) {
                cartItem.setVariant(variant.get().get());
            }
            if (Objects.isNull(persistedCart.get())) {
                userCart.setUser(user.get().get());
                cartRepository.save(userCart);
            } else {
                userCart = persistedCart.get();
            }
            Product productRes = product.get().get();
            Variant variantRes = variant.get().get();

            CartItem cartItemPersisted = cartItemRepository.findByProductIdAndVariantIdAndCartId(productRes.getProductId(), variantRes.getVariantId(), userCart.getCartId());

            if(!Objects.isNull(cartItemPersisted)) {
                String oldNote = cartItemPersisted.getNote();
                cartItemPersisted.setQty(cartItemPersisted.getQty() + cartItemDTO.getQty());
                String newNote = null;
                if (!Objects.isNull(oldNote)) {
                    newNote = oldNote;
                }
                if(!Objects.isNull(cartItemDTO.getNote())) {
                    if (!Objects.isNull(newNote)) {
                        newNote = newNote.concat(". ").concat(cartItemDTO.getNote());
                    }
                }
                cartItemPersisted.setNote(newNote);
                cartItemRepository.save(cartItemPersisted);
            } else {
                cartItem.setQty(cartItemDTO.getQty());
                cartItem.setCart(userCart);
                cartItem.setProduct(product.get().get());
                cartItem.setNote(cartItemDTO.getNote());
                cartItemRepository.save(cartItem);
            }

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
            cartItemsResponses = CartItemMapper.INSTANCE.toCartItemResponseList(userCart.getCartItems());
            cartResponse.setCartItems(cartItemsResponses);
            double grandTotal = cartItemsResponses.stream().mapToDouble(CartItemResponse::getTotalPrice).sum();
            cartResponse.setGrandTotal(grandTotal);
            cartResponse.setGrandTotalIDR(CurrencyFormatter.toRupiah(grandTotal));
        } catch (Exception ex) {
            strExceptionArr[1] = "getAllCartItem(WebRequest request) -- LINE 103";
            ex.printStackTrace();
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

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateCartItemVariant(Long itemId, Long variantId, WebRequest request) {
        try {
            CompletableFuture<Optional<CartItem>> cartItem = CompletableFuture.supplyAsync(
                    () -> cartItemRepository.findById(itemId)
            );
            CompletableFuture<Optional<Variant>> newVariant = CompletableFuture.supplyAsync(
                    () -> variantRepository.findById(variantId)
            );
            CompletableFuture.allOf(cartItem, newVariant).join();
            if (cartItem.get().isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CART_ITEM_NOT_FOUND,
                        HttpStatus.BAD_REQUEST, null, "FS0005", request);
            }
            if (newVariant.get().isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_VARIANT_NOT_FOUND,
                        HttpStatus.BAD_REQUEST, null, "FS0005", request);
            }
            Product productRes = cartItem.get().get().getProduct();
            Cart userCart = cartItem.get().get().getCart();
            CartItem cartItemPersisted = cartItemRepository.findByProductIdAndVariantIdAndCartId(productRes.getProductId(), variantId, userCart.getCartId());

            if(!Objects.isNull(cartItemPersisted)) {
                String oldNote = cartItemPersisted.getNote();
                cartItemPersisted.setQty(cartItemPersisted.getQty() + cartItem.get().get().getQty());
                String newNote = null;
                if (!Objects.isNull(oldNote)) {
                    newNote = oldNote.concat(". ").concat(cartItem.get().get().getNote());
                } else {
                    newNote = cartItem.get().get().getNote();
                }
                cartItemPersisted.setNote(Objects.isNull(newNote) ? null : newNote.trim());
                cartItemRepository.save(cartItemPersisted);
                cartItemRepository.deleteById(cartItem.get().get().getCartItemId());
            } else {
                cartItem.get().get().setVariant(newVariant.get().get());
                cartItemRepository.save(cartItem.get().get());
            }

        } catch (Exception ex) {
            strExceptionArr[1] = "updateCartItemVariant(Long itemId, Long variantId, WebRequest request) --LINE 163";
            LoggingFile.exceptionString(strExceptionArr, ex, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_UPDATE_ITEM_VARIANT,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FS0005", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.CART_ITEM_VARIANT_UPDATED,
                HttpStatus.OK, null, null, request);
    }

    @Transactional
    public Map<String, Object> updateCartItemQuantity(Long itemId, Integer amount, WebRequest request) {
        Optional<CartItem> cartItem = cartItemRepository.findById(itemId);
        if (cartItem.isEmpty()) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CART_ITEM_NOT_FOUND,
                    HttpStatus.NOT_FOUND, null, null, request);
        }
        cartItem.get().setQty(cartItem.get().getQty() + amount);
        if (cartItem.get().getQty() == 0) {
            cartItemRepository.deleteById(cartItem.get().getCartItemId());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_DELETE_CART_ITEM,
                    HttpStatus.OK, null, null, request);
        }
        cartItemRepository.save(cartItem.get());
        return new ResponseHandler().generateModelAttribut(ConstantMessage.QUANTITY_INCREMENTED_BY.concat(" ").concat(String.valueOf(amount)),
                HttpStatus.OK, null, null, request);
    }
}
