package com.pimpmyapp.collegeapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.fragment.NoticeFragment;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

//import com.pimpmyapp.collegeapp.Manifest;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton floatingActionButtonNoticeFromGallary,floatingActionButtonDocument,floatingActionButtonNoticeFromCamera;
    Uri selectedImageUriFromGallary;

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
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

        } else if (id == R.id.extra) {

        }
        else if (id == R.id.logout){
            SharedPreferences sharedpref = getSharedPreferences("userData" ,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putBoolean("isLogin" , false);

            startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void init() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButtonDocument = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButtonNoticeFromGallary = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButtonNoticeFromCamera = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
    }

    private void methodListener() {

        floatingActionButtonDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        floatingActionButtonNoticeFromGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
                Toast.makeText(DashboardActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

       /* floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

    void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void openGallary() {
        if(checkGallaryPermission()){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,0);
        }

        else{

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},12);
        }

    }

    private boolean checkGallaryPermission() {
        boolean flag = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0)
        {
            if(resultCode == RESULT_OK)
            {
                Dialog dialog = new Dialog(this);
               // dialog.setContentView();

                selectedImageUriFromGallary = data.getData();
                addpost();
            }
        }
    }

    private void addpost() {

        final NoticePojo noticepojo = new NoticePojo();
        if(selectedImageUriFromGallary !=null)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("image");
            UploadTask uploadTask = reference.putFile(selectedImageUriFromGallary);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(floatingActionButtonNoticeFromGallary,"image upload failed",Snackbar.LENGTH_LONG).show();
                    noticepojo.setImage("");
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    String imageUploadUrl = taskSnapshot.getDownloadUrl().toString();
                    noticepojo.setImage(imageUploadUrl);

                }
            });

        }

    }
}
