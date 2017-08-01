package com.pimpmyapp.collegeapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

public class DepartmentDetailActivity extends AppCompatActivity {
    WebView webView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Department");
        webView = (WebView) findViewById(R.id.webView);
        getBranch();


    }

    private void getBranch() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("user_id", "Anonymous");
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserPojo userPojo = dataSnapshot.child(user_id).getValue(UserPojo.class);
                String branch = userPojo.getBranch();
                webView.setWebViewClient(new WebViewClient() {
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Toast.makeText(DepartmentDetailActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                    }
                });

                webView.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        DepartmentDetailActivity.this.setProgress(progress * 1000);
                    }
                });
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(false);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.clearView();
                webView.measure(100, 100);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                switch (branch) {
                    case "Ceramic Engineering":
                        webView.loadUrl("http://cet-gov.ac.in/cre.php#tab25");
                        break;
                    case "Civil Engineering":
                        webView.loadUrl("http://cet-gov.ac.in/ce.php#tab25");
                        break;
                    case "Computer Science":
                        webView.loadUrl("http://cet-gov.ac.in/cse.php#tab25");
                        break;
                    case "Electrical Engineering":
                        webView.loadUrl("http://cet-gov.ac.in/ee.php#tab25");
                        break;
                    case "Electronics Engineering":
                        webView.loadUrl("http://cet-gov.ac.in/ece.php#tab25");
                        break;
                    case "Mechanical Engineering":
                        webView.loadUrl("http://cet-gov.ac.in/me.php#tab25");
                        break;
                }
                webView.canGoBackOrForward(1000);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void refresh() {
        webView.reload();
    }

    private void loadWebView() {


        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(DepartmentDetailActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                DepartmentDetailActivity.this.setProgress(progress * 1000);
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.clearView();
        webView.measure(100, 100);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        /*switch (uBranch) {
            case "Ceramic Engineering":
                webView.loadUrl("http://cet-gov.ac.in/cre.php#tab25");
                break;
            case "Civil Engineering":
                webView.loadUrl("http://cet-gov.ac.in/ce.php#tab25");
                break;
            case "Computer Science":
                webView.loadUrl("http://cet-gov.ac.in/cse.php#tab25");
                break;
            case "Electrical Engineering":
                webView.loadUrl("http://cet-gov.ac.in/ee.php#tab25");
                break;
            case "Electronics Engineering":
                webView.loadUrl("http://cet-gov.ac.in/ece.php#tab25");
                break;
            case "Mechanical Engineering":
                webView.loadUrl("http://cet-gov.ac.in/me.php#tab25");
                break;
        }*/
        webView.canGoBackOrForward(1000);

    }

}
