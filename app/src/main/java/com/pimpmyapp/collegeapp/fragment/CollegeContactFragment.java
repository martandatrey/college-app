package com.pimpmyapp.collegeapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pimpmyapp.collegeapp.R;


public class CollegeContactFragment extends Fragment {

    TextView phoneOffice1, phoneOffice2, phoneProctor, phoneExam, phoneAccount, emailPrincipal, emailAccount, emailProctor, emailExam, webAdd;

    public CollegeContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_college_contact, container, false);
        phoneOffice1 = (TextView) view.findViewById(R.id.phoneOffice1);
        phoneOffice2 = (TextView) view.findViewById(R.id.phoneOffice2);
        phoneProctor = (TextView) view.findViewById(R.id.phoneProctor);
        phoneExam = (TextView) view.findViewById(R.id.phoneExam);
        phoneAccount = (TextView) view.findViewById(R.id.phoneAccount);
        emailPrincipal = (TextView) view.findViewById(R.id.emailPrincipal);
        emailAccount = (TextView) view.findViewById(R.id.emailAccount);
        emailProctor = (TextView) view.findViewById(R.id.emailProctor);
        emailExam = (TextView) view.findViewById(R.id.emailExam);
        webAdd = (TextView) view.findViewById(R.id.webAdd);

        phoneOffice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01512250948"));
                startActivity(intent);
            }
        });
        phoneOffice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01512250949"));
                startActivity(intent);
            }
        });
        phoneProctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01512250955"));
                startActivity(intent);
            }
        });
        phoneExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01512250948"));
                startActivity(intent);
            }
        });
        phoneAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01512250185"));
                startActivity(intent);
            }
        });

        emailPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"principal@cet-gov.ac.in"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        emailProctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"proctor@cet-gov.ac.in"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        emailExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"exam@cet-gov.ac.in"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        emailAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"account@cet-gov.ac.in"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        webAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cet-gov.ac.in"));
                startActivity(browserIntent);
            }
        });


        return view;
    }

}
