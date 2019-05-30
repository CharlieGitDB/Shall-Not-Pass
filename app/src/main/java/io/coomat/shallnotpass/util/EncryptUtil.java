package io.coomat.shallnotpass.util;

import com.scottyab.aescrypt.AESCrypt;

import org.mindrot.jbcrypt.BCrypt;

import java.security.GeneralSecurityException;

import io.coomat.shallnotpass.config.Constant;

public class EncryptUtil {

    /**
     * Check a hashed password against the unhashed password
     */
    public static Boolean check(String unhashed, String hashed) {
        return BCrypt.checkpw(unhashed, hashed);
    }

    /**
     * Create the hash from a string
     */
    public static String createHash(String unhashed) {
        return BCrypt.hashpw(unhashed, BCrypt.gensalt());
    }

    /**
     * Encrypt the string against the Constant.secret static String
     */
    public static String encrypt(String raw) {
        try {
            return AESCrypt.encrypt(Constant.secret, raw);
        } catch (GeneralSecurityException e) {
            System.exit(500);
        }

        return null;
    }

    /**
     * Decrpyt the string against the encrypted string and Constant.secret static String
     */
    public static String decrypt(String crypt) {
        try {
            return AESCrypt.decrypt(Constant.secret, crypt);
        } catch (GeneralSecurityException e) {
            System.exit(500);
        }

        return null;
    }
}
