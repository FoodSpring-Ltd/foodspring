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

import com.juaracoding.foodspring.dto.CartItemResponse;
import com.juaracoding.foodspring.model.CartItem;
import com.juaracoding.foodspring.model.Discount;
import com.juaracoding.foodspring.utils.CalcUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "productImg", source = "item.product.imageURL")
    @Mapping(target = "productName", source = "item.product.productName")
    @Mapping(target = "unitPrice", source = "item.product.price")
    @Mapping(target = "productVariants", source = "item.product.variants")
    @Mapping(target = "variantName", expression = "java(getVariantName(item))")
    @Mapping(target = "discountAmount", expression = "java(getDiscountAmount(item))")
    @Mapping(target = "totalPrice", expression = "java(getTotalPrice(item))")
    CartItemResponse toCartItemResponse(CartItem item);

    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems);

    default String getVariantName(CartItem item) {
        return item.getVariant() != null ? item.getVariant().getName() : "None";
    }

    default Float getDiscountAmount(CartItem item) {
        Discount discount = item.getProduct().getDiscount();
        return discount != null ? discount.getPercentDiscount() : null;
    }

    default Double getTotalPrice(CartItem item) {
        if (item.getProduct().getDiscount() != null) {
            Double discountedPrice = CalcUtils.getDiscountedPrice(item.getProduct().getPrice(), item.getProduct().getDiscount().getPercentDiscount());
            return discountedPrice * item.getQty();
        }
        return item.getProduct().getPrice() * item.getQty();
    }
}
