package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

public class UserProfileActivity extends AppCompatActivity {
    EditText name, branch, year, rollNo, semester, email, phoneNo;
    TextView changePass, changeImage;
    ImageView profileImage;
    Button submitBtn;
    String user_id;
    Intent i;
    Uri selectedImageUriFromGallery = null;
    int newImage = 0;
    UserPojo userPojo;
    Spinner branchSpinner, semSpinner;
    InputMethodManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        init();
        setUneditable();
        setValues();
        methodListener();
    }

    private void init() {
        name = (EditText) findViewById(R.id.userName);
        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        semSpinner = (Spinner) findViewById(R.id.semSpinner);
        year = (EditText) findViewById(R.id.Year);
        rollNo = (EditText) findViewById(R.id.rollNo);
        semester = (EditText) findViewById(R.id.semester);
        email = (EditText) findViewById(R.id.email);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
        changePass = (TextView) findViewById(R.id.changePass);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        changeImage = (TextView) findViewById(R.id.changeImage);
        branch = (EditText) findViewById(R.id.branch);

    }

    private void setUneditable() {
        name.setTag(name.getKeyListener());
        name.setKeyListener(null);
        year.setTag(year.getKeyListener());
        year.setKeyListener(null);
        rollNo.setTag(rollNo.getKeyListener());
        rollNo.setKeyListener(null);
        semester.setTag(semester.getKeyListener());
        semester.setKeyListener(null);
        email.setTag(email.getKeyListener());
        email.setKeyListener(null);
        phoneNo.setTag(phoneNo.getKeyListener());
        phoneNo.setKeyListener(null);
        branch.setTag(branch.getKeyListener());
        branch.setKeyListener(null);
        branchSpinner.setVisibility(View.GONE);
        semSpinner.setVisibility(View.GONE);


    }

    private void methodListener() {
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (name.getRight() - name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        name.setKeyListener((KeyListener) name.getTag());
                        name.requestFocus();
                        im.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
                        return true;
                    }
                }
                return false;
            }
        });

        branch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (branch.getRight() - branch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        branchSpinner.setVisibility(View.VISIBLE);

                        return true;
                    }
                }

                return false;
            }
        });

        semester.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (semester.getRight() - semester.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        semSpinner.setVisibility(View.VISIBLE);

                        return true;
                    }
                }

                return false;
            }
        });


        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (email.getRight() - email.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        email.setKeyListener((KeyListener) email.getTag());


                        return true;
                    }
                }

                return false;
            }
        });

        phoneNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (phoneNo.getRight() - phoneNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        phoneNo.setKeyListener((KeyListener) phoneNo.getTag());


                        return true;
                    }
                }

                return false;
            }
        });

        rollNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (rollNo.getRight() - rollNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        rollNo.setKeyListener((KeyListener) rollNo.getTag());


                        return true;
                    }
                }

                return false;
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertValueOnServer();
                setUneditable();
            }
        });

    }

    private void setValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "Anonymous");
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users/" + user_id);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                userPojo = dataSnapshot.getValue(UserPojo.class);
                if (!userPojo.getProfileImage().equals("")) {
                    Glide.with(UserProfileActivity.this)
                            .load(userPojo.getProfileImage())
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profileImage);
                }
                name.setText(userPojo.getName());
                branch.setText(userPojo.getBranch());
                year.setText(userPojo.getYear() + " Year");
                semester.setText(userPojo.getSem() + " sem");
                email.setText(userPojo.getEmail());
                phoneNo.setText(userPojo.getPhoneNo());
                rollNo.setText(userPojo.getRollNo());
                String selectedBranch = userPojo.getBranch();
                String selectedSem = userPojo.getSem();
                switch (selectedBranch) {
                    case "Ceramic Engineering":
                        branchSpinner.setSelection(1);
                        break;
                    case "Civil Engineering":
                        branchSpinner.setSelection(2);
                        break;
                    case "Computer Science":
                        branchSpinner.setSelection(3);
                        break;
                    case "Electrical Engineering":
                        branchSpinner.setSelection(4);
                        break;
                    case "Electronics Engineering":
                        branchSpinner.setSelection(5);
                        break;
                    case "Mechanical Engineering":
                        branchSpinner.setSelection(6);
                        break;
                }

                switch (selectedSem) {
                    case "I":
                        semSpinner.setSelection(1);
                        break;
                    case "II":
                        semSpinner.setSelection(2);
                        break;
                    case "III":
                        semSpinner.setSelection(3);
                        break;
                    case "IV":
                        semSpinner.setSelection(4);
                        break;
                    case "V":
                        semSpinner.setSelection(5);
                        break;
                    case "VI":
                        semSpinner.setSelection(6);
                        break;
                    case "VII":
                        semSpinner.setSelection(7);
                        break;
                    case "VIII":
                        semSpinner.setSelection(8);
                        break;

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                selectedImageUriFromGallery = data.getData();
                Glide.with(UserProfileActivity.this)
                        .load(selectedImageUriFromGallery)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImage);
                newImage = 1;
            }
    }

    private void insertValueOnServer() {

        String enteredName = name.getText().toString();
        String enteredRollNo = rollNo.getText().toString();
        String enteredBranch = branchSpinner.getSelectedItem().toString();
        String enteredSem = semSpinner.getSelectedItem().toString();
        String enteredEmail = email.getText().toString();
        String enteredPhone = phoneNo.getText().toString();
        final UserPojo newUserPojo = new UserPojo();
        newUserPojo.setName(enteredName);
        newUserPojo.setRollNo(enteredRollNo);
        newUserPojo.setBranch(enteredBranch);
        newUserPojo.setEmail(enteredEmail);
        newUserPojo.setPhoneNo(enteredPhone);
        newUserPojo.setSem(enteredSem);
        newUserPojo.setPass(userPojo.getPass());
        newUserPojo.setAdmin(userPojo.isAdmin());
        newUserPojo.setsAdmin(userPojo.issAdmin());
        newUserPojo.setWantsTobeAdmin(userPojo.isWantsTobeAdmin());
        newUserPojo.setUser_id(user_id);
        switch (enteredSem) {
            case "I":
                newUserPojo.setYear("1st");
                break;
            case "II":
                newUserPojo.setYear("1st");
                break;
            case "III":
                newUserPojo.setYear("2nd");
                break;
            case "IV":
                newUserPojo.setYear("2nd");
                break;
            case "V":
                newUserPojo.setYear("3rd");
                break;
            case "VI":
                newUserPojo.setYear("3rd");
                break;
            case "VII":
                newUserPojo.setYear("4th");
                break;
            case "VIII":
                newUserPojo.setYear("4th");
                break;

        }
        final ProgressDialog progressDialog = new ProgressDialog(UserProfileActivity.this);
        progressDialog.setMessage("updating...");
        progressDialog.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Profile_Images/" + user_id);
        if (newImage == 1) {

            final UploadTask uploadTask = storageReference.putFile(selectedImageUriFromGallery);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    newUserPojo.setProfileImage(taskSnapshot.getDownloadUrl().toString());
                    ref.child(user_id).setValue(newUserPojo);
                    progressDialog.cancel();
                    Snackbar.make(submitBtn, "Profile updated", Snackbar.LENGTH_SHORT).show();

                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfileActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                    Toast.makeText(UserProfileActivity.this, "upadate failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            newUserPojo.setProfileImage(userPojo.getProfileImage());
            ref.child(user_id).setValue(newUserPojo);
            progressDialog.cancel();
            Snackbar.make(submitBtn, "Profile Updated", Snackbar.LENGTH_SHORT).show();


        }



    }
}
