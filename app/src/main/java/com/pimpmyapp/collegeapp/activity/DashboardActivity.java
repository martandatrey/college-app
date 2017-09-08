package com.pimpmyapp.collegeapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.fragment.AcademicCalendarFragment;
import com.pimpmyapp.collegeapp.fragment.AdminFragment;
import com.pimpmyapp.collegeapp.fragment.NoticeFragment;
import com.pimpmyapp.collegeapp.fragment.ResultFragment;
import com.pimpmyapp.collegeapp.fragment.UserListingFragment;
import com.pimpmyapp.collegeapp.fragment.WelcomeFragment;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

//import com.pimpmyapp.collegeapp.Manifest;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fabDoc, fabGal;
    RelativeLayout relativeLayoutFab;
    View view;
    TextView nameTv, branchTv, yearTv;
    String  user_id;
    ImageView  profileImage;
    NavigationView navigationView;
    Toolbar dashboardToolbar;
    CoordinatorLayout cordlay;
    boolean isBlocked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserPojo userPojo = dataSnapshot.child(user_id).getValue(UserPojo.class);
                isBlocked=userPojo.isBlocked();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        init();
        navigationView.setItemIconTintList(null);
        MenuItem mi = navigationView.getMenu().getItem(0);
        mi.setChecked(true);
        setValues();
        methodListener();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        if (i.getBooleanExtra("uploaded", false)) {
            Snackbar.make(floatingActionMenu, "Your notice will be published shortly", Snackbar.LENGTH_LONG).show();
            i.putExtra("uploaded", false);
        }
        setValues();


    }

    public void setValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "Anonymous");

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameTv.setText(dataSnapshot.child(user_id).child("name").getValue(String.class));
                branchTv.setText(dataSnapshot.child(user_id).child("branch").getValue(String.class));
                String year = dataSnapshot.child(user_id).child("year").getValue(String.class) + " year " + dataSnapshot.child(user_id).child("sem").getValue(String.class) + " semester";
                yearTv.setText(year);
                String imagePath = dataSnapshot.child(user_id).child("profileImage").getValue(String.class);
                if (!imagePath.equals(""))
                    Glide.with(DashboardActivity.this)
                            .load(imagePath)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profileImage);

                boolean isAdmin = dataSnapshot.child(user_id).child("admin").getValue(boolean.class);
                MenuItem admin_menu = navigationView.getMenu().getItem(6);

                if (!isAdmin) {
                    admin_menu.setVisible(false);
                }
                boolean isSAdmin = dataSnapshot.child(user_id).child("sAdmin").getValue(boolean.class);
                MenuItem users = navigationView.getMenu().getItem(7);
                if (!isSAdmin) {
                    users.setVisible(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        Log.d("1234", "onNavigationItemSelected: " + isBlocked);
        int id = item.getItemId();
        if (id == R.id.dashboard) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Dashboard");
            floatingActionMenu.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, new WelcomeFragment());
            transaction.commit();
        }

        if (id == R.id.notices) {
            changeFragment(new NoticeFragment());
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Notices");
            if (!isBlocked) {
                floatingActionMenu.setVisibility(View.VISIBLE);
            }

        } else if (id == R.id.TimeTable) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Time Table");
            if (!isBlocked) {
                floatingActionMenu.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.AcedemicCalender) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Academic Calendar");
            changeFragment(new AcademicCalendarFragment());
            floatingActionMenu.setVisibility(View.GONE);

        } else if (id == R.id.Documents) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Documents");
            if (!isBlocked) {
                floatingActionMenu.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.result) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Check Result");
            changeFragment(new ResultFragment());
            floatingActionMenu.setVisibility(View.GONE);

        } else if (id == R.id.reviewNotice) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Review Notice");
            changeFragment(new AdminFragment());
            if (!isBlocked) {
                floatingActionMenu.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.users) {
            setSupportActionBar(dashboardToolbar);
            getSupportActionBar().setTitle("Users");
            changeFragment(new UserListingFragment());
            floatingActionMenu.setVisibility(View.GONE);

        } else if (id == R.id.logout) {
            logout();
        } else if (id == R.id.about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
        navigationView.setCheckedItem(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private void init() {
        //initialized nav view

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // fetched header from nav view
        view = navigationView.getHeaderView(0);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabGal = (FloatingActionButton) findViewById(R.id.fab_gal);
        fabDoc = (FloatingActionButton) findViewById(R.id.fab_doc);
        dashboardToolbar = (Toolbar) findViewById(R.id.toolbar);
        // these are the elements of nav header view
        // they are not initialized

        branchTv = (TextView) view.findViewById(R.id.branchTv);
        nameTv = (TextView) view.findViewById(R.id.nameTv);
        yearTv = (TextView) view.findViewById(R.id.yearTv);
        relativeLayoutFab = (RelativeLayout) findViewById(R.id.relativeLayoutFab);
        cordlay = (CoordinatorLayout) findViewById(R.id.cordLay);
        floatingActionMenu.setClosedOnTouchOutside(true);
        profileImage = (ImageView) view.findViewById(R.id.profile_image);
    }


    private void methodListener() {

        fabGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddNewNoticeActivity.class));

            }
        });

        fabDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "No Function assigned to this button.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
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



}
