package com.juaracoding.foodspring.model.mapper;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 3:40 PM
@Last Modified 8/27/2023 3:40 PM
Version 1.0
*/

import com.juaracoding.foodspring.dto.CustomerDetails;
import com.juaracoding.foodspring.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "last_name", source = "user.lastName")
    @Mapping(target = "first_name", source = "user.firstName")
    CustomerDetails toCustomerDetails(User user);
}
