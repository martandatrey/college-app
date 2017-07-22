package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.Calendar;

import static android.support.design.widget.Snackbar.make;

public class AddNewNoticeActivity extends AppCompatActivity {
    EditText noticeTitle, noticeDes;
    Button dueDateBtn, selectImageBtn;
    ImageView noticeImageView;
    String dueDateSelectedByUser;
    Uri selectedImageUriFromGallary;
    int imageViewCheck = 0;
    String enteredTitle, enteredDes;
    Intent i;
    View parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_notice);
        getSupportActionBar().setTitle("Add New Notice");
        init();
        methodListener();
    }

    private void init() {
        parentLayout = findViewById(android.R.id.content);
        noticeTitle = (EditText) findViewById(R.id.noticeTitleEditText);
        noticeDes = (EditText) findViewById(R.id.noticeDesEditText);
        dueDateBtn = (Button) findViewById(R.id.DueDateBtn);
        selectImageBtn = (Button) findViewById(R.id.selectImage);
        noticeImageView = (ImageView) findViewById(R.id.newNoticeAddImage);


    }

    private void methodListener() {
        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewNoticeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dueDateSelectedByUser = "" + day + "-" + (month + 1) + "-" + year;
                        dueDateBtn.setText(dueDateSelectedByUser);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }

        });
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });
    }

    private void openGallary() {
        if (checkGalleryPermission()) {
            i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, 0);
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
        }

    }

    private boolean checkGalleryPermission() {
        boolean flag = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                imageViewCheck = 1;
                selectedImageUriFromGallary = data.getData();
                Glide.with(AddNewNoticeActivity.this)
                        .load(selectedImageUriFromGallary)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(noticeImageView);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.publish_post, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.publish) {
            enteredTitle = noticeTitle.getText().toString();
            enteredDes = noticeDes.getText().toString();
            if (enteredTitle.equals("")) {
                noticeTitle.setError("Select title for notice");
            } else if (imageViewCheck == 0) {
                Toast.makeText(AddNewNoticeActivity.this, "select an image first", Toast.LENGTH_LONG).show();
            } else {
                addpost();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addpost() {
        final NoticePojo noticepojo = new NoticePojo();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String addedOn = "" + day + "-" + (month + 1) + "-" + year;
        noticepojo.setDesc(enteredDes);
        noticepojo.setAddedOn(addedOn);
        noticepojo.setTitle(enteredTitle);
        noticepojo.setDate(dueDateSelectedByUser);
        if (selectedImageUriFromGallary != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            if (!isNetworkAvailable()) {
                Snackbar snackbar = Snackbar.make(selectImageBtn, "No Internet Connection.", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addpost();
                    }
                });
                snackbar.show();
            } else {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading...");
                dialog.setCancelable(false);
                dialog.show();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("notice");
                final String noticeKey = ref.push().getKey();
                final StorageReference reference = storage.getReference(noticeKey);
                final UploadTask[] uploadTask = {reference.putFile(selectedImageUriFromGallary)};
                uploadTask[0].addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar;
                        snackbar = make(selectImageBtn, "Image upload failed", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                uploadTask[0] = reference.putFile(selectedImageUriFromGallary);
                            }
                        });
                        snackbar.show();
                        noticepojo.setImage("");
                    }
                });

                uploadTask[0].addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        SharedPreferences sharedPreference = getSharedPreferences("userData", MODE_PRIVATE);
                        String user_name = sharedPreference.getString("name", "unknown");
                        noticepojo.setAddedBy(user_name);
                        Log.d("1234", "onSuccess: " + user_name);
                        @SuppressWarnings("VisibleForTests") String imageUploadUrl = taskSnapshot.getDownloadUrl().toString();
                        noticepojo.setImage(imageUploadUrl);
                        noticepojo.setNoticeID(noticeKey);
                        ref.child(noticeKey).setValue(noticepojo);
                        dialog.cancel();
                        Snackbar.make(selectImageBtn, "Your notice will be published shortly", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(AddNewNoticeActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }, 3000);
                    }
                });
            }
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
