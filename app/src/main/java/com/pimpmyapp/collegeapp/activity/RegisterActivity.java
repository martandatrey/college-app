package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, emailET, passwordET, rollnoET, phnoET, cpasswordET;
    Button regBtn;
    CircleImageView profileImage;
    TextView errorSpinner, regTitle;
    Spinner branch, semester;
    Intent i;
    ScrollView scrollView;
    LinearLayout linLay;
    Uri selectedImageUriFromGallery;
    CheckBox checkBox;
    int profileImageSelected = 0;
    Typeface custom_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        custom_font = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
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
        semester = (Spinner) findViewById(R.id.semester);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        linLay = (LinearLayout) findViewById(R.id.linLay);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        regTitle = (TextView) findViewById(R.id.regTitle);
        regTitle.setTypeface(custom_font);
    }

    private void methodListner() {
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
    }

    private void pickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
        }
    }

    private void openGallery() {

        i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0)
            if (resultCode == RESULT_OK) {
                profileImageSelected = 1;
                selectedImageUriFromGallery = data.getData();
                Glide.with(RegisterActivity.this)
                        .load(selectedImageUriFromGallery)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImage);
            }
    }

    private void register() {

        String name = nameET.getText().toString();
        String rollNo = rollnoET.getText().toString();
        final String phno = phnoET.getText().toString();
        final String email = emailET.getText().toString();
        String pass = passwordET.getText().toString();
        String cpass = cpasswordET.getText().toString();
        String selBranch = branch.getSelectedItem().toString();
        String sem = semester.getSelectedItem().toString();

        errorSpinner.setText("");

        if (name.equals("")) {
            nameET.setError("Name is required.");
            focusOnView(nameET);

        } else if (rollNo.equals("")) {
            rollnoET.setError("Roll Number is required.");
            focusOnView(rollnoET);
        } else if (selBranch.equals("-- Select Branch --")) {
            errorSpinner.setVisibility(View.VISIBLE);
            errorSpinner.setText("Select your branch");
            focusOnView(errorSpinner);
        } else if (sem.equals("-- Select Semester --")) {
            errorSpinner.setVisibility(View.VISIBLE);
            errorSpinner.append("Enter your semester.");
            focusOnView(errorSpinner);
        } else if (email.equals("")) {
            emailET.setError("Email is required.");
            focusOnView(emailET);
        } else if (!(Pattern.matches("\\w*[@]\\w*[.]\\w*", email))) {
            focusOnView(emailET);
            emailET.setError("Email is not valid.");
        } else if (phno.equals("")) {
            phnoET.setError("Phone number is required.");
            focusOnView(phnoET);
        } else if (phno.length() < 10) {
            focusOnView(phnoET);
            phnoET.setError("Enter valid phone no.");
        } else if (pass.equals("")) {
            focusOnView(passwordET);
            passwordET.setError("Password is required.");
        } else if (pass.length() < 8) {
            focusOnView(passwordET);
            passwordET.setError("Minimum 8 characters are required");
        } else if (cpass.equals("")) {
            focusOnView(cpasswordET);
            cpasswordET.setError("Re-enter your password.");
        } else if (!cpass.equals(pass)) {
            focusOnView(cpasswordET);
            cpasswordET.setError("password doesn't matches");
        } else {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Registering...");
            dialog.setCancelable(false);
            dialog.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference("Users");
            final UserPojo user = new UserPojo();
            user.setName(name);
            user.setEmail(email);
            user.setPass(pass);
            user.setBranch(selBranch);
            user.setRollNo(rollNo);
            user.setPhoneNo(phno);
            final String user_ID = ref.push().getKey();
            user.setUser_id(user_ID);
            user.setSem(sem);
            if (checkBox.isChecked())
                user.setWantsTobeAdmin(true);
            else
                user.setWantsTobeAdmin(false);
            switch (sem) {
                case "I":
                    user.setYear("1st");
                    break;
                case "II":
                    user.setYear("1st");
                    break;
                case "III":
                    user.setYear("2nd");
                    break;
                case "IV":
                    user.setYear("2nd");
                    break;
                case "V":
                    user.setYear("3rd");
                    break;
                case "VI":
                    user.setYear("3rd");
                    break;
                case "VII":
                    user.setYear("4th");
                    break;
                case "VIII":
                    user.setYear("4th");
                    break;
            }
            if (profileImageSelected == 1) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Profile_Images/" + user_ID);
                final UploadTask uploadTask = storageReference.putFile(selectedImageUriFromGallery);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        user.setProfileImage(taskSnapshot.getDownloadUrl().toString());
                        ref.child(user_ID).setValue(user);
                        dialog.cancel();
                        Snackbar.make(regBtn, "You have sucessfully registered.", Snackbar.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.cancel();
                        Toast.makeText(RegisterActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                ref.child(user_ID).setValue(user);
                dialog.cancel();
                Toast.makeText(this, "You have sucessfully registered.", Toast.LENGTH_SHORT).show();
                /*Snackbar .make(regBtn, "You have sucessfully registered.", Snackbar.LENGTH_SHORT).show();*/
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

        }


    }

    private final void focusOnView(final View view) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, view.getBottom());
            }
        });
    }
}
