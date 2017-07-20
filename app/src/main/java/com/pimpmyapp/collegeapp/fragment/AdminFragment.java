package com.pimpmyapp.collegeapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.adapter.NoticeAdapter;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.ArrayList;

/**
 * Created by marta on 20-Jul-17.
 */

public class AdminFragment extends Fragment {
    ArrayAdapter noticeAdapter;
    ArrayList<NoticePojo> noticeList = new ArrayList<>();
    ListView lv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment,null);
        lv = (ListView) view.findViewById(R.id.listView);
        noticeAdapter = new NoticeAdapter(getActivity(),R.layout.notice_list_item,noticeList);
        lv.setAdapter(noticeAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("notice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    NoticePojo notice = childSnapshot.getValue(NoticePojo.class);
                    noticeList.add(notice);
                }
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }


}
