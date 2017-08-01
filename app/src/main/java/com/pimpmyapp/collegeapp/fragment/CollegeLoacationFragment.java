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


public class CollegeLoacationFragment extends Fragment {
    TextView direction;

    public CollegeLoacationFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_college_loacation, container, false);
        direction = (TextView) view.findViewById(R.id.direction);
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.co.in/maps/place/Govt+College+Of+Engineering+And+Technology/@28.0659709,73.2875746,17.31z/data=!4m12!1m6!3m5!1s0x0:0x565439ed468d8bdc!2sCET+Canteen!8m2!3d28.0656818!4d73.2896544!3m4!1s0x0:0xdf2ee22cb2b4621c!8m2!3d28.0668297!4d73.289752";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }

        });


        return view;

    }


}
