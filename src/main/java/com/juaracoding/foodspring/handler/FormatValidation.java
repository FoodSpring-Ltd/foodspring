package com.juaracoding.foodspring.handler;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/17/2023 1:45 PM
@Last Modified 8/17/2023 1:45 PM
Version 1.0
*/

import com.juaracoding.foodspring.utils.ConstantMessage;

import java.util.regex.Pattern;

public class FormatValidation {

    public static Boolean emailFormatValidation(String email)
    {
        //^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$
        return Pattern.compile(ConstantMessage.REGEX_EMAIL_STANDARD_RFC5322)
                .matcher(email)
                .matches();
    }

    public static Boolean phoneNumberFormatValidation(String phoneNumber)
    {
        return Pattern.compile(ConstantMessage.REGEX_PHONE)
                .matcher(phoneNumber)
                .matches();
    }

    public static Boolean dateFormatYYYYMMDDValidation(String date)
    {
        return Pattern.compile(ConstantMessage.REGEX_DATE_YYYYMMDD)
                .matcher(date)
                .matches();
    }
}
