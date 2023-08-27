package com.juaracoding.foodspring.config;

import com.foodspring.core.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

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

@Configuration
@PropertySource("classpath:mailer-config-local.properties")
public class MailerConfig {
    private static String emailUserName;
    private static String emailPassword;
    private static String emailHost;
    private static String emailPort;
    private static String emailPortSSL;
    private static String emailPortTLS;
    private static String emailAuth;
    private static String emailStartTLSEnable;
    private static String emailSMTPSocketFactoryClass;


    @Value("${email.username}")
    private void setEmailUserName(String emailUserName) {
        this.emailUserName = emailUserName;
    }

    @Value("${email.password}")
    private void setEmailPassword(String emailPassword) {
        this.emailPassword = Crypto.performDecrypt(emailPassword);
    }

    @Value("${email.host}")
    private void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    @Value("${email.port.default}")
    private void setEmailPort(String emailPort) {
        this.emailPort = emailPort;
    }

    @Value("${email.port.ssl}")
    private void setEmailPortSSL(String emailPortSSL) {
        this.emailPortSSL = emailPortSSL;
    }

    @Value("${email.port.tls}")
    private void setEmailPortTLS(String emailPortTLS) {
        this.emailPortTLS = emailPortTLS;
    }

    @Value("${email.auth}")
    private void setEmailAuth(String emailAuth) {
        this.emailAuth = emailAuth;
    }

    @Value("${email.starttls.enable}")
    private void setEmailStartTLSEnable(String emailStartTLSEnable) {
        this.emailStartTLSEnable = emailStartTLSEnable;
    }
    @Value("${email.smtp.socket.factory.class}")
    private void setEmailSMTPSocketFactoryClass(String emailSMTPSocketFactoryClass) {
        this.emailSMTPSocketFactoryClass = emailSMTPSocketFactoryClass;
    }

    public static String getEmailUserName() {
        return emailUserName;
    }

    public static String getEmailPassword() {
        return emailPassword;
    }

    public static String getEmailHost() {
        return emailHost;
    }

    public static String getEmailPort() {
        return emailPort;
    }

    public static String getEmailPortSSL() {
        return emailPortSSL;
    }

    public static String getEmailPortTLS() {
        return emailPortTLS;
    }

    public static String getEmailAuth() {
        return emailAuth;
    }

    public static String getEmailStartTLSEnable() {
        return emailStartTLSEnable;
    }

    public static String getEmailSMTPSocketFactoryClass() {
        return emailSMTPSocketFactoryClass;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
