package com.pimpmyapp.collegeapp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import uk.co.senab.photoview.PhotoViewAttacher;

public class NoticeViewActivity extends AppCompatActivity {
    TextView title, date, uploadedBy, fileSize, uploadedOn, noticeDes, expand;
    Drawable expandDrawable, contractDrawable;
    ImageView image, editIV, publishIV, deleteIV, downloadImage;
    String notice_id;
    EditText noticeTitle, desc;
    Button dueDateBtn, addNoticeBtn, selectImageBtn;
    String dueDateSelectedByUser = "";
    ImageView noticeImageView;
    Intent i;
    Spinner catSpinner;
    Uri selectedImageUriFromGallery;
    TableLayout tabLay;
    String enteredTitle;
    DatabaseReference ref;
    NoticePojo noticePojo = new NoticePojo();
    boolean isPublished;
    PhotoViewAttacher mAttacher;



    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                File mydir = getDir(root + "/College Board", Context.MODE_PRIVATE); //Creating an internal dir;
                if (!mydir.exists()) {
                    /*Environment.getExternalStorageDirectory().createNewFile();*/
                    mydir.mkdirs();
                }
                String noticeId = noticePojo.getNoticeID();
                OutputStream output = new FileOutputStream(mydir + "/" + noticeId + ".jpg");
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);
        init();
        Intent i = getIntent();
        notice_id = i.getStringExtra("notice_id");
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("user_id", "Anonymous");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");


        ref = FirebaseDatabase.getInstance().getReference("Notice");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                    if (child.getKey().equals(notice_id)) {
                        noticePojo = child.getValue(NoticePojo.class);
                    }
                isPublished = noticePojo.isPublished();
                title.setText(noticePojo.getTitle());
                date.setText("Due Date: " + noticePojo.getDate());
                uploadedBy.setText(noticePojo.getAddedBy());
                noticeDes.setText(noticePojo.getDesc());
                fileSize.setText(noticePojo.getImageSizeString() + " Bytes");
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
                        if (isAdmin) {
                            editIV.setVisibility(View.VISIBLE);
                            publishIV.setVisibility(View.VISIBLE);
                            deleteIV.setVisibility(View.VISIBLE);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                title.setSelected(true);
            }
        }, 3000);
        date = (TextView) findViewById(R.id.date);
        uploadedBy = (TextView) findViewById(R.id.uploadedBy);
        editIV = (ImageView) findViewById(R.id.editIV);
        publishIV = (ImageView) findViewById(R.id.publishedIV);
        noticeDes = (TextView) findViewById(R.id.noticeDes);
        deleteIV = (ImageView) findViewById(R.id.deleteIV);
        image = (ImageView) findViewById(R.id.imageView);
        mAttacher = new PhotoViewAttacher(image);
        fileSize = (TextView) findViewById(R.id.fileSize);
        uploadedOn = (TextView) findViewById(R.id.uploadedOn);
        expand = (TextView) findViewById(R.id.expand);
        tabLay = (TableLayout) findViewById(R.id.tabLay);
        contractDrawable = this.getResources().getDrawable(R.drawable.ic_remove_circle_outline_black_24dp);
        expandDrawable = this.getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp);
        downloadImage = (ImageView) findViewById(R.id.downloadImage);
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
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLay.getVisibility() == View.VISIBLE) {
                    tabLay.setVisibility(View.INVISIBLE);
                    expand.setCompoundDrawablesWithIntrinsicBounds(null, null, expandDrawable, null);
                } else {
                    expand.setCompoundDrawablesWithIntrinsicBounds(null, null, contractDrawable, null);
                    tabLay.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotice();
            }
        });
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //download the image code goes here
                Log.d("1234", noticePojo.getImage());
                new DownloadFileFromURL().execute(noticePojo.getImage());
               /* Intent ceramicIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(noticePojo.getImage()));
                startActivity(ceramicIntent);*/
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publish Settings");
        String publicity = isPublished ? "Published." : "not Published.";
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
        noticeImageView = (ImageView) view.findViewById(R.id.newNoticeAddImage);
        desc = (EditText) view.findViewById(R.id.noticeDescEditText);
        catSpinner = (Spinner) view.findViewById(R.id.categorySpinner);

        desc.setText(noticePojo.getDesc());
        noticeTitle.setText(noticePojo.getTitle());
        if (noticePojo.getDate().equals("No Due Date"))
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

        addNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredTitle = noticeTitle.getText().toString();
                String category = catSpinner.getSelectedItem().toString();
                if (enteredTitle.equals("")) {
                    noticeTitle.setError("Select title for notice");
                } else {
                    if (dueDateSelectedByUser.equals(""))
                        noticePojo.setDate("No Due Date");
                    else if (category.equals("Select Category")) {
                        Toast.makeText(NoticeViewActivity.this, "Select Category", Toast.LENGTH_SHORT).show();
                    } else {
                        noticePojo.setDate("Due Date :" + dueDateSelectedByUser);
                        noticePojo.setTitle(enteredTitle);
                        noticePojo.setDesc(desc.getText().toString());
                        noticePojo.setImage(noticePojo.getImage());
                        noticePojo.setAddedBy(noticePojo.getAddedBy());
                        noticePojo.setCategory(category);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference(catSpinner.getSelectedItem().toString());
                        ref.child(notice_id).setValue(noticePojo);
                        title.setText(noticePojo.getTitle());
                        noticeDes.setText(noticePojo.getDesc());
                        date.setText(noticePojo.getDate());
                        Glide.with(NoticeViewActivity.this).load(noticePojo.getImage()).crossFade().into(image);
                        dialog.cancel();
                    }

                }

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("Add New Notice");
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
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

    private void deleteNotice() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notice");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm?");
        builder.setMessage("Delete this notice");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("1234", "onClick: ref child notice id " + noticePojo.getNoticeID());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference(noticePojo.getCategory() + "/" + noticePojo.getNoticeID());
                storageRef.delete();
                ref.child(noticePojo.getNoticeID()).removeValue();
                finish();


            }

        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
