package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/17/2023 1:27 PM
@Last Modified 8/17/2023 1:27 PM
Version 1.0
*/

import com.juaracoding.foodspring.utils.ConstantMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserDTO {


    private Long userId;

    @NotBlank(message = ConstantMessage.ERROR_EMAIL_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_EMAIL_MAX_MIN_LENGTH, min = 15, max = 50)
    private String email;

    @NotBlank(message = ConstantMessage.ERROR_USRNAME_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_USRNAME_MAX_MIN_LENGTH, min = 10, max = 30)
    private String username;

    @NotBlank(message = ConstantMessage.ERROR_PASSWORD_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_PASSWORD_MAX_MIN_LENGTH, min = 8, max = 25)
    private String password;


    @NotBlank(message = ConstantMessage.ERROR_FIRSTNAME_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_FIRSTNAME_MAX_MIN_LENGTH, min = 8, max = 40)
    private String firstName;
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")//masih miss validasi nya
    private LocalDate birthDay;


    @NotBlank(message = ConstantMessage.ERROR_PHONE_IS_BLANK)
    private String phone;
    private String token;
}