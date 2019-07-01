package io.coomat.shallnotpass.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import io.coomat.shallnotpass.R;
import io.coomat.shallnotpass.model.Account;
import io.coomat.shallnotpass.model.AccountTransferType;
import io.coomat.shallnotpass.model.CallBack;

public class DialogMaker {

    /**
     * Create a dialog that is an alert displaying a message
     */
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

    /**
     * Create a dialog that gives the user an option of two choices
     */
    public void showChoiceDialog(Context context, String title, String message, @NonNull final String option1, @NonNull final String option2, String cancelBtn, @NonNull final CallBack<String> callback) {
        if (TextUtils.isEmpty(cancelBtn)) cancelBtn = "Cancel";

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        if (!TextUtils.isEmpty(title)) builder.setTitle(title);

        builder.setMessage(message);

        builder.setPositiveButton(option2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.fire(option2);
            }
        });

        builder.setNegativeButton(option1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.fire(option1);
            }
        });

        builder.setNeutralButton(cancelBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    /**
     * Create a dialog that takes input from the user
     */
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

    /**
     * Create a dialog that displays the account details of the selected account
     */
    public void showAccountDialog(final Context context, LayoutInflater layoutInflator, final Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view = layoutInflator.inflate(R.layout.account_overview_dialog, null);

        final TextView siteText = view.findViewById(R.id.siteTextView);
        final TextView usernameText = view.findViewById(R.id.usernameTextView);
        final TextView passwordText = view.findViewById(R.id.passwordTextView);

        siteText.setText(account.getSite());
        usernameText.setText(account.getUsername());
        passwordText.setText(account.getPassword());

        Button copyUsername = view.findViewById(R.id.copyUsername);
        Button copyPassword = view.findViewById(R.id.copyPassword);

        copyUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtil.copyTextToClipBoard(context, "Username", account.getUsername());
            }
        });

        copyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtil.copyTextToClipBoard(context, "Password", account.getPassword());
            }
        });

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    /**
     * Create a dialog that allows the user to add an account
     */
    public void showAddUserDialog(Context context, LayoutInflater layoutInflator, String title, String positiveBtn, String cancelBtn, @NonNull final CallBack<Account> callback) {
        if (TextUtils.isEmpty(positiveBtn)) positiveBtn = "OK";
        if (TextUtils.isEmpty(cancelBtn)) cancelBtn = "Cancel";

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);

        View view = layoutInflator.inflate(R.layout.account_create_dialog, null);

        final EditText siteText = view.findViewById(R.id.editTextSite);
        final EditText usernameText = view.findViewById(R.id.editTextUsername);
        final EditText passwordText = view.findViewById(R.id.editTextPassword);
        Button generatePasswordBtn = view.findViewById(R.id.generatePassword);

        generatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String safePassword = EncryptUtil.generateSafePassword();
                passwordText.setText(safePassword);
            }
        });

        builder.setView(view);

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

    /**
     * Creates a dialog that allows the user to "transfer" their accounts.  They can import from a JSON file or save a JSON file of accounts
     */
    public void showAccountTransferDialog(Context context, @NonNull final CallBack<String> callback) {
        showChoiceDialog(context, "Account Transfer", "Would you like to import accounts or export accounts?", AccountTransferType.IMPORT.name(), AccountTransferType.EXPORT.name(), null, callback);
    }

    /**
     * Creates a dialog that prompts the user for a response
     */
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
