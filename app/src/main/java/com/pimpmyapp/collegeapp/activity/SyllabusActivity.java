package com.pimpmyapp.collegeapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.pimpmyapp.collegeapp.R;

public class SyllabusActivity extends AppCompatActivity {

    Spinner yearSpinner;
    Button downloadBtn;
    String selectedYear;
    String selectedBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        init();
        methodListener();
        getSupportActionBar().setTitle("Syllabus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        downloadBtn = (Button) findViewById(R.id.downloadBtn);
    }

    private void methodListener() {


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedYear = yearSpinner.getSelectedItem().toString();
                switch (selectedYear) {
                    case "I Year(All Branch)":
                        Intent allIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2017/07/Syllabus-B.Tech-1st-year.pdf"));
                        startActivity(allIntent);
                        break;
                    case "Ceramic Engineering":
                        Intent ceramicIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2015/05/BTech-Ceramic-Syllabus.pdf"));
                        startActivity(ceramicIntent);
                        break;
                    case "Civil Engineering":
                        Intent civilIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2015/05/BTech_Civil_syllabi_12-13.pdf"));
                        startActivity(civilIntent);
                        break;
                    case "Computer Science":
                        Intent computerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2015/10/CS_3_8_syllabus%2007102015.pdf"));
                        startActivity(computerIntent);
                    case "Electrical Engineering":
                        Intent eeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2015/05/BTech_Syllabus-EE-3-8-Sem.pdf"));
                        startActivity(eeIntent);
                        break;
                    case "Electronics Engineering":
                        Intent eceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2015/05/BTech_Syllabus-EEE-3-8-Sem.pdf"));
                        startActivity(eceIntent);
                        break;
                    case "Mechanical Engineering":
                        Intent meIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rtu.ac.in/RTU/wp-content/uploads/2015/05/BTech-Mech-Syllabus12_13.pdf"));
                        startActivity(meIntent);
                        break;
                }
            }
        });
    }

}
