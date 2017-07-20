package com.pimpmyapp.collegeapp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.Calendar;

public class NoticeViewActivity extends AppCompatActivity {
    TextView title, date, uploadedBy;
    ImageView image, edit, publishIV;
    String notice_id;
    EditText noticeTitle;
    Button dueDateBtn, addNoticeBtn, selectImageBtn;
    String dueDateSelectedByUser;
    ImageView noticeImageView;
    String enteredTitle;
    NoticePojo noticePojo;
    boolean isPublished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);

        Intent i = getIntent();
        notice_id = i.getStringExtra("notice_id");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("notice");
        Log.d("1234", "onCreate: ");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticePojo = dataSnapshot.child(notice_id).getValue(NoticePojo.class);
                isPublished = noticePojo.isPublished();
                Log.d("1234", "onDataChange: " + noticePojo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("1234", "onCancelled: ");
            }
        });
        Log.d("1234", "onCreate: 222 ");
        title = (TextView) findViewById(R.id.titleTV);
        date = (TextView) findViewById(R.id.date);
        uploadedBy = (TextView) findViewById(R.id.uploadedBy);
        edit = (ImageView) findViewById(R.id.editIV);
        publishIV = (ImageView) findViewById(R.id.publishedIV);
        image = (ImageView) findViewById(R.id.imageView);
       // title.setText(notice.getTitle());
      //  date.setText(notice.getDate());
       // uploadedBy.setText(notice.getAddedBy());
       // Glide.with(this).load(notice.getImage()).crossFade().into(image);
        if (!isPublished) {
            publishIV.setColorFilter(Color.parseColor("#ff0000"));
        } else {
            publishIV.setColorFilter(Color.parseColor("#00ff00"));
        }

        methodListners();
    }

    private void methodListners() {
        edit.setOnClickListener(new View.OnClickListener() {
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
        builder.setTitle("Publish Setting");
        String publicity = isPublished ? " Published." : " not Published.";
        builder.setMessage("This Post is " + publicity);
        builder.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noticePojo.setPublished(false);
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
                noticePojo.setPublished(false);
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
        noticeTitle.setText(noticePojo.getTitle());
        dueDateBtn.setText(noticePojo.getAddedOn());
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

        addNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredTitle = noticeTitle.getText().toString();
                if (enteredTitle.equals("")) {
                    noticeTitle.setError("Select title for notice");
                } else {
                    noticePojo.setAddedOn(dueDateBtn.getText().toString());
                    noticePojo.setTitle(enteredTitle);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("notice");
                    ref.child(notice_id).setValue(noticePojo);
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
}
