package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 8:26 AM
@Last Modified 8/16/2023 8:26 AM
Version 1.0
*/

import com.juaracoding.foodspring.config.AppConfig;
import com.juaracoding.foodspring.core.SMTPCore;

public class ExecuteSMTP {

    StringBuilder stringBuilder = new StringBuilder();
    private String [] strException = new String[2];

    public ExecuteSMTP() {
        strException[0] = "ExecuteSMTP";
    }

    public Boolean sendSMTPToken(String mailAddress, String subject, String [] strVerification,String pathFile)
    {
        try
        {
            if(AppConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !mailAddress.equals(""))
            {
                String strContent = new ReadTextFileSB(pathFile).getContentFile();
                strContent = strContent.replace("#JKVM3NH",strVerification[0]);//Kepentingan
                strContent = strContent.replace("XF#31NN",strVerification[1]);//Nama Lengkap
                strContent = strContent.replace("8U0_1GH$",strVerification[2]);//TOKEN

                String [] strEmail = {mailAddress};
                SMTPCore sc = new SMTPCore();
                return  sc.sendMailWithAttachment(strEmail,
                        subject,
                        strContent,
                        "SSL",null);
            }
        }
        catch (Exception e)
        {
            strException[1]="sendToken(String mailAddress, String subject, String purpose, String token) -- LINE 38";
            LoggingFile.exceptionString(strException,e,AppConfig.getFlagLogging());
            return false;
        }
        return true;
    }
}
