package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/17/2023 1:15 PM
@Last Modified 8/17/2023 1:15 PM
Version 1.0
*/

import com.juaracoding.foodspring.utils.ConstantMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ForgetPasswordDTO {

    /*
        validasi field newPassword not null not empty
        length max 25 min 8
     */
    @NotBlank(message = ConstantMessage.ERROR_NEW_PASSWORD_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_NEW_PASSWORD_MAX_MIN_LENGTH, max = 25,min = 8)
    private String newPassword;

    /*
        validasi field email not null not empty
        length max 25 min 8
     */
    @NotBlank(message = ConstantMessage.ERROR_CONFIRM_PASSWORD_IS_EMPTY)
    @Length(message = ConstantMessage.ERROR_CONFIRM_PASSWORD_MAX_MIN_LENGTH, max = 25,min = 8)
    private String confirmPassword;
    private String email;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getOldPassword() {
//        return oldPassword;
//    }
//
//    public void setOldPassword(String oldPassword) {
//        this.oldPassword = oldPassword;
//    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
