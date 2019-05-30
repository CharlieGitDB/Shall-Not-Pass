package io.coomat.shallnotpass.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import io.coomat.shallnotpass.util.EncryptUtil;
import io.coomat.shallnotpass.util.StringUtils;

public class ConfigHelper {

    private String MASTER_KEY = "master";
    private String KEY_ENTRY = "key";

    private SharedPreferences sharedPref;

    public ConfigHelper(Context context) {
        sharedPref = context.getSharedPreferences(MASTER_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Checks if the user has already registered their master account
     */
    public Boolean isRegistered() {
        String masterKey = sharedPref.getString(KEY_ENTRY, null);

        if (StringUtils.isEmpty(masterKey)) return false;

        return true;
    }

    /**
     * Checks if the password they have entered matches their master password
     */
    public Boolean checkPw(String unhashed) {
        String masterKey = sharedPref.getString(KEY_ENTRY, null);

        return EncryptUtil.check(unhashed, masterKey);
    }

    /**
     * Adds the master password to the persisted data
     */
    public void createKey(String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String hash = EncryptUtil.createHash(password);
        editor.putString(KEY_ENTRY, hash);
        editor.apply();
    }


}
