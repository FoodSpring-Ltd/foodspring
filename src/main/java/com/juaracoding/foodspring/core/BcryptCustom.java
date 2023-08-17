package com.juaracoding.foodspring.core;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/16/2023 8:22 AM
@Last Modified 8/16/2023 8:22 AM
Version 1.0
*/

import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;

public class BcryptCustom {
    private final int logRounds;

    public BcryptCustom(int logRounds) {
        this.logRounds = logRounds;
    }

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        if (BCrypt.checkpw(password, hash)) {
            int intRounds = getRounds(hash);
//            System.out.println("ROUNDS VALUE -> "+intRounds);
            if (intRounds == 0) {
                return false;
            }
            if (intRounds != logRounds) {
                String newHash = hash(password);
                return updateFunc.apply(newHash);
            }
            return true;
        }
        return false;
    }

    private int getRounds(String salt) {
        char minor = (char) 0;
        int off = 0;

        if (salt.charAt(0) != '$' || salt.charAt(1) != '2') {
//            System.out.println("Salt Version not valid");
            return 0;
        }
        if (salt.charAt(2) == '$') {
            off = 3;
        } else {
            minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$') {
//                System.out.println("Salt Version not valid");
                return 0;
            }
            off = 4;
        }
        if (salt.charAt(off + 2) > '$') {
//            System.out.println("Salt rounds are  Missing");
            return 0;
        }
        return Integer.parseInt(salt.substring(off, off + 2));
    }
}