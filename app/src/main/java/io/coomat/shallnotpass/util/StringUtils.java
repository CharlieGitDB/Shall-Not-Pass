package io.coomat.shallnotpass.util;

public class StringUtils {

    /**
     * Checks the content of a string and returns false if the string is null or empty
     */
    public static Boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

}
