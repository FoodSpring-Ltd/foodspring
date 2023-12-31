package com.juaracoding.foodspring.dto;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 9:18 PM
@Last Modified 8/20/2023 9:18 PM
Version 1.0
*/

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {

    private Long discountId;

    @NotBlank(message = "discount name can't be blank")
    private String discountName;

    @NotNull(message = "Start date can't be null")
    private LocalDateTime startAt;

    @NotNull(message = "End date can't be null")
    private LocalDateTime endAt;

    @NotNull(message = "Percent Discount can't be null")
    private Float percentDiscount;
}
