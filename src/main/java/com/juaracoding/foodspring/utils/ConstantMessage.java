package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 8:39 AM
@Last Modified 8/16/2023 8:39 AM
Version 1.0
*/

public class ConstantMessage {

    public final static String REGEX_DATE_YYYYMMDD = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";

    public final static String REGEX_DATE_DDMMYY = "";
    /*
        REGISTER
     */
    public final static String REGIS_USRNAME_NOT_EMPTY = "Username tidak boleh kosong";
    public final static String REGIS_PWD_NOT_EMPTY = "Password tidak boleh kosong";
    public final static String REGIS_EMAIL_NOT_EMPTY = "Email tidak boleh kosong";
    public final static String REGIS_NO_HP_NOT_EMPTY = "No HP tidak boleh kosong";

    public final static String ERROR_UNEXPECTED = "UNEXPECTED ERROR";
    public final static String ERROR_UNPROCCESSABLE = "Validation error. Check 'errors' field for details.";
    public final static String ERROR_NO_CONTENT = "PERMINTAAN TIDAK DAPAT DIPROSES";
    public final static String ERROR_DATA_INVALID = "DATA TIDAK VALID";
    public final static String ERROR_INTERNAL_SERVER = "INTERNAL SERVER ERROR";
    public final static String ERROR_MAIL_FORM_JSON = "Malformed JSON request";

    public final static  String ERROR_FAILED_CREATED = "Error created";
    public static final String SUCCESS_CREATED = "Data created successfully";

    public static final String ERROR_EMAIL_ISEXIST = "Email already used" ;
    public static final String ERROR_PHONE_ISEXIST = "Phone already used";
    public static final String ERROR_USERNAME_ISEXIST = "Username already used";
    public static final String ERROR_USER_ISACTIVE = "User is active";
    public static final String ERROR_FLOW_INVALID = "Error invalid flow";
    public static final String SUCCESS_CHECK_REGIS = "Registration check success";
    public static final String ERROR_TOKEN_INVALID = "Token invalid";
    public static final String ERROR_USER_NOT_EXISTS = "User doesn't exist";
    public static final String ERROR_LOGIN_FAILED = "Login failed";
    public static final String SUCCESS_LOGIN = "Login success";
    public static final String SUCCESS_SEND_NEW_TOKEN = "New token has been sent";
    public static final String ERROR_NEW_PASSWORD_IS_EMPTY = "New Password can't be empty";
    public static final String ERROR_NEW_PASSWORD_MAX_MIN_LENGTH = "Password max 25 and min 8";
    public static final String ERROR_CONFIRM_PASSWORD_IS_EMPTY = "ERROR_CONFIRM_PASSWORD_IS_EMPTY";
    public static final String ERROR_CONFIRM_PASSWORD_MAX_MIN_LENGTH = "ERROR_CONFIRM_PASSWORD_MAX_MIN_LENGTH";
    public static final String ERROR_TOKEN_FORGOTPWD_NOT_SAME = "ERROR_TOKEN_FORGOTPWD_NOT_SAME";
    public static final String SUCCESS_TOKEN_MATCH = "SUCCESS_TOKEN_MATCH";
    public static final String ERROR_PASSWORD_CONFIRM_FAILED = "ERROR_PASSWORD_CONFIRM_FAILED";
    public static final String SUCCESS_CHANGE_PWD = "SUCCESS_CHANGE_PWD";
    public static final String SUCCESS_SAVE = "SUCCESS_SAVE";
    public static final String ERROR_SAVE_FAILED = "ERROR_SAVE_FAILED";
    public static final String SUCCESS_UPDATE = "SUCCESS_UPDATE";
    public static final String ERROR_EMPTY_FILE = "ERROR_EMPTY_FILE";
    public static final String ERROR_EMAIL_IS_EMPTY = "ERROR_EMAIL_IS_EMPTY";
    public static final String ERROR_EMAIL_MAX_MIN_LENGTH = "ERROR_EMAIL_MAX_MIN_LENGTH";
    public static final String ERROR_USRNAME_IS_EMPTY = "ERROR_USRNAME_IS_EMPTY";
    public static final String ERROR_USRNAME_MAX_MIN_LENGTH = "ERROR_USRNAME_MAX_MIN_LENGTH ";
    public static final String ERROR_PASSWORD_IS_EMPTY = "ERROR_PASSWORD_IS_EMPTY";
    public static final String ERROR_PASSWORD_MAX_MIN_LENGTH = "ERROR_PASSWORD_MAX_MIN_LENGTH" ;
    public static final String ERROR_FIRSTNAME_IS_EMPTY = "ERROR_FIRSTNAME_IS_EMPTY";
    public static final String ERROR_FIRSTNAME_MAX_MIN_LENGTH = "ERROR_FIRSTNAME_MAX_MIN_LENGTH";
    public static final String ERROR_PHONE_IS_BLANK = "ERROR_PHONE_IS_BLANK";
    public static final String WARNING_DATA_EMPTY = "WARNING_DATA_EMPTY";
    public static final String SUCCESS_FIND_BY = "SUCCESS_FIND_BY";
    public static final String USER_IS_ACTIVE = "USER_IS_ACTIVE";
    public static final String VERIFY_LINK_VALID = "VERIFY_LINK_VALID";
    public static final String WARNING_MENU_NOT_EXISTS = "WARNING_MENU_NOT_EXISTS";
    public static final String REGEX_PHONE = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    public final static String REGEX_EMAIL_STANDARD_RFC5322  = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static final String ERROR_PHONE_NUMBER_FORMAT_INVALID = "ERROR_PHONE_NUMBER_FORMAT_INVALID";
    public static final String ERROR_EMAIL_FORMAT_INVALID = "ERROR_EMAIL_FORMAT_INVALID";
    public static final String ERROR_TOKEN_IS_EMPTY = "ERROR_TOKEN_IS_EMPTY";
    public static final String THIS_FIELD_CANT_BE_BLANK = "This Field can't be blank";
    public static final String ERROR_CREATING_CATEGORY = "Error creating category";
    public static final String SUCCESS_CREATE_CATEGORY = "Category successfully created";
    public static final String SUCCESS_CREATED_PRODUCT = "Product successfully created";
    public static final String ERROR_CREATING_PRODUCT = "Error creating product, please check the log";
    public static final Object PRODUCT_UPDATED = "Product updated successfully";
    public static final Object ERROR_PRODUCT_UPDATED = "Product update failed! Please try again";
    public static final Object PRODUCT_DELETION_SUCCESS = "Product deleted successfully";
    public static final Object ERROR_DELETE_PRODUCT = "Something went wrong, can't delete product";
    public static final String SUCCESS_CREATE_DISCOUNT = "Discount created successfully";
    public static final String ERROR_CREATE_DISCOUNT = "Failed to create discount. Something went wrong";
    public static final String SUCCESS_UPDATE_DISCOUNT = "Discount updated successfully";
    public static final String ERROR_DELETE_DISCOUNT = "Error deleting discount";
    public static final String SUCCESS_DELETE_DISCOUNT = "Success delete discount";
    public static final Object ERROR_UPDATE_DISCOUNT = "Discount update failed";
    public static final String INVALID_DATE_RANGE = "Start datetime should be before end datetime";
}

