package com.pimpmyapp.collegeapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pimpmyapp.collegeapp.R;

public class PayFeeActivity extends AppCompatActivity {

    TextView firstLink, secondLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fee_activtiy);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pay Fee");
        methodListener();

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

        firstLink = (TextView) findViewById(R.id.firstLink);
        secondLink = (TextView) findViewById(R.id.secondLink);
    }

    private void methodListener() {

        firstLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cet-gov.ac.in/OnlineFees/prepay.php"));
                startActivity(browserIntent);

            }
        });

        secondLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cet-gov.ac.in/OnlineFees/1stYear.php"));
                startActivity(browserIntent);

            }
        });
    }
}
