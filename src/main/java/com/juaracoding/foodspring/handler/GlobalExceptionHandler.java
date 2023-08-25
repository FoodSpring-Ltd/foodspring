package com.juaracoding.foodspring.handler;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/3/2023 8:24 PM
@Last Modified 8/3/2023 8:24 PM
Version 1.0
*/

import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.AppConfig;
import com.juaracoding.foodspring.utils.ConstantMessage;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Order(0)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private List<ApiValidationError> lsSubError = new ArrayList<ApiValidationError>();
    private String[] strException = new String[2];

    public GlobalExceptionHandler() {
        strException[0] = "GlobalExceptionHandler";
    }


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        lsSubError.clear();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            lsSubError.add(new ApiValidationError(fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue(), fieldError.getObjectName()));
        }

        ApiError apiError =
                new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ConstantMessage.ERROR_UNPROCCESSABLE, ex, request.getDescription(false), "00001");
        apiError.setSubErrors(lsSubError);
        return ResponseEntity.unprocessableEntity().body(apiError);
    }


    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        strException[1] = "handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) -- LINE 54";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ConstantMessage.ERROR_MAIL_FORM_JSON, ex, request.getDescription(false), "00002"));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        strException[1] = "resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) -- LINE 59";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex, request.getDescription(false), "00003"));
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest request) {

        strException[1] = "handleAllUncaughtException(Exception ex, WebRequest request) -- LINE 70";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ConstantMessage.ERROR_INTERNAL_SERVER, ex, request.getDescription(false), "X2013"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorized(Exception ex, WebRequest request) {
        strException[1] = "handleUnauthorized(Exception ex, WebRequest request) -- LINE 89";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ConstantMessage.ERROR_UNAUTHORIZE, ex, request.getDescription(false), "R403"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        strException[1] = "dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) -- LINE 79";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ConstantMessage.ERROR_DATA_INVALID, ex, request.getDescription(false), "X7006"));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        strException[1] = "handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) -- LINE 89";
        LoggingFile.exceptionString(strException, ex, AppConfig.getFlagLogging());
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ConstantMessage.ERROR_UNEXPECTED, ex, request.getDescription(false), "X2236"));
    }
}