package com.pimpmyapp.collegeapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pimpmyapp.collegeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class OtpVerifyActivity extends AppCompatActivity {
    final int otpCode = (int) ((Math.random() * 10000 + 1));
    EditText otpET;
    Button submit;
    URL otp_url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        init();
        Intent i = getIntent();
        final String phno = i.getStringExtra("phno");
        //get the phone number from the register activity
        String authKey = "165801AJKlRCGJjv596dbe2b";
        final String otp_url = "https://control.msg91.com/api/sendotp.php?authkey=165801AJKlRCGJjv596dbe2b&mobile=91" + phno + "&message=Your%20otp%20for%20College%20Board%20account%20is%20" + otpCode + "&sender=CBoard&otp=" + otpCode;
        Log.d("1234", "onResponse: " + otp_url);
        Log.d("1234", "onResponse: " + otpCode);

        //create a string request for the otp link
        final StringRequest request = new StringRequest(Request.Method.GET, otp_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("1234", "onResponse: " + response);
                Log.d("1234", "onResponse: " + otp_url);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String success = jsonObj.getString("type");
                    if (success.equals("success")) {
                        Toast.makeText(OtpVerifyActivity.this, "Otp was sent to your mobile " + phno, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("error")) {
                        Toast.makeText(OtpVerifyActivity.this, "" + jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OtpVerifyActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        //check fot the sent otp
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOtp = (otpET.getText().toString());
                String otpCodeString = Integer.toString(otpCode);
                if (enteredOtp.equals(otpCodeString)) {
                    Toast.makeText(OtpVerifyActivity.this, "OTP verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OtpVerifyActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(OtpVerifyActivity.this, "Entered otp is not correct.", Toast.LENGTH_SHORT).show();
                }



            }
        });
        RequestQueue queue = Volley.newRequestQueue(OtpVerifyActivity.this);
        queue.add(request);
        /*try {
            otp_url = new URL("https://control.msg91.com/api/sendotp.php?authkey="+authKey+"&mobile=91"+phno+"&message="+otpCode+"%20is%20the%20otp%20to%20verify%20your%20college%20board%20account.&sender=CollegeBoard");
        } catch (MalformedURLException e) {
            Toast.makeText(this, "malformed url", Toast.LENGTH_SHORT).show();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new sendOtpnew().execute(view);

            }
        });*/
    }

    private void init() {
        otpET = (EditText) findViewById(R.id.enterOtpET);
        submit = (Button) findViewById(R.id.submitBtn);
    }

    private class sendOtpnew extends AsyncTask<View, Void, Void> {
        @Override
        protected Void doInBackground(View... view) {
            try {
                otp_url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String enteredOtp = (otpET.getText().toString());
            if (enteredOtp.equals(otpCode)) {
                startActivity(new Intent(OtpVerifyActivity.this, DashboardActivity.class));
                finish();
            } else {
                Snackbar.make(view[0], "OTP donot match", Snackbar.LENGTH_LONG).setAction("Resend", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            otp_url.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            return null;
        }
    }
}
