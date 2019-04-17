package io.coomat.shallnotpass;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import io.coomat.shallnotpass.helper.ConfigHelper;
import io.coomat.shallnotpass.util.EncryptUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {

    private String INCORRECT_PASSWORD = "Incorrect password";

    private Boolean isRegistered;

    @BindView(R.id.introText)
    public TextView introText;

    @Order(1)
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    @BindView(R.id.passwordText)
    public EditText passwordText;

    @Order(2)
    @ConfirmPassword
    @BindView(R.id.confirmPasswordText)
    public EditText confirmPasswordText;

    private Validator validator = new Validator(this);

    private ConfigHelper configHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        configHelper = new ConfigHelper(this);

        validator.setValidationListener(this);

        isRegistered = configHelper.isRegistered();

        if (isRegistered) {
            introText.setText(R.string.intro_registered);
            confirmPasswordText.setVisibility(View.GONE);
        } else {
            introText.setText(R.string.intro_unregistered);
        }
    }

    @OnEditorAction(R.id.passwordText)
    public boolean onPasswordAction(TextView v, int actionId, KeyEvent event) {
        return handleSubmit(actionId);
    }

    @OnEditorAction(R.id.confirmPasswordText)
    public boolean onConfirmAction(TextView v, int actionId, KeyEvent event) {
        return handleSubmit(actionId);
    }

    private boolean handleSubmit(int actionId) {
        boolean submitted = false;

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            submitted = true;

            checkSubmit();
        }

        return submitted;
    }


    private void checkSubmit() {
        if (isRegistered) {
            Boolean isCorrect = configHelper.checkPw(passwordText.getText().toString());

            if (isCorrect) {
                goToPasswordActivity();
            } else {
                passwordText.setText("");
                Toast.makeText(this, INCORRECT_PASSWORD, Toast.LENGTH_SHORT).show();
            }
        } else {
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        configHelper.createKey(passwordText.getText().toString());
        goToPasswordActivity();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String errorMsg = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(errorMsg);
            } else {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goToPasswordActivity() {
        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
        startActivity(intent);
    }
}
