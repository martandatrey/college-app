package com.pimpmyapp.collegeapp.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.activity.DashboardActivity;
import com.pimpmyapp.collegeapp.activity.LoginActivity;
import com.pimpmyapp.collegeapp.activity.NoticeViewActivity;
import com.pimpmyapp.collegeapp.adapter.NoticeAdapter;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.pimpmyapp.collegeapp.R.id.refresh;

/**
 * Created by marta on 20-Jul-17.
 */

public class AdminFragment extends Fragment {
    NoticeAdapter noticeAdapter;
    ArrayList<NoticePojo> noticeList = new ArrayList<>();
    ListView lv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.admin_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh){
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }
void refresh(){
lv.setSelectionAfterHeaderView();
}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.admin_fragment, null);
        lv = (ListView) view.findViewById(R.id.listView);
        noticeAdapter = new NoticeAdapter(getActivity(), R.layout.notice_list_item, noticeList);
        lv.setAdapter(noticeAdapter);

        fetchValues();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NoticePojo noticePojo = noticeList.get(position);
                Intent i = new Intent(getActivity(), NoticeViewActivity.class);
                i.putExtra("notice_id", noticePojo.getNoticeID());
                Log.d("1234", "onItemClick: " + noticePojo.getNoticeID());
                startActivity(i);
            }
        });
        return view;
    }

    public void fetchValues() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("notice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticeList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    NoticePojo notice = childSnapshot.getValue(NoticePojo.class);
                    noticeList.add(0,notice);
                }
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
