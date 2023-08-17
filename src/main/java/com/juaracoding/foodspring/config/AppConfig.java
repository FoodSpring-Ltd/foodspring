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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:app-config.properties")
public class AppConfig {
    private static String flagLogging;//additionForLogging


    private static String flagTestDebug;

    private static String flagBcrypt;

    private static String flagMaxCounter;

    private static String flagSMTPActive;

    private static String flagSessionValidation;

    private static String urlEndPointVerify;

    private static String urlPathVerifyEmail;

    private static String pathThymeleafTemplateReport;
    private static String pathSeparatorReport;
    private static String howToDownloadReport;
    private static String pathTemplateDownload;

    public static String getPathTemplateDownload() {
        return pathTemplateDownload;
    }

    @Value("${path.template.download}")
    private void setPathTemplateDownload(String pathTemplateDownload) {
        AppConfig.pathTemplateDownload = pathTemplateDownload;
    }

    public static String getHowToDownloadReport() {
        return howToDownloadReport;
    }

    @Value("${how.to.download.report}")
    private void setHowToDownloadReport(String howToDownloadReport) {
        AppConfig.howToDownloadReport = howToDownloadReport;
    }

    @Value("${path.separator.report}")
    public static String getPathSeparatorReport() {
        return pathSeparatorReport;
    }

    @Value("${path.separator.report}")
    private void setPathSeparatorReport(String pathSeparatorReport) {
        AppConfig.pathSeparatorReport = pathSeparatorReport;
    }

    public static String getPathThymeleafTemplateReport() {
        return pathThymeleafTemplateReport;
    }

    @Value("${path.thymeleaf.template.report}")
    private void setPathThymeleafTemplateReport(String pathThymeleafTemplateReport) {
        AppConfig.pathThymeleafTemplateReport = pathThymeleafTemplateReport;
    }

    public static String getUrlPathVerifyEmail() {
        return urlPathVerifyEmail;
    }

    @Value("${url.path.verify.email}")
    private void setUrlPathVerifyEmail(String urlPathVerifyEmail) {
        AppConfig.urlPathVerifyEmail = urlPathVerifyEmail;
    }

    public static String getUrlEndPointVerify() {
        return urlEndPointVerify;
    }

    @Value("${url.end.point.verify}")
    private void setUrlEndPointVerify(String urlEndPointVerify) {
        AppConfig.urlEndPointVerify = urlEndPointVerify;
    }

    public static String getFlagSessionValidation() {
        return flagSessionValidation;
    }

    @Value("${flag.session.validation}")
    private void setFlagSessionValidation(String flagSessionValidation) {
        System.out.println(flagSessionValidation);
        AppConfig.flagSessionValidation = flagSessionValidation;
    }

    public static String getFlagSMTPActive() {
        return flagSMTPActive;
    }

    @Value("${flag.smtp.active}")
    private void setFlagSMTPActive(String flagSMTPActive) {
        AppConfig.flagSMTPActive = flagSMTPActive;
    }

    public static String getFlagMaxCounter() {
        return flagMaxCounter;
    }

    @Value("${flag.max.counter.login}")
    private void setFlagMaxCounter(String flagMaxCounter) {
        AppConfig.flagMaxCounter = flagMaxCounter;
    }

    public static String getFlagBcrypt() {
        return flagBcrypt;
    }

    @Value("${flag.bcrypts}")
    private void setFlagBcrypt(String flagBcrypt) {
        AppConfig.flagBcrypt = flagBcrypt;
    }

    public static String getFlagTestDebug() {
        return flagTestDebug;
    }

    @Value("${flag.test.debug}")
    private void setFlagTestDebug(String flagTestDebug) {
        AppConfig.flagTestDebug = flagTestDebug;
    }

    public static String getFlagLogging() {
        return flagLogging;
    }

    @Value("${flag.logging}")
    private void setFlagLogging(String flagLogging) {
        this.flagLogging = flagLogging;
    }
}
