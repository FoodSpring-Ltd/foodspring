package com.juaracoding.foodspring.utils;

import com.foodspring.utils.CurrencyFormatter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcUtilsTest {

    @Test
    void testGetDiscountedPrice() {
        double initialPrice = 100.0;
        int discountPercent = 20;

        double discountedPrice = CalcUtils.getDiscountedPrice(initialPrice, discountPercent);

        assertEquals(80.0, discountedPrice);
    }

    @Test
    void testGetDiscountedPriceIDR() {
        double initialPrice = 100.0;
        int discountPercent = 20;
        String expectedFormattedPrice = "80";

        String discountedPriceIDR = CalcUtils.getDiscountedPriceIDR(initialPrice, discountPercent);

        assertEquals(expectedFormattedPrice, discountedPriceIDR);
    }

    @Test
    void testGetDiscountedPriceIDR2() {
        try(MockedStatic<CurrencyFormatter> currencyFormatter = Mockito.mockStatic(CurrencyFormatter.class)) {
            currencyFormatter.when(() -> CurrencyFormatter.toRupiah(20000.0)).thenReturn("20.000");
            assertEquals("20.000", CurrencyFormatter.toRupiah(20000.0));
        }

    }
}

