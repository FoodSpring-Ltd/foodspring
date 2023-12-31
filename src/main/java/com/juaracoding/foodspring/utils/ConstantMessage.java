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
    public static final String SUCCESS_FIND_BY = "Success find by ";
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
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String ERROR_PRODUCT_UPDATED = "Product update failed! Please try again";
    public static final String PRODUCT_DELETION_SUCCESS = "Product deleted successfully";
    public static final String ERROR_DELETE_PRODUCT = "Something went wrong, can't delete product";
    public static final String SUCCESS_CREATE_DISCOUNT = "Discount created successfully";
    public static final String ERROR_CREATE_DISCOUNT = "Failed to create discount. Something went wrong";
    public static final String SUCCESS_UPDATE_DISCOUNT = "Discount updated successfully";
    public static final String ERROR_DELETE_DISCOUNT = "Error deleting discount";
    public static final String SUCCESS_DELETE_DISCOUNT = "Success delete discount";
    public static final String ERROR_UPDATE_DISCOUNT = "Discount update failed";
    public static final String INVALID_DATE_RANGE = "Start datetime should be before end datetime";
    public static final String SUCCESS_ADD_TO_CART = "Product added to cart successfully";
    public static final String FAILED_ADD_TO_CART_PRODUCT = "Failed add to cart, product not found";
    public static final String FAILED_ADD_TO_CART_VARIANT = "Failed add to cart, variant not found";
    public static final String FAILED_ADD_TO_CART = "Failed add to cart";
    public static final String FAILED_GET_CART_ITEMS = "Failed fetching cart items data";
    public static final String SUCCESS_FETCH_DATA = "Data retrieval success";
    public static final String SUCCESS_DELETE_DATA = "Data deletion successful";
    public static final String CART_ITEM_VARIANT_UPDATED = "Cart item variant updated successfully";
    public static final String ERROR_UPDATE_ITEM_VARIANT = "Failed to update cart item's variant";
    public static final String ERROR_CART_ITEM_NOT_FOUND = "Cart item doesn't exist";
    public static final String ERROR_VARIANT_NOT_FOUND = "Variant doesn't exist";
    public static final String PRODUCT_NOT_AVAILABLE = "Product is not available";
    public static final String QUANTITY_INCREMENTED_BY = "Quantity updated by ";
    public static final String SUCCESS_DELETE_CART_ITEM = "Cart Item has been deleted";
    public static final String SUCCESS_RETRIEVE_DATA = "Data retrieval success";
    public static final String ERROR_RETRIEVE_DATA = "Failed to retrieve data";
    public static final String ERROR_UNAUTHORIZE = "You're not logged in";
    public static final String ERROR_ACCOUNT_INACTIVE = "Your account is not activated or deleted";
    public static final String ERROR_CREATE_ORDER = "Something went wrong, can't proceed order";
    public static final String CART_EMPTY = "Cart is empty";
    public static final String SUCCESS_CREATE_ORDER = "Order created successfully";
    public static final String ERROR_UPDATE_ORDER_STATUS = "Failed to update order status";
    public static final String SUCCESS_UPDATE_ORDER_STATUS = "Success update order status";
    public static final String ERROR_NO_ORDER_FOUND = "No such shop order found";
    public static final String ORDER_STATUS_NOT_CHANGED = "No transaction status matched condition checking. Order status no updated.";
    public static final String ERROR_INVALID_DATA = "Data invalid";
    public static final String ERROR_TO_PROCEED = "Can't process your request, please try again later";
    public static final String ERROR_CREATE_PAYMENT = "failed to create payment, please try again later";
    public static final String NOT_CRITICAL = "Not Critical";
    public static final String ORDER_STATUS_ALREADY_CHANGED = "Order status has been modified";
    public static final String FAILED_GET_ORDER_REPORT = "Failed to get order count report";
    public static final String ERROR_CREATING_INVOICE = "Failed to generate invoice pdf";
    public static final String SUCCESS_CREATING_INVOICE = "Invoice created successfully";
}

