package com.pimpmyapp.collegeapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.URLhelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EnterRollnoActivity extends AppCompatActivity {
    TextView title;
    Button submit;
    EditText rollNo, name, mName;
    String sRollNo, sName, sMName;
ImageButton checkBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_rollno_activity);
        init();
        methodListners();
    }

    private void methodListners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    private void verify() {
        //enter the code to set a password
        String enteredMName = mName.getText().toString().trim().toUpperCase();
        if (enteredMName.equals(sMName)) {
            Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show();
        }
    }

    private void check() {
        if (!isNetworkAvailable()) {
            Snackbar.make(submit, "No Internet Connection.", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    check();
                }
            }).show();
        }
        final ProgressDialog pdialog = new ProgressDialog(this);
        pdialog.setMessage("Fetching Details...");
        pdialog.setCancelable(true);
        pdialog.show();
        sRollNo = rollNo.getText().toString().trim().toUpperCase();
        StringRequest request = new StringRequest(Request.Method.POST, URLhelper.checkUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("1234", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        sName = jsonObject.getString("name");
                        name.setText(sName);
                        sMName = jsonObject.getString("mname");
                        findViewById(R.id.nameTIL).setVisibility(View.VISIBLE);
                        findViewById(R.id.mNameTIL).setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        pdialog.cancel();
                    } else {
                        Toast.makeText(EnterRollnoActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        pdialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EnterRollnoActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                pdialog.cancel();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("rollno", sRollNo);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void init() {
        title = (TextView) findViewById(R.id.titleTV);
        name = (EditText) findViewById(R.id.nameET);
        rollNo = (EditText) findViewById(R.id.rollNoET);
        mName = (EditText) findViewById(R.id.motherNameET);
        submit = (Button) findViewById(R.id.submitBtn1);
        checkBtn = (ImageButton)findViewById(R.id.checkBtn);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
