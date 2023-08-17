package com.juaracoding.foodspring.core;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 8:21 AM
@Last Modified 8/16/2023 8:21 AM
Version 1.0
*/

import java.util.function.Function;

public class BcryptImpl {

    private static final BcryptCustom bcrypt = new BcryptCustom(11);

    public static String hash(String password) {
        return bcrypt.hash(password);
    }

    public static boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
    }

    public static boolean verifyHash(String password, String hash) {
        return bcrypt.verifyHash(password, hash);
    }

    public static void main(String[] args) {
//        String[] mutableHash = new String[1];
//        Function<String, Boolean> update = hash -> { mutableHash[0] = hash; return true; };

        String strUserName = "Paul";
        String strPwd = "Paul123";
        String unamePwd = strUserName + strPwd;
        String strAfterEncrypt1 = hash(unamePwd);///PaulPaul123
        String strAfterEncrypt2 = hash(unamePwd);
        System.out.println(strAfterEncrypt1);
        System.out.println(strAfterEncrypt2);
    }
}
