package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/23/2023 9:15 PM
@Last Modified 8/23/2023 9:15 PM
Version 1.0
*/

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String toRupiah(Double price) {
        Locale myIndonesianLocale = new Locale("in", "ID");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(myIndonesianLocale);
        return formatter.format(price)
                .replace(",00", "")
                .replace("Rp", "")
                .trim();
    }
}
