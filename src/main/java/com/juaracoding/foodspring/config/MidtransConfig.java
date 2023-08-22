package com.juaracoding.foodspring.config;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/22/2023 11:52 AM
@Last Modified 8/22/2023 11:52 AM
Version 1.0
*/

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:midtrans-config.properties")
public class MidtransConfig {


    private static String midtransClientKey;

    private static String snapURL;

    private static String midtransServerKey;

    @Value("${midtrans.client_key}")
    public void setMidtransClientKey(String midtransClientKey) {
        MidtransConfig.midtransClientKey = midtransClientKey;
    }
    @Value("${midtrans.snap_url}")
    public void setSnapURL(String snapURL) {
        MidtransConfig.snapURL = snapURL;
    }
    @Value("${midtrans.server_key}")
    public void setMidtransServerKey(String midtransServerKey) {
        MidtransConfig.midtransServerKey = midtransServerKey;
    }
    public static String getClientKey() {
        return midtransClientKey;
    }

    public static String getSnapURL() {
        return snapURL;
    }

    public static String getMidtransServerKey() {
        return midtransServerKey;
    }

    @Bean
    public Config getSnapConfig() {
        return Config.builder()
                .setServerKey(midtransServerKey)
                .setClientKey(midtransClientKey)
                .setIsProduction(false)
                .build();
    }

    @Bean
    public MidtransSnapApi getSnapAPI() {
        return new ConfigFactory(getSnapConfig()).getSnapApi();
    }
}
