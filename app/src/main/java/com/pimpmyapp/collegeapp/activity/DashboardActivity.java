package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import com.pimpmyapp.collegeapp.fragment.AdminFragment;
import com.pimpmyapp.collegeapp.fragment.NoticeFragment;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.Calendar;

//import com.pimpmyapp.collegeapp.Manifest;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fabDoc, fabGal, fabCam;
    Uri selectedImageUriFromGallary;
    EditText noticeTitle;
    Intent i;
    Button dueDateBtn, addNoticeBtn;
    String dueDateSelectedByUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        methodListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        } else if (id == R.id.TimeTable) {

        } else if (id == R.id.AcedemicCalender) {

        } else if (id == R.id.Documents) {

        } else if (id == R.id.reviewNotice) {
            changeFragment(new AdminFragment());

        } else if (id == R.id.extra) {

        } else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabGal = (FloatingActionButton) findViewById(R.id.fab_gal);
        fabDoc = (FloatingActionButton) findViewById(R.id.fab_doc);
        fabCam = (FloatingActionButton) findViewById(R.id.fab_cam);
    }


    private void methodListener() {

        fabGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
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

    }

    void changeFragment(Fragment fragment) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                selectedImageUriFromGallary = data.getData();

                LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflator.inflate(R.layout.notice_dialog_item, null);
                final Dialog dialog = new Dialog(this);

                dueDateBtn = (Button) view.findViewById(R.id.DueDateBtn);
                noticeTitle = (EditText) view.findViewById(R.id.noticeTitleTextView);
                addNoticeBtn = (Button) view.findViewById(R.id.addNoticeBtn);
                dueDateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(DashboardActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dueDateSelectedByUser = "" + day + "-" + (month + 1) + "-" + year;
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();


                    }
                });

                addNoticeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        addpost();

                    }
                });
                dialog.setContentView(view);
                dialog.show();
                dialog.setCancelable(false);
            }
        }
    }

    private void addpost() {
        String enteredTitle = noticeTitle.getText().toString();
        final NoticePojo noticepojo = new NoticePojo();

        noticepojo.setTitle(enteredTitle);
        noticepojo.setDate(dueDateSelectedByUser);
        if (selectedImageUriFromGallary != null) {
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
                final UploadTask[] uploadTask = {reference.putFile(selectedImageUriFromGallary)};
                uploadTask[0].addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(fabDoc, "Image upload failed", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                uploadTask[0] = reference.putFile(selectedImageUriFromGallary);
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
                        String imageUploadUrl = taskSnapshot.getDownloadUrl().toString();
                        noticepojo.setImage(imageUploadUrl);
                        noticepojo.setNoticeID(noticeKey);
                        ref.child(noticeKey).setValue(noticepojo);
                        Snackbar.make(fabGal, "Your notice will be published shortly", Snackbar.LENGTH_LONG).show();


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
