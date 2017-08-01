package com.pimpmyapp.collegeapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

import static com.google.gson.internal.UnsafeAllocator.create;

/**
 * Created by marta on 29-Jul-17.
 */

public class UserListingFragment extends Fragment {
    ArrayAdapter userAdapter;
    ArrayList<UserPojo> userList = new ArrayList<>();
    ListView lv;
    UserPojo userPojo = new UserPojo();
    Button filter;
    Spinner catSpinner, branchSpinner;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    String branch;
    int pos;

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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                pos = position;
                userPojo = userList.get(position);
                openDialog();

            }
        });
        return view;
    }

    private void openDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_user_settings, null);
        final Dialog dialog = new Dialog(getActivity());
        TextView block = (TextView) view.findViewById(R.id.block);
        TextView admin = (TextView) view.findViewById(R.id.admin);
        TextView delete = (TextView) view.findViewById(R.id.delete);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockDialog();
                dialog.cancel();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
                dialog.cancel();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminDialog();
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void adminDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Remove Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(userPojo.getUser_id()).child("admin").setValue(false);
                Toast.makeText(getActivity(), "The User is removed as Admin", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Make Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(userPojo.getUser_id()).child("admin").setValue(true);
                Toast.makeText(getActivity(), "The User is Admin", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Delete User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(userPojo.getUser_id()).removeValue();
                Toast.makeText(getActivity(), "The User is Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void blockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Unblock", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(userPojo.getUser_id()).child("blocked").setValue(false);
                Toast.makeText(getActivity(), "The User is Unblocked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Block", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(userPojo.getUser_id()).child("blocked").setValue(true);
                Toast.makeText(getActivity(), "The User is Blocked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userPojo.getUser_id()).removeValue();
        Toast.makeText(getActivity(), "The User is Deleted", Toast.LENGTH_SHORT).show();
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
