package io.coomat.shallnotpass.util;

import com.scottyab.aescrypt.AESCrypt;

import org.mindrot.jbcrypt.BCrypt;

import java.security.GeneralSecurityException;

import io.coomat.shallnotpass.config.Constant;

public class EncryptUtil {

    public static Boolean check(String unhashed, String hashed) {
        return BCrypt.checkpw(unhashed, hashed);
    }

    public static String createHash(String unhashed) {
        return BCrypt.hashpw(unhashed, BCrypt.gensalt(12));
    }

    public static String encrypt(String raw) {
        try {
            return AESCrypt.encrypt(Constant.hashedKey, raw);
        } catch (GeneralSecurityException e) {
            System.exit(500);
        }

        return null;
    }

    public static String decrypt(String crypt) {
        try {
            return AESCrypt.decrypt(Constant.hashedKey, crypt);
        } catch (GeneralSecurityException e) {
            System.exit(500);
        }

        return null;
    }
}
