package com.pimpmyapp.collegeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pimpmyapp.collegeapp.R;

public class LoginActivity extends AppCompatActivity {


    EditText loginId,loginpass;
    Button loginBtn;
    TextView registerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intit();
        methodListener();
    }

    private void intit() {

        loginId = (EditText) findViewById(R.id.loginId);
        loginpass = (EditText) findViewById(R.id.loginPass);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
    }

    private void methodListener() {

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });
    }

    private void login() {

        String username = loginId.getText().toString();
        String password = loginpass.getText().toString();
        
    }
}
