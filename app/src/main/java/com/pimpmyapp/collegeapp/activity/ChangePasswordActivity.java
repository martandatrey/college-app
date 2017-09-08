package com.pimpmyapp.collegeapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.pimpmyapp.collegeapp.R;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPass, newPass, confirmNewPass;
    Button submitBtn, cancelBtn;
    String enteredCurrentPass, enteredNewPass, enteredConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        currentPass = (EditText) findViewById(R.id.currentPassET);
        newPass = (EditText) findViewById(R.id.newPassET);
        confirmNewPass = (EditText) findViewById(R.id.confirmPassET);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);


    }
}
