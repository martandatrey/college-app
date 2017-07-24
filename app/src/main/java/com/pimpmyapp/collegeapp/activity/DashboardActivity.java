package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.fragment.AdminFragment;
import com.pimpmyapp.collegeapp.fragment.NoticeFragment;
import com.pimpmyapp.collegeapp.fragment.ResultFragment;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.Calendar;

//import com.pimpmyapp.collegeapp.Manifest;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fabDoc, fabGal, fabCam;
    Uri selectedImageUriFromGallery;
    EditText noticeTitle;
    RelativeLayout relativeLayoutFab;
    Intent i;
    TextView nameTv, branchTv, yearTv;
    Button dueDateBtn, addNoticeBtn, selectImageBtn;
    String dueDateSelectedByUser,user_id;
    ImageView noticeImageView;
    String enteredTitle;
    NavigationView navigationView;
    Toolbar dashboardToolbar;
    int imageViewCheck = 0;
    CoordinatorLayout cordlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        setValues();
        methodListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
       if(i.getBooleanExtra("uploaded",false)){
           Snackbar.make(floatingActionMenu, "Your notice will be published shortly", Snackbar.LENGTH_LONG).show();
           i.putExtra("uploaded" , false);
       }
    }

    private void setValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","Anonymous");
      /*  DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameTv.setText(dataSnapshot.child(user_id).child("name").getValue(String.class));
                branchTv.setText(dataSnapshot.child(user_id).child("branch").getValue(String.class));
                String year= dataSnapshot.child(user_id).child("year").getValue(String.class) + " year " + dataSnapshot.child(user_id).child("sem").getValue(String.class) + " semester";
                yearTv.setText(year);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;

        }
        int id = item.getItemId();
        if (id == R.id.Logout) {
            SharedPreferences sharedpref = getSharedPreferences("userData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putBoolean("isLogin", false);
            editor.commit();

            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notices) {
            changeFragment(new NoticeFragment());
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Notices");

        } else if (id == R.id.TimeTable) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Time Table");

        } else if (id == R.id.AcedemicCalender) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Academic Calendar");

        } else if (id == R.id.Documents) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Documents");

        }else if (id == R.id.result) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Check Result");
            changeFragment(new ResultFragment());
            floatingActionMenu.setVisibility(View.GONE);

        } else if (id == R.id.reviewNotice) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Review Notice");
            changeFragment(new AdminFragment());

        } else if (id == R.id.users) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Extra");

        } else if (id == R.id.logout) {
            logout();
        }
        navigationView.setCheckedItem(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabGal = (FloatingActionButton) findViewById(R.id.fab_gal);
        fabDoc = (FloatingActionButton) findViewById(R.id.fab_doc);
        fabCam = (FloatingActionButton) findViewById(R.id.fab_cam);
        dashboardToolbar = (Toolbar) findViewById(R.id.toolbar);
        branchTv = (TextView) findViewById(R.id.branchTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        yearTv = (TextView) findViewById(R.id.yearTv);
        relativeLayoutFab = (RelativeLayout) findViewById(R.id.relativeLayoutFab);
        cordlay = (CoordinatorLayout) findViewById(R.id.cordLay);
    }


    private void methodListener() {

        fabGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,AddNewNoticeActivity.class));

            }
        });

        fabDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "No Function assigned to this button.", Toast.LENGTH_SHORT).show();

            }
        });

        fabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "no func assigned", Toast.LENGTH_SHORT).show();
            }
        });
      /*  cordlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (floatingActionMenu.isOpened())
                    floatingActionMenu.close(true);
                return true;
            }
        });*/
    }

    private void showDialog() {


        LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.notice_dialog_item, null);
        final Dialog dialog = new Dialog(this);

        dueDateBtn = (Button) view.findViewById(R.id.DueDateBtn);
        noticeTitle = (EditText) view.findViewById(R.id.noticeTitleEditText);
        addNoticeBtn = (Button) view.findViewById(R.id.addNoticeBtn);
        selectImageBtn = (Button) view.findViewById(R.id.selectImage);
        noticeImageView = (ImageView) view.findViewById(R.id.newNoticeAddImage);
        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DashboardActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                } else if (imageViewCheck == 0) {
                    Toast.makeText(DashboardActivity.this, "select an image first", Toast.LENGTH_LONG).show();
                } else {
                    dialog.cancel();
                    addpost();
                }


            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallary();


            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("Add New Notice");
        dialog.setContentView(view);
        dialog.show();
        dialog.setCancelable(true);
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                imageViewCheck = 1;
                selectedImageUriFromGallery = data.getData();
                Glide.with(DashboardActivity.this)
                        .load(selectedImageUriFromGallery)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(noticeImageView);

            }
        }
    }

    private void addpost() {

        final NoticePojo noticepojo = new NoticePojo();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String addedOn = "" + day + "-" + (month + 1) + "-" + year;

        noticepojo.setAddedOn(addedOn);
        noticepojo.setTitle(enteredTitle);
        noticepojo.setDate(dueDateSelectedByUser);
        if (selectedImageUriFromGallery != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();

            if (!isNetworkAvailable()) {
                Snackbar.make(fabGal, "No Internet Connection.", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addpost();
                    }
                }).show();
            } else {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("notice");
                final String noticeKey = ref.push().getKey();
                final StorageReference reference = storage.getReference(noticeKey);
                final UploadTask[] uploadTask = {reference.putFile(selectedImageUriFromGallery)};
                uploadTask[0].addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(fabDoc, "Image upload failed", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                uploadTask[0] = reference.putFile(selectedImageUriFromGallery);
                            }
                        });


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
                        Snackbar.make(fabGal, "Your notice will be isPublished shortly", Snackbar.LENGTH_LONG).show();


                    }
                });
            }
        }

    }


    private void logout() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Logging out...");
        dialog.setCancelable(false);
        dialog.show();
        SharedPreferences sharedpref = getSharedPreferences("userData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();
        dialog.cancel();

        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
