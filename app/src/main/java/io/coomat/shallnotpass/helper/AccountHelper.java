package io.coomat.shallnotpass.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.coomat.shallnotpass.model.Account;
import io.coomat.shallnotpass.util.EncryptUtil;

public class AccountHelper {

    private String DATA = "data";
    private SharedPreferences sharedPref;

    public AccountHelper(Context context) {
        sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
    }

    public List<Account> addAccount(Account account) {
        SharedPreferences.Editor editor = sharedPref.edit();

        account.setUsername(EncryptUtil.encrypt(account.getUsername()));
        account.setPassword(EncryptUtil.encrypt(account.getPassword()));

        editor.putString(account.getUuid(), accountToJson(account));
        editor.commit();

        return getAllAccounts();
    }

    public List<Account> addRawAccounts(List<Account> rawAccounts) {
        SharedPreferences.Editor editor = sharedPref.edit();

        if (rawAccounts == null || rawAccounts.isEmpty()) return rawAccounts;

        for (Account rawAccount : rawAccounts) {
            editor.putString(rawAccount.getUuid(), accountToJson(rawAccount));
            editor.commit();
        }

        return getAllAccounts();
    }

    public List<Account> removeAccount(String uuid) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(uuid);
        editor.commit();

        return getAllAccounts();
    }

    public List<Account> getAllAccounts() {
        Map<String, ?> rawAccounts = sharedPref.getAll();

        List<Account> accounts = new ArrayList<Account>();

        for (Map.Entry<String, ?> rawAccount : rawAccounts.entrySet()) {
            String accountJson = (String) rawAccounts.get(rawAccount.getKey());

            Account account = jsonToAccount(accountJson);

            account.setUsername(EncryptUtil.decrypt(account.getUsername()));
            account.setPassword(EncryptUtil.decrypt(account.getPassword()));

            accounts.add(account);
        }

        if (accounts.size() > 0) {
            Collections.sort(accounts, new Comparator<Account>() {
                @Override
                public int compare(final Account a1, final Account a2) {
                    return a1.getSite().compareTo(a2.getSite());
                }
            });
        }

        return accounts;
    }

    public List<Account> getAllAcountsRaw() {
        Map<String, ?> rawAccounts = sharedPref.getAll();

        List<Account> accounts = new ArrayList<Account>();

        for (Map.Entry<String, ?> rawAccount : rawAccounts.entrySet()) {
            String accountJson = (String) rawAccounts.get(rawAccount.getKey());

            Account account = jsonToAccount(accountJson);

            accounts.add(account);
        }

        if (accounts.size() > 0) {
            Collections.sort(accounts, new Comparator<Account>() {
                @Override
                public int compare(final Account a1, final Account a2) {
                    return a1.getSite().compareTo(a2.getSite());
                }
            });
        }

        return accounts;
    }

    private String accountToJson(Account account) {
        Gson gson = new Gson();

        return gson.toJson(account);
    }

    private Account jsonToAccount(String accountJson) {
        Gson gson = new Gson();

        Account account = gson.fromJson(accountJson, Account.class);

        return account;
    }

}
