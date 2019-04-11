package io.coomat.shallnotpass;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import io.coomat.shallnotpass.adapter.AccountDataAdapter;
import io.coomat.shallnotpass.helper.AccountHelper;
import io.coomat.shallnotpass.model.Account;
import io.coomat.shallnotpass.model.CallBack;
import io.coomat.shallnotpass.util.DialogMaker;
import io.coomat.shallnotpass.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class PasswordActivity extends AppCompatActivity {

    private Context context;

    @BindView(R.id.tableView)
    public TableView<Account> tableView;

    private DialogMaker dialogMaker = new DialogMaker();
    private AccountHelper accountHelper;

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
        //TODO: ADD FORM VALIDATION
        //TODO: POSSIBLY REMOVE MULTI LINE EDITING?
        //TODO: ADD MASTER ACCOUNT DELETE (This would delete the master account and all related child accounts)

        return new CallBack<Account>() {
            @Override
            public void fire(Account account) {
                if (account == null || StringUtils.isEmpty(account.getSite()) || StringUtils.isEmpty(account.getUsername()) || StringUtils.isEmpty(account.getPassword())) {
                    Toast.makeText(context, "All inputs are required to create an account", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addAccount:
                dialogMaker.showAddUserDialog(
                        PasswordActivity.this,
                        "Add User",
                        null,
                        null,
                        handleAddAccount()
                );
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
