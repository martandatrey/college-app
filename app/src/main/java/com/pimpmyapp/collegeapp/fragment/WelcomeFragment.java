package com.pimpmyapp.collegeapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.activity.CollegeDetailActivity;
import com.pimpmyapp.collegeapp.activity.DepartmentDetailActivity;
import com.pimpmyapp.collegeapp.activity.PayFeeActivity;
import com.pimpmyapp.collegeapp.activity.SyllabusActivity;
import com.pimpmyapp.collegeapp.activity.UserProfileActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by marta on 22-Jul-17.
 */

public class WelcomeFragment extends Fragment {
    RelativeLayout userProfileLay;
    LinearLayout collegeLay, departmentsLay, payFeeLay, syllabusLay;
    TextView userName, userRollNo, userBranch;
    String user_id;
    ImageView userImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.welcome_fragment,null);

        userProfileLay = (RelativeLayout) view.findViewById(R.id.user_profileLay);
        collegeLay = (LinearLayout) view.findViewById(R.id.collegeLay);
        departmentsLay = (LinearLayout) view.findViewById(R.id.departmentsLay);
        payFeeLay = (LinearLayout) view.findViewById(R.id.payFeeLay);
        userName = (TextView) view.findViewById(R.id.user_name);
        userRollNo = (TextView) view.findViewById(R.id.userRollNo);
        userBranch = (TextView) view.findViewById(R.id.userBranch);
        userImage = (ImageView) view.findViewById(R.id.profile_image);
        syllabusLay = (LinearLayout) view.findViewById(R.id.syllabusLay);

        setValues();
        methodListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setValues();

    }

    public void setValues() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "Anonymous");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child(user_id).child("name").getValue(String.class));
                userBranch.setText(dataSnapshot.child(user_id).child("branch").getValue(String.class));
                userRollNo.setText(dataSnapshot.child(user_id).child("rollNo").getValue(String.class));
                String imagePath = dataSnapshot.child(user_id).child("profileImage").getValue(String.class);
                if (!imagePath.equals(""))
                    Glide.with(getActivity())
                            .load(imagePath)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(userImage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void methodListener() {
        userProfileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
            }
        });

        collegeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CollegeDetailActivity.class));
            }
        });

        payFeeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PayFeeActivity.class));
            }
        });

        departmentsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DepartmentDetailActivity.class));
            }
        });

        syllabusLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SyllabusActivity.class));
            }
        });


    }
}
