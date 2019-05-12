package io.coomat.shallnotpass.helper;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.coomat.shallnotpass.model.Account;

public class AccountTransferHelper {

    private AccountHelper accountHelper;

    public AccountTransferHelper(Context context) {
        accountHelper = new AccountHelper(context);
    }

    public List<Account> importAccounts() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, "shallnotpassaccounts.json");

        StringBuilder rawAccountsFile = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                rawAccountsFile.append(line);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Account>>(){}.getType();
        List<Account> rawAccounts = gson.fromJson(rawAccountsFile.toString(), listType);

        return accountHelper.addRawAccounts(rawAccounts);
    }

    public void exportAccounts() {
        Gson gson = new Gson();

        List<Account> accounts = accountHelper.getAllAcountsRaw();
        String accountsJson = gson.toJson(accounts);

        writeAccountsToStorage(accountsJson);
    }

    private void writeAccountsToStorage(String accountsJson) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, "shallnotpassaccounts.json");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(accountsJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
