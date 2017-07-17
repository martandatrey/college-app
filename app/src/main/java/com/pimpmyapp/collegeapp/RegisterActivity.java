package com.pimpmyapp.collegeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET ,emailET, passwordET,rollnoET, phnoET, cpasswordET;
    Spinner branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }

    private void init() {
        nameET = (EditText) findViewById(R.id.nameET);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passET);
        cpasswordET = (EditText) findViewById(R.id.cpassET);
        phnoET = (EditText) findViewById(R.id.phnoET);
        rollnoET = (EditText) findViewById(R.id.rollNoET);
        branch = (Spinner) findViewById(R.id.branch);
    }
}
