package com.juaracoding.foodspring.model.mapper;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 12:40 PM
@Last Modified 8/27/2023 12:40 PM
Version 1.0
*/

import com.foodspring.utils.CurrencyFormatter;
import com.juaracoding.foodspring.dto.InvoiceItem;
import com.juaracoding.foodspring.dto.MidtransItemDetails;
import com.juaracoding.foodspring.dto.OrderItemResponse;
import com.juaracoding.foodspring.model.OrderItem;
import com.juaracoding.foodspring.utils.CalcUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "price", expression = "java(getUnitPrice(item))")
    @Mapping(target = "quantity", source = "item.qty")
    @Mapping(target = "name", source = "item.productName")
    @Mapping(target = "category", source = "item.productCategory")
    @Mapping(target = "id", source = "item.orderItemId")
    @Mapping(target = "brand", ignore = true)
    MidtransItemDetails toMidtransItemDetails(OrderItem item);
    @Mapping(target = "unitPriceIDR", expression =  "java(toRupiah(item.getUnitPrice()))")
    @Mapping(target = "totalPrice", expression = "java(getFinalPrice(item))")
    @Mapping(target = "totalPriceIDR", expression = "java(toRupiah(getFinalPrice(item)))")
    OrderItemResponse toOrderItemResponse(OrderItem item);

    @Mapping(target = "unitPriceIDR", expression =  "java(toRupiah(item.getUnitPrice()))")
    @Mapping(target = "totalPriceIDR", expression = "java(toRupiah(getFinalPrice(item)))")
    @Mapping(target = "discount", source = "item.discountPercentage")
    @Mapping(target = "totalPrice", expression = "java(getFinalPrice(item))")
    InvoiceItem toInvoiceItem(OrderItem item);

    default List<MidtransItemDetails> toMidtransItemDetailsList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toMidtransItemDetails)
                .collect(Collectors.toList());
    }

    default List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    default List<InvoiceItem> toInvoiceItemList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toInvoiceItem)
                .collect(Collectors.toList());
    }

    default Double getFinalPrice(OrderItem item) {
        double price = item.getUnitPrice() * item.getQty();
        if(item.getDiscountPercentage() != null) {
            price  = CalcUtils.getDiscountedPrice(item.getUnitPrice(), item.getDiscountPercentage()) * item.getQty();
        }
        return  price;

    }

    default Integer getUnitPrice(OrderItem item) {
        double price = item.getUnitPrice();
        if(item.getDiscountPercentage() != null) {
            price  = CalcUtils.getDiscountedPrice(item.getUnitPrice(), item.getDiscountPercentage());
        }
        return  (int) price;
    }

    default String toRupiah(Double price) {
        return CurrencyFormatter.toRupiah(price);
    }
}
