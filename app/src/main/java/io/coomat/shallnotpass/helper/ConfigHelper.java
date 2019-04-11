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

    public Boolean isRegistered() {
        String masterKey = sharedPref.getString(KEY_ENTRY, null);

        if (StringUtils.isEmpty(masterKey)) return false;

        return true;
    }

    public void createKey(String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String hash = EncryptUtil.createHash(password);
        editor.putString(KEY_ENTRY, hash);
        editor.apply();
    }


}
