package com.juaracoding.foodspring.model.mapper;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/23/2023 7:58 PM
@Last Modified 8/23/2023 7:58 PM
Version 1.0
*/

import com.foodspring.utils.CurrencyFormatter;
import com.juaracoding.foodspring.dto.CartItemResponse;
import com.juaracoding.foodspring.model.CartItem;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.model.OrderItem;
import com.juaracoding.foodspring.model.Variant;
import com.juaracoding.foodspring.utils.CalcUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "productImg", expression = "java(getImageURL(item))")
    @Mapping(target = "cartItemId", source = "item.cartItemId")
    @Mapping(target = "productName", expression = "java(getProductName(item))")
    @Mapping(target = "unitPrice", expression = "java(getProductPrice(item))")
    @Mapping(target = "unitPriceIDR", expression = "java(toRupiah(getProductPrice(item)))")
    @Mapping(target = "productVariants", expression = "java(getProductVariants(item))")
    @Mapping(target = "variantName", expression = "java(getVariantName(item))")
    @Mapping(target = "discountAmount", expression = "java(getDiscountAmount(item))")
    @Mapping(target = "totalPrice", expression = "java(getTotalPrice(item))")
    @Mapping(target = "totalPriceIDR", expression = "java(toRupiah(getTotalPrice(item)))")
    CartItemResponse toCartItemResponse(CartItem item);


    @Mapping(target = "productName", expression = "java(getProductName(item))")
    @Mapping(target = "productImageURL", expression = "java(getImageURL(item))")
    @Mapping(target = "unitPrice", expression = "java(getProductPrice(item))")
    @Mapping(target = "variant", expression = "java(getVariantName(item))")
    @Mapping(target = "discountPercentage", expression = "java(getDiscountAmount(item))")
    @Mapping(target = "discountName", expression = "java(getDiscountName(item))")
    @Mapping(target = "qty", source = "item.qty")
    @Mapping(target = "note", source = "item.note")
    @Mapping(target = "productCategory", expression = "java(getCategoryName(item))")
    OrderItem toOrderItem(CartItem item);

    default List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems) {
        return cartItems.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getIsAvailable()) // Exclude items with null product
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());
    }

    default List<OrderItem> toOrderItemList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toOrderItem)
                .collect(Collectors.toList());
    }



    default List<Variant> getProductVariants(CartItem item) {
        return !Objects.isNull(item.getProduct()) ? item.getProduct().getVariants() : null;
    }


    default String getImageURL(CartItem item) {
        return !Objects.isNull(item.getProduct()) ? item.getProduct().getImageURL() : null;
    }

    default Double getProductPrice(CartItem item) {
        return !Objects.isNull(item.getProduct()) ? item.getProduct().getPrice() : null;
    }

    default String getProductName(CartItem item) {
        return !Objects.isNull(item.getProduct()) ? item.getProduct().getProductName() : null;
    }

    default String getVariantName(CartItem item) {
        return item.getVariant() != null ? item.getVariant().getName() : "None";
    }

    default Integer getDiscountAmount(CartItem item) {
        if (item.getProduct() != null && item.getProduct().getDiscount() != null && isDiscountApplicable(item.getProduct().getDiscount())) {
            return item.getProduct().getDiscount().getPercentDiscount();
        }
        return null;
    }

    default String getCategoryName(CartItem item) {
        if (item.getProduct() != null && item.getProduct().getCategory() != null) {
            return item.getProduct().getCategory().getName();
        }
        return null;
    }
    default String getDiscountName(CartItem item) {
        if (item.getProduct() != null && item.getProduct().getDiscount() != null && isDiscountApplicable(item.getProduct().getDiscount())) {
            return item.getProduct().getDiscount().getDiscountName();
        }
        return null;
    }

    default Double getTotalPrice(CartItem item) {
        if (item.getProduct() != null) {
            Double productPrice = item.getProduct().getPrice();
            int discountPercent = item.getProduct().getDiscount() != null && isDiscountApplicable(item.getProduct().getDiscount()) ? item.getProduct().getDiscount().getPercentDiscount() : 0;

            double totalPrice = productPrice * item.getQty();
            if (discountPercent > 0) {
                Double discountedPrice = CalcUtils.getDiscountedPrice(productPrice, discountPercent);
                totalPrice = discountedPrice * item.getQty();
                return totalPrice;
            }

            return totalPrice;
        }
        return 0.0;
    }

    default boolean isDiscountApplicable(Discount discount) {
        if (Objects.isNull(discount)) {
            return false;
        }
        boolean isStarted = discount.getStartAt().equals(LocalDateTime.now()) || discount.getStartAt().isBefore(LocalDateTime.now());
        boolean isEnded = discount.getEndAt().isBefore(LocalDateTime.now());
        return isStarted && !isEnded;
    }
    default String toRupiah(Double price) {
        if (price == null) {
            return null;
        }
        return CurrencyFormatter.toRupiah(price);
    }
}
