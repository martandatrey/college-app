package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.Calendar;

public class NoticeViewActivity extends AppCompatActivity {
    TextView title, date, uploadedBy, fileSize, uploadedOn, desc;
    ImageView image, editIV, publishIV;
    String notice_id;
    EditText noticeTitle;
    Button dueDateBtn, addNoticeBtn, selectImageBtn;
    String dueDateSelectedByUser;
    ImageView noticeImageView;
    Intent i;
    Uri selectedImageUriFromGallery;
    String enteredTitle;
    DatabaseReference ref;
    NoticePojo noticePojo = new NoticePojo();
    boolean isPublished;


    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);
        init();
        Intent i = getIntent();
        notice_id = i.getStringExtra("notice_id");
        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("user_id","Anonymous");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");


        ref = FirebaseDatabase.getInstance().getReference("notice");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                    if (child.getKey().equals(notice_id)) {
                        noticePojo = child.getValue(NoticePojo.class);
                        Log.d("1234", "onDataChange: " + noticePojo);
                    }
                isPublished = noticePojo.isPublished();
                title.setText(noticePojo.getTitle());
                date.setText(noticePojo.getDate());
                uploadedBy.setText(noticePojo.getAddedBy());
                fileSize.setText(noticePojo.getImageSize() + " Bytes");
                uploadedOn.setText(noticePojo.getAddedOn());
                Glide.with(NoticeViewActivity.this).load(noticePojo.getImage()).crossFade().into(image);
                if (isPublished) {
                    publishIV.setColorFilter(Color.parseColor("#00ff00"));
                } else {
                    publishIV.setColorFilter(Color.parseColor("#ff0000"));
                }
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isAdmin = dataSnapshot.child(user_id).child("admin").getValue(boolean.class);
                        if (!isAdmin) {
                            editIV.setVisibility(View.GONE);
                            publishIV.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("1234", "onCreate: before " + noticePojo);

        methodListeners();
        Log.d("1234", "onCreate: after method listener" + noticePojo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void init() {
        title = (TextView) findViewById(R.id.titleTV);
        date = (TextView) findViewById(R.id.date);
        uploadedBy = (TextView) findViewById(R.id.uploadedBy);
        editIV = (ImageView) findViewById(R.id.editIV);
        publishIV = (ImageView) findViewById(R.id.publishedIV);
        image = (ImageView) findViewById(R.id.imageView);
        fileSize = (TextView) findViewById(R.id.fileSize);
        uploadedOn = (TextView) findViewById(R.id.uploadedOn);
    }


    private void methodListeners() {
        editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog();
            }
        });
        publishIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publish Settings");
        String publicity = isPublished ? "Published." : " not Published.";
        builder.setMessage("This Post is " + publicity);
        builder.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.child(notice_id).child("published").setValue(false);

                publishIV.setColorFilter(Color.parseColor("#ff0000"));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNeutralButton("Publish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.child(notice_id).child("published").setValue(true);
                publishIV.setColorFilter(Color.parseColor("#00ff00"));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notice_dialog_item, null);
        final Dialog dialog = new Dialog(this);

        dueDateBtn = (Button) view.findViewById(R.id.DueDateBtn);
        noticeTitle = (EditText) view.findViewById(R.id.noticeTitleEditText);
        addNoticeBtn = (Button) view.findViewById(R.id.addNoticeBtn);
        selectImageBtn = (Button) view.findViewById(R.id.selectImage);
        noticeImageView = (ImageView) view.findViewById(R.id.newNoticeAddImage);
        desc = (TextView) view.findViewById(R.id.noticeDescEditText);
        desc.setText(noticePojo.getDesc());
        noticeTitle.setText(noticePojo.getTitle());
        if (noticePojo.getDate().equals(""))
            dueDateBtn.setText("Select Date");
        else
            dueDateBtn.setText(noticePojo.getDate());
        Glide.with(this).load(noticePojo.getImage()).crossFade().into(noticeImageView);
        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoticeViewActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if (checkGalleryPermission()) {
                    i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(i, 0);
                } else {

                    ActivityCompat.requestPermissions(NoticeViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
                }
            }
        });

        addNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredTitle = noticeTitle.getText().toString();
                if (enteredTitle.equals("")) {
                    noticeTitle.setError("Select title for notice");
                } else {
                    noticePojo.setDate(dueDateBtn.getText().toString());
                    noticePojo.setTitle(enteredTitle);
                    noticePojo.setDesc(desc.getText().toString());
                    noticePojo.setImage(noticePojo.getImage());
                    noticePojo.setAddedBy(noticePojo.getAddedBy());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("notice");
                    ref.child(notice_id).setValue(noticePojo);
                    title.setText(noticePojo.getTitle());
                    date.setText(noticePojo.getDate());
                    Glide.with(NoticeViewActivity.this).load(noticePojo.getImage()).crossFade().into(image);
                    dialog.cancel();
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("Add New Notice");
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    private boolean checkGalleryPermission() {
        boolean flag = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                selectedImageUriFromGallery = data.getData();
                Glide.with(NoticeViewActivity.this)
                        .load(selectedImageUriFromGallery)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(noticeImageView);

            }
        }
    }
}
