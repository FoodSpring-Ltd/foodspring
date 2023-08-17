package com.juaracoding.foodspring.config;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 7:50 AM
@Last Modified 8/16/2023 7:50 AM
Version 1.0
*/

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {



    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("foodspring-public")
                .packagesToScan("com.juaracoding.foodspring.controller")
                .pathsToMatch("/**")
                .build();
    }


    @Bean
    public OpenAPI springShopOpenAPI() {
        var contact = new Contact();
        contact.setEmail("hakimamarullah@gmail.com");
        contact.setName("Hakim Amarullah");
        return new OpenAPI()
                .info(new Info().title("FoodSpring API")
                        .contact(contact)
                        .description("Spring JPA")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }


}