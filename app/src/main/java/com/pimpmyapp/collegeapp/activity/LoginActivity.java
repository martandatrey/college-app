package com.pimpmyapp.collegeapp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

public class LoginActivity extends AppCompatActivity {


    EditText loginId, loginpass;
    Button loginBtn;
    TextView registerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pref = getSharedPreferences("userData",MODE_PRIVATE);
        boolean isLogin = pref.getBoolean("isLogin", false);
        if (isLogin) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
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
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String username = loginId.getText().toString();
        final String password = loginpass.getText().toString();

        if (username.equals("")) {
            loginId.setError("This field is required");
        } else if (password.equals("")) {
            loginpass.setError("This field is required");
        } else {
            FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userReference = userDatabase.getReference("Users");
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        UserPojo userPojo = childDataSnapshot.getValue(UserPojo.class);

                        if ((username.equals(userPojo.getEmail()) || username.equals(userPojo.getPhoneNo())) && password.equals(userPojo.getPass())) {
                            SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLogin", true);
                            editor.putString("user_id", userPojo.getUser_id());
                            editor.commit();
                            progressDialog.cancel();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Snackbar snackbar;
                            snackbar = Snackbar.make(loginBtn, "Check your Email/Password.", Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(Color.rgb(98, 134, 241));
                            TextView snackbarTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                            snackbarTextView.setTextColor(Color.rgb(255, 255, 255));
                            snackbar.show();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }
}
