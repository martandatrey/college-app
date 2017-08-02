package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
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
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

import static android.support.design.widget.Snackbar.make;

public class AddNewNoticeActivity extends AppCompatActivity implements ImagePickerCallback {
    EditText noticeTitle, noticeDes;
    Button dueDateBtn, selectImageBtn;
    ImageView noticeImageView;
    String dueDateSelectedByUser = "";
    Uri selectedImageUriFromGallery;
    int imageViewCheck = 0, year, month, day;
    String enteredTitle, enteredDes;
    Intent i;
    Spinner catSpinner;
    View parentLayout;
    CoordinatorLayout corLay;
    TableRow cameraRow, galleryRow;
    Calendar calendar;
    NoticePojo noticepojo;
    CameraImagePicker cameraPicker;
    int flagCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_notice);
        getSupportActionBar().setTitle("Add New Notice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkPermissions();
        init();
        methodListener();

    }


    private void checkPermissions() {
        if (!checkCameraPermission() || !checkGalleryPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 12);
        }
    }

    private void init() {
        parentLayout = findViewById(android.R.id.content);
        noticeTitle = (EditText) findViewById(R.id.noticeTitleEditText);
        noticeDes = (EditText) findViewById(R.id.noticeDesEditText);
        dueDateBtn = (Button) findViewById(R.id.DueDateBtn);
        selectImageBtn = (Button) findViewById(R.id.selectImage);
        noticeImageView = (ImageView) findViewById(R.id.newNoticeAddImage);
        catSpinner = (Spinner) findViewById(R.id.catSpinner);
        corLay = (CoordinatorLayout) findViewById(R.id.newPostrootLay);

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
                openDialog();
            }
        });
    }


    private boolean checkGalleryPermission() {
        boolean flag = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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
        if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.publish) {
            enteredTitle = noticeTitle.getText().toString();
            enteredDes = noticeDes.getText().toString();
            if (enteredTitle.equals("")) {
                noticeTitle.setError("Select title for notice");
            } else if (catSpinner.getSelectedItem().toString().equals("Select Category")) {
                Snackbar.make(corLay, "Select Category", Snackbar.LENGTH_SHORT).show();
            } else {
                addpost();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addpost() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();
        final float[] fileSize = new float[1];
        noticepojo = new NoticePojo();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String cat = catSpinner.getSelectedItem().toString();
        noticepojo.setCategory(cat);
        final String addedOn = "" + day + "-" + (month + 1) + "-" + year;
        noticepojo.setDesc(enteredDes);
        noticepojo.setAddedOn(addedOn);
        noticepojo.setTitle(enteredTitle);
        if (!dueDateSelectedByUser.equals(""))
            noticepojo.setDate(dueDateSelectedByUser);
        else
            noticepojo.setDate("No Due Date");
        if (selectedImageUriFromGallery != null) {

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

                FirebaseStorage storage = FirebaseStorage.getInstance();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference(cat);
                final String noticeKey = ref.push().getKey();
                final StorageReference reference = storage.getReference(cat + "/" + noticeKey);
                final UploadTask[] uploadTask = {null};
                if (flagCheck == 1) {
                    Bitmap src = BitmapFactory.decodeFile(selectedImageUriFromGallery.toString());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    uploadTask[0] = reference.putBytes(data);
                } else if (flagCheck == 2) {
                    uploadTask[0] = reference.putFile(selectedImageUriFromGallery);
                }

                uploadTask[0].addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar;
                        snackbar = make(selectImageBtn, "Image upload failed", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                uploadTask[0] = reference.putFile(selectedImageUriFromGallery);
                            }
                        });
                        dialog.cancel();
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
                        @SuppressWarnings("VisibleForTests") String imageUploadUrl = taskSnapshot.getDownloadUrl().toString();
                        noticepojo.setImage(imageUploadUrl);
                        noticepojo.setNoticeID(noticeKey);
                        noticepojo.setAddedOn(addedOn);

                        fileSize[0] = (float) taskSnapshot.getBytesTransferred();
                        long imageSize = taskSnapshot.getTotalByteCount();
                        noticepojo.setImageSize(imageSize);
                        if (fileSize[0] < 1024) {

                            noticepojo.setImageSizeString(fileSize[0] + "Bytes");
                        } else if (fileSize[0] < (1024 * 1024) && fileSize[0] >= 1024) {

                            noticepojo.setImageSizeString(fileSize[0] / 1024 + "KB");
                        } else if (fileSize[0] < (1024 * 1024 * 1024) && fileSize[0] >= (1024 * 1024)) {

                            noticepojo.setImageSizeString(fileSize[0] / (1024 * 1024) + "MB");
                        }

                        ref.child(noticeKey).setValue(noticepojo);
                        dialog.cancel();
                        Intent i = new Intent(AddNewNoticeActivity.this, DashboardActivity.class);
                        i.putExtra("uploaded", true);
                        startActivity(i);
                        finish();

                    }
                });
            }
        } else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference(cat);
            final String noticeKey = ref.push().getKey();
            SharedPreferences sharedPreference = getSharedPreferences("userData", MODE_PRIVATE);
            String user_name = sharedPreference.getString("name", "unknown");
            noticepojo.setAddedBy(user_name);
            noticepojo.setNoticeID(noticeKey);
            noticepojo.setAddedOn(addedOn);
            ref.child(noticeKey).setValue(noticepojo);
            dialog.cancel();
            Intent i = new Intent(AddNewNoticeActivity.this, DashboardActivity.class);
            i.putExtra("uploaded", true);
            startActivity(i);
            finish();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void openGallery() {
        i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 0);
    }

    private void openDialog() {
        LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.image_dialog_item, null);
        final Dialog dialog = new Dialog(this);
        cameraRow = (TableRow) view.findViewById(R.id.cameraRow);
        galleryRow = (TableRow) view.findViewById(R.id.galleryRow);
        cameraRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagCheck = 1;
                takePicture();

                dialog.cancel();

            }
        });

        galleryRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagCheck = 2;
                openGallery();
                dialog.cancel();
            }

        });
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    imageViewCheck = 1;
                    selectedImageUriFromGallery = data.getData();
                    Glide.with(AddNewNoticeActivity.this)
                            .load(selectedImageUriFromGallery)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(noticeImageView);

                }
                break;
            case Picker.PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    imageViewCheck = 1;
                    if (cameraPicker == null) {
                        cameraPicker = new CameraImagePicker(this);
                        cameraPicker.setImagePickerCallback(this);
                        cameraPicker.reinitialize(String.valueOf(selectedImageUriFromGallery));
                    }
                    cameraPicker.submit(data);
                }


        }


    }

    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.setDebugglable(true);
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
        cameraPicker.setImagePickerCallback(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        selectedImageUriFromGallery = Uri.parse(cameraPicker.pickImage());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", String.valueOf(selectedImageUriFromGallery));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                selectedImageUriFromGallery = Uri.parse(savedInstanceState.getString("picker_path"));
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        Log.d("1234", "onImagesChosen: " + selectedImageUriFromGallery);
        noticeImageView.setImageURI(selectedImageUriFromGallery);

    }

    @Override
    public void onError(String s) {
        Toast.makeText(this, "Oops! something went wrong", Toast.LENGTH_SHORT).show();
    }
}
