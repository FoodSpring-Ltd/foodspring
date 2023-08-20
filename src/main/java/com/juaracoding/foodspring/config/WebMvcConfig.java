package com.juaracoding.foodspring.config;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/19/2023 11:24 AM
@Last Modified 8/19/2023 11:24 AM
Version 1.0
*/


import com.juaracoding.foodspring.interceptor.AdminAccessInterceptor;
import com.juaracoding.foodspring.interceptor.BasicLoginInterceptor;
import com.juaracoding.foodspring.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean(value = "AdminAccessInterceptorBean")
    public AdminAccessInterceptor adminAccessInterceptor() {
        return new AdminAccessInterceptor();
    }

    @Bean(value = "BasicLoginInterceptorBean")
    public BasicLoginInterceptor basicLoginInterceptor() {
        return new BasicLoginInterceptor();
    }

    @Bean
    public NoLoginInterceptor noLoginInterceptor() {
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
                        .addPathPatterns("/auth/**");
        registry.addInterceptor(adminAccessInterceptor())
                        .addPathPatterns("/admin/**");
        registry.addInterceptor(basicLoginInterceptor())
                .addPathPatterns("/**");
    }
}

