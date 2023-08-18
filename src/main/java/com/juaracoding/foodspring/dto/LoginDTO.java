package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/18/2023 11:08 AM
@Last Modified 8/18/2023 11:08 AM
Version 1.0
*/

import com.juaracoding.foodspring.utils.ConstantMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = ConstantMessage.THIS_FIELD_CANT_BE_BLANK)
    private String credential;

    @NotBlank(message = ConstantMessage.THIS_FIELD_CANT_BE_BLANK)
    private String password;
}
