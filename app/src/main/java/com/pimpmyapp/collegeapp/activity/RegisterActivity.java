package com.pimpmyapp.collegeapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, emailET, passwordET, rollnoET, phnoET, cpasswordET;
    Button regBtn;
    TextView errorSpinner;
    Spinner branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        methodListner();
    }

    private void init() {
        nameET = (EditText) findViewById(R.id.nameET);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passET);
        cpasswordET = (EditText) findViewById(R.id.cpassET);
        phnoET = (EditText) findViewById(R.id.phnoET);
        rollnoET = (EditText) findViewById(R.id.rollNoET);
        branch = (Spinner) findViewById(R.id.branch);
        regBtn = (Button) findViewById(R.id.registerBtn);
        errorSpinner = (TextView) findViewById(R.id.errorSpinner);
        errorSpinner.setVisibility(View.GONE);
    }

    private void methodListner() {
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String name = nameET.getText().toString();
        String rollNo = rollnoET.getText().toString();
        String phno = phnoET.getText().toString();
        String email = emailET.getText().toString();
        String pass = passwordET.getText().toString();
        String cpass = cpasswordET.getText().toString();
        String selBranch = branch.getSelectedItem().toString();

            if (name.equals(""))
                nameET.setError("Name is required.");

            if (email.equals(""))
                emailET.setError("Email is required.");
            if (rollNo.equals(""))
                rollnoET.setError("Roll Number is required.");
            if (selBranch.equals("-- Select Branch --")) {
                errorSpinner.setVisibility(View.VISIBLE);
                errorSpinner.setText("Select your branch");
            }
            if (pass.equals(""))
                passwordET.setError("Password is required.");
            if (cpass.equals(""))
                cpasswordET.setError("Re-enter your password.");

        else {
                FirebaseDatabase  database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                UserPojo user = new UserPojo();
                user.setName(name);
                user.setEmail(email);
                user.setPass(pass);
                user.setBranch(selBranch);
                user.setRollNo(rollNo);
                user.setPhoneNo(phno);
                String user_ID = ref.push().getKey();
                user.setUser_id(user_ID);

                ref.child(user_ID).setValue(user);

                Toast.makeText(this, "You have registered sucessfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }


    }
}
