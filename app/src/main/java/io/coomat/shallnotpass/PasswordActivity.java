package io.coomat.shallnotpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import io.coomat.shallnotpass.adapter.AccountDataAdapter;
import io.coomat.shallnotpass.helper.AccountHelper;
import io.coomat.shallnotpass.helper.AccountTransferHelper;
import io.coomat.shallnotpass.model.Account;
import io.coomat.shallnotpass.model.AccountTransferType;
import io.coomat.shallnotpass.model.CallBack;
import io.coomat.shallnotpass.util.DialogMaker;
import io.coomat.shallnotpass.util.StringUtils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

public class PasswordActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private Context context;

    @BindView(R.id.tableView)
    public TableView<Account> tableView;

    @BindView(R.id.searchBar)
    public MaterialSearchBar searchBar;

    private DialogMaker dialogMaker = new DialogMaker();
    private AccountHelper accountHelper;
    private AccountTransferHelper accountTransferHelper;

    private final String[] AccountsHeader = { "Site", "Username", "Password" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        context = this;

        accountHelper = new AccountHelper(context);
        accountTransferHelper = new AccountTransferHelper(context);

        searchBar.setOnSearchActionListener(this);

        setTableConfigs();
    }

    private void setTableConfigs() {
        TableColumnWeightModel colModel = new TableColumnWeightModel(3);
        colModel.setColumnWeight(1, 1);
        colModel.setColumnWeight(2, 1);
        colModel.setColumnWeight(3,1);
        tableView.setColumnModel(colModel);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context, AccountsHeader));

        tableView.setDataAdapter(new AccountDataAdapter(context, accountHelper.getAllAccounts()));

        tableView.addDataClickListener(new TableDataClickListener<Account>() {
            @Override
            public void onDataClicked(int rowIndex, Account account) {
                dialogMaker.showAccountDialog(
                        PasswordActivity.this,
                        getLayoutInflater(),
                        account
                );
            }
        });

        tableView.addDataLongClickListener(new TableDataLongClickListener<Account>() {
            @Override
            public boolean onDataLongClicked(int rowIndex, Account account) {
                dialogMaker.showConfirmDialog(
                        PasswordActivity.this,
                        "Remove Account",
                        "Are you sure you would like to remove account?\n\nThis action cannot be undone.",
                        null,
                        null,
                        null,
                        handleDelete(account)
                );

                return true;
            }
        });
    }

    private CallBack<Account> handleAddAccount() {
        return new CallBack<Account>() {
            @Override
            public void fire(Account account) {
                if (account == null || StringUtils.isEmpty(account.getSite()) || StringUtils.isEmpty(account.getPassword())) {
                    Toast.makeText(context, "Site & password are required to create an account", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Account> accounts  = accountHelper.addAccount(account);

                tableView.setDataAdapter(new AccountDataAdapter(context, accounts));
            }
        };
    }

    private CallBack<Boolean> handleDelete(final Account account) {
        return new CallBack<Boolean>() {
            @Override
            public void fire(Boolean data) {
                if (data) {
                    List<Account> accounts = accountHelper.removeAccount(account.getUuid());

                    tableView.setDataAdapter(new AccountDataAdapter(context, accounts));
                }
            }
        };
    }

    private CallBack<String> handleAccountTransfer() {
        return new CallBack<String>() {
            @Override
            public void fire(String data) {
                if (data.equals(AccountTransferType.IMPORT.name())) {
                    List<Account> accounts = accountTransferHelper.importAccounts();
                    tableView.setDataAdapter(new AccountDataAdapter(context, accounts));
                } else {
                    accountTransferHelper.exportAccounts();
                    Toast.makeText(context, "Accounts data has been saved in Downloads folder", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private CallBack<Boolean> handleLogOut() {
        return new CallBack<Boolean>() {
            @Override
            public void fire(Boolean data) {
                if (data) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);

        super.onCreateOptionsMenu(menu);

        return true;
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled) {
            List<Account> accounts = accountHelper.getAllAccounts();
            tableView.setDataAdapter(new AccountDataAdapter(context, accounts));
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        List<Account> accounts = accountHelper.getAccountsFromSearch(text.toString());
        tableView.setDataAdapter(new AccountDataAdapter(context, accounts));
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        //do nothing
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addAccount:
                dialogMaker.showAddUserDialog(
                        PasswordActivity.this,
                        getLayoutInflater(),
                        "Add User",
                        null,
                        null,
                        handleAddAccount()
                );
                break;
            case R.id.accountTransfer:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PasswordActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 200);
                } else {
                    dialogMaker.showAccountTransferDialog(context, handleAccountTransfer());
                }
                break;
            case R.id.logOut:
                dialogMaker.showConfirmDialog(
                        PasswordActivity.this,
                        "Log Out?",
                        "Are you sure you would like to log out?",
                        null,
                        null,
                        null,
                        handleLogOut()
                );
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
