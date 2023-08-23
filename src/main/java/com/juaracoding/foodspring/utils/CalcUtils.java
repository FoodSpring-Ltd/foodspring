package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 5:02 PM
@Last Modified 8/22/2023 5:02 PM
Version 1.0
*/

public class CalcUtils {

    public static double getDiscountedPrice(Double initialPrice, Float discountPercent) {
        float percentDiscount = discountPercent / 100;
        double deductionAmount = Math.round(initialPrice * percentDiscount);
        double discountedPrice = initialPrice - deductionAmount;
        return discountedPrice;
    }

    public static String getDiscountedPriceIDR(Double initialPrice, Float discountPercent) {
        return CurrencyFormatter.toRupiah(getDiscountedPrice(initialPrice, discountPercent));
    }
}
