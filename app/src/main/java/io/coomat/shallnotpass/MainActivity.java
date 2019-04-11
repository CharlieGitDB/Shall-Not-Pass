package io.coomat.shallnotpass;

import androidx.appcompat.app.AppCompatActivity;
import io.coomat.shallnotpass.helper.ConfigHelper;

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

    private TextView introText;

    @Order(1)
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText passwordText;

    @Order(2)
    @ConfirmPassword
    private EditText confirmPasswordText;

    private Validator validator = new Validator(this);

    private ConfigHelper configHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initEvents();
    }

    private void init() {
        configHelper = new ConfigHelper(this);

        introText = findViewById(R.id.introText);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);

        validator.setValidationListener(this);

        if (configHelper.isRegistered()) {
            introText.setText(R.string.intro_registered);
        } else {
            introText.setText(R.string.intro_unregistered);
        }
    }

    private void initEvents() {
        confirmPasswordText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean submitted = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitted = true;

                    handleSubmit();
                }

                return submitted;
            }
        });
    }

    private void handleSubmit() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        if (!configHelper.isRegistered()) {
            configHelper.createKey(passwordText.getText().toString());
        }

        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
        startActivity(intent);
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
}
