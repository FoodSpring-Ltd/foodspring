package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 8:20 AM
@Last Modified 8/16/2023 8:20 AM
Version 1.0
*/

import org.apache.log4j.Logger;

public class LoggingFile {

    private static StringBuilder sbuilds = new StringBuilder();
    private static Logger logger = Logger.getLogger(LoggingFile.class);
    public static void exceptionString(String[] datax, Exception e, String flag) {
        if(flag.equals("y"))
        {
            sbuilds.setLength(0);
            logger.info(sbuilds.append(System.getProperty("line.separator")).
                    append("ERROR IN CLASS =>").append(datax[0]).append(System.getProperty("line.separator")).
                    append("METHOD   =>").append(datax[1]).append(System.getProperty("line.separator")).
                    append("ERROR IS       =>").append(e.getMessage()).
                    append(System.getProperty("line.separator")).toString());
            sbuilds.setLength(0);
        }
    }

    public static void exceptionString(String[] datax, Exception e, String flag, String addNotes) {
        if(flag.equals("y"))
        {
            sbuilds.setLength(0);
            logger.info(sbuilds.append(System.getProperty("line.separator")).
                    append("ERROR IN CLASS =>").append(datax[0]).append(System.getProperty("line.separator")).
                    append("METHOD   =>").append(datax[1]).append(System.getProperty("line.separator")).
                    append("ERROR IS       =>").append(e.getMessage()).
                    append("Notes Tambahan       =>").append(addNotes).
                    append(System.getProperty("line.separator")).toString());
            sbuilds.setLength(0);
        }
    }
}
