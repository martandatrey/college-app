package com.pimpmyapp.collegeapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.adapter.UserAdapter;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

import java.util.ArrayList;

/**
 * Created by marta on 29-Jul-17.
 */

public class UserListingFragment extends Fragment {
    ArrayAdapter userAdapter;
    ArrayList<UserPojo> userList = new ArrayList<>();
    ListView lv;
    Button filter;
    Spinner catSpinner, branchSpinner;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    String branch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_listing_fragment, null);
        lv = (ListView) view.findViewById(R.id.listView);
        catSpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        branchSpinner = (Spinner) view.findViewById(R.id.branchSpinner);
        filter = (Button) view.findViewById(R.id.filter);
        userAdapter = new UserAdapter(getActivity(), R.layout.user_list_item, userList);
        lv.setAdapter(userAdapter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String selected = catSpinner.getSelectedItem().toString();
                branch = branchSpinner.getSelectedItem().toString();
                switch (selected) {
                    case "All Users":
                        Toast.makeText(getActivity(), "Showing All Users", Toast.LENGTH_SHORT).show();
                        fetchAllUsers();
                        break;
                    case "Wants to be Admin":
                        Toast.makeText(getActivity(), "Users who want to be Admins", Toast.LENGTH_SHORT).show();
                        fetchWTBAUsers();
                        break;
                    case "Admins":
                        Toast.makeText(getActivity(), "Showing All Admins", Toast.LENGTH_SHORT).show();
                        fetchAllAdminusers();
                        break;
                }
                if (selected.equals("Branch")) {
                    branchSpinner.setVisibility(View.VISIBLE);
                    branch = branchSpinner.getSelectedItem().toString();
                    fetchSelectedBranchUsers();
                }
            }
        });
        return view;
    }

    private void fetchSelectedBranchUsers() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    UserPojo userPojo = childSnapshot.getValue(UserPojo.class);
                    if (userPojo.getBranch().equals(branch))
                        userList.add(userPojo);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchAllAdminusers() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    UserPojo userPojo = childSnapshot.getValue(UserPojo.class);
                    if (userPojo.isAdmin())
                        userList.add(userPojo);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWTBAUsers() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    UserPojo userPojo = childSnapshot.getValue(UserPojo.class);
                    if (userPojo.isWantsTobeAdmin())
                        userList.add(userPojo);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllUsers() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    UserPojo userPojo = childSnapshot.getValue(UserPojo.class);
                    userList.add(userPojo);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
