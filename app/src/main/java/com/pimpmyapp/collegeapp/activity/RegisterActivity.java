package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.pimpmyapp.collegeapp.URLhelper;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, emailET, passwordET, rollnoET, phnoET, cpasswordET, course, semester, fnameET, mnameET, genderET, yearET;
    Button regBtn;
    CircleImageView profileImage;
    TextView regTitle;
    Intent i, rollIntent;
    ScrollView scrollView;
    LinearLayout linLay;
    Uri selectedImageUriFromGallery;
    CheckBox checkBox;
    int profileImageSelected = 0;
    // Typeface custom_font;
    HashMap<String, String> userData = new HashMap<>();
    String stName, stfName, stmName, stYear, stSem, stRollNo, stGender, stCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        custom_font = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
        rollIntent = getIntent();
        userData.put("rollno", rollIntent.getStringExtra("rollno"));
        Log.d("1234", "onCreate: " + rollIntent.getStringExtra("rollno"));

        init();
        methodListner();
        setUserData();
        getUserData();
    }

    private void setUserData() {
        Log.d("1234", "setUserData: ");
        nameET.setText(stName);
        fnameET.setText(stfName);
        mnameET.setText(stmName);
        course.setText(stCourse);
        semester.setText(stSem);
        rollnoET.setText(stRollNo);
        yearET.setText(stYear);
        genderET.setText(stGender);
    }

    private void getUserData() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Getting User Data...");
        dialog.setCancelable(false);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLhelper.checkUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    stName = jsonObject.getString("name");
                    stCourse = jsonObject.getString("course");
                    stmName = jsonObject.getString("mname");
                    stfName = jsonObject.getString("fname");
                    stGender = jsonObject.getString("gender");
                    stYear = jsonObject.getString("year");
                    stSem = jsonObject.getString("sem");
                    stRollNo = jsonObject.getString("rollno");
                    Log.d("1234", "onResponse: "+ stRollNo);
                   /* userData.put("name", stName);
                    userData.put("mname", jsonObject.getString("mname"));
                    userData.put("fname", jsonObject.getString("fname"));
                    userData.put("year", jsonObject.getString("year"));
                    userData.put("sem", jsonObject.getString("sem"));
                    userData.put("rollno", jsonObject.getString("rolno"));
                    userData.put("gender", jsonObject.getString("gender"));
                    userData.put("course", jsonObject.getString("course"));*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("1234", "onResponse: " + e.toString());
                }


                dialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("rollno", rollIntent.getStringExtra("rollno"));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void init() {
        nameET = (EditText) findViewById(R.id.RnameET);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passET);
        cpasswordET = (EditText) findViewById(R.id.cpassET);
        phnoET = (EditText) findViewById(R.id.phnoET);
        rollnoET = (EditText) findViewById(R.id.rollNoET);
        course = (EditText) findViewById(R.id.course);
        regBtn = (Button) findViewById(R.id.registerBtn);
        semester = (EditText) findViewById(R.id.semester);
        yearET = (EditText) findViewById(R.id.yearET);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        linLay = (LinearLayout) findViewById(R.id.linLay);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        regTitle = (TextView) findViewById(R.id.regTitle);
        fnameET = (EditText) findViewById(R.id.fnameET);
        mnameET = (EditText) findViewById(R.id.mnameET);
        genderET = (EditText) findViewById(R.id.genderET);
        // regTitle.setTypeface(custom_font);
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
        String selCourse = course.getText().toString();
        String sem = semester.getText().toString();
        String year = yearET.getText().toString();
        String fname = fnameET.getText().toString();
        String mname = mnameET.getText().toString();
        String gender = genderET.getText().toString();

        if (name.equals("")) {
            nameET.setError("Name is required.");
            focusOnView(nameET);

        } else if (rollNo.equals("")) {
            rollnoET.setError("Roll Number is required.");
            focusOnView(rollnoET);
        } else if (gender.equals("")) {
            rollnoET.setError("Gender is required.");
            focusOnView(genderET);
        } else if (fname.equals("")) {
            rollnoET.setError("Father's Name is required.");
            focusOnView(fnameET);
        } else if (mname.equals("")) {
            rollnoET.setError("Mother's Name is required.");
            focusOnView(mnameET);
        } else if (selCourse.equals("")) {
            course.setError("Enter Branch");
            focusOnView(course);
        } else if (sem.equals("")) {
            semester.setError("Enter Semester");
            focusOnView(semester);
        } else if (year.equals("")) {
            yearET.setError("Enter Year");
            focusOnView(semester);
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
            user.setBranch(selCourse);
            user.setRollNo(rollNo);
            user.setPhoneNo(phno);
            final String user_ID = ref.push().getKey();
            user.setUser_id(user_ID);
            user.setSem(sem);
            user.setYear(year);
            if (checkBox.isChecked())
                user.setWantsTobeAdmin(true);
            else
                user.setWantsTobeAdmin(false);

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
