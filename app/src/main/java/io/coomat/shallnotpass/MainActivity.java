package io.coomat.shallnotpass;

import androidx.appcompat.app.AppCompatActivity;
import io.coomat.shallnotpass.config.Constant;
import io.coomat.shallnotpass.util.EncryptUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initEvents();
    }

    private void init() {
        passwordText = findViewById(R.id.passwordText);
    }

    private void initEvents() {
        passwordText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
        String passwordVal = passwordText.getText().toString();

        Boolean isCorrectPassword = EncryptUtil.check(passwordVal, Constant.hashedKey);
        
        if (isCorrectPassword) {
            Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
            startActivity(intent);
        } else {
            passwordText.setText("");
            Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
        startActivity(intent);
    }
}
