package com.juaracoding.foodspring.config;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 7:54 AM
@Last Modified 8/16/2023 7:54 AM
Version 1.0
*/

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.juaracoding.foodspring.core.Crypto;
import com.juaracoding.foodspring.utils.MappingAttribute;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class MainConfig {

    @Autowired
    private Environment environment;

    @Primary
    @Bean
    public DataSource getDatasource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(environment.getProperty("spring.datasource.driverClassName"));
        dataSourceBuilder.url(environment.getProperty("spring.datasource.url"));
        dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(Crypto.performDecrypt(environment.getProperty("spring.datasource.password")));
        return dataSourceBuilder.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MappingAttribute mappingAttribute() {
        return new MappingAttribute();
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getProperty("cloudinary.cloud_name"),
                "api_key", environment.getProperty("cloudinary.api.key"),
                "api_secret", environment.getProperty("cloudinary.api.secret")
        ));
        return cloudinary;
    }
}
