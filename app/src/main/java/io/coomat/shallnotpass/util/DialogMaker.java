package io.coomat.shallnotpass.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import io.coomat.shallnotpass.R;
import io.coomat.shallnotpass.model.Account;
import io.coomat.shallnotpass.model.CallBack;

public class DialogMaker {

    public void showMsgDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showInputDialog(Context context, int inputType, String title, String positiveBtn, String cancelBtn, @NonNull final CallBack<String> callback) {
        if (TextUtils.isEmpty(positiveBtn)) positiveBtn = "OK";
        if (TextUtils.isEmpty(cancelBtn)) cancelBtn = "Cancel";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        final EditText inputContent = new EditText(context);
        inputContent.setInputType(inputType);
        builder.setView(inputContent);

        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.fire(inputContent.getText().toString());
            }
        });

        builder.setNegativeButton(cancelBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void showAccountDialog(Context context, Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView siteText = new TextView(context);
        siteText.setText("Site: " + account.getSite());
        siteText.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        final TextView usernameText = new TextView(context);
        usernameText.setText("Username: " + account.getUsername());
        usernameText.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        final TextView passwordText = new TextView(context);
        passwordText.setText("Password: " + account.getPassword());
        passwordText.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        layout.addView(siteText);
        layout.addView(usernameText);
        layout.addView(passwordText);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void showAddUserDialog(Context context, String title, String positiveBtn, String cancelBtn, @NonNull final CallBack<Account> callback) {
        if (TextUtils.isEmpty(positiveBtn)) positiveBtn = "OK";
        if (TextUtils.isEmpty(cancelBtn)) cancelBtn = "Cancel";

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary));

        final EditText siteText = new EditText(context);
        siteText.setHint(R.string.site);
        siteText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        siteText.setHintTextColor(context.getResources().getColor(R.color.colorPrimary));
        siteText.setBackgroundTintList(colorStateList);

        final EditText usernameText = new EditText(context);
        usernameText.setHint(R.string.username);
        usernameText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        usernameText.setHintTextColor(context.getResources().getColor(R.color.colorPrimary));
        usernameText.setBackgroundTintList(colorStateList);

        final EditText passwordText = new EditText(context);
        passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordText.setHint(R.string.password);
        passwordText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        passwordText.setHintTextColor(context.getResources().getColor(R.color.colorPrimary));
        passwordText.setBackgroundTintList(colorStateList);

        layout.addView(siteText);
        layout.addView(usernameText);
        layout.addView(passwordText);

        builder.setView(layout);

        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String site = siteText.getText().toString();
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                Account account = new Account(site, username, password);

                callback.fire(account);
            }
        });

        builder.setNegativeButton(cancelBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void showConfirmDialog(Context context, String title, String message, String positiveBtn, String noBtn, String cancelBtn, @NonNull final CallBack<Boolean> callback) {
        if (TextUtils.isEmpty(positiveBtn)) positiveBtn = "YES";
        if (TextUtils.isEmpty(noBtn)) noBtn = "NO";

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        if (!TextUtils.isEmpty(title)) builder.setTitle(title);

        builder.setMessage(message);

        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.fire(true);
            }
        });

        if (cancelBtn != null) {
            builder.setNeutralButton(cancelBtn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        builder.setNegativeButton(noBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.fire(false);
            }
        });

        builder.show();
    }
}
