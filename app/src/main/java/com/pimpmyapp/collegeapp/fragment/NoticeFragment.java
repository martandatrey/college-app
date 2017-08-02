package com.pimpmyapp.collegeapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.activity.NoticeViewActivity;
import com.pimpmyapp.collegeapp.adapter.NoticeAdapter;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.ArrayList;

/**
 * Created by choud on 19-07-2017.
 */

public class NoticeFragment extends Fragment {
    NoticeAdapter noticeAdapter;
    ArrayList<NoticePojo> noticeList = new ArrayList<>();
    ListView lv;
    RecyclerView rv;
    FloatingActionMenu floatingActionMenu;

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {

        }

        return super.onOptionsItemSelected(item);
    }

    void refresh() {
        noticeList.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("notice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    NoticePojo notice = childSnapshot.getValue(NoticePojo.class);
                    if (notice.isPublished())
                        noticeList.add(notice);
                }
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lv.setSelectionAfterHeaderView();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_fragment, null);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        floatingActionMenu = (FloatingActionMenu) getActivity().findViewById(R.id.material_design_android_floating_action_menu);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        noticeAdapter = new NoticeAdapter(getActivity(),noticeList);
        rv.setAdapter(noticeAdapter);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                if (floatingActionMenu.isOpened())
                    floatingActionMenu.close(true);
                NoticePojo noticePojo = noticeList.get(position);
                Intent i = new Intent(getActivity(), NoticeViewActivity.class);
                i.putExtra("notice_id", noticePojo.getNoticeID());
                startActivity(i);
            }
        }));

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        System.out.println("The RecyclerView is not scrolling");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        System.out.println("Scrolling now");
                        if (floatingActionMenu.isOpened())
                            floatingActionMenu.close(true);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        System.out.println("Scroll Settling");
                        break;

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching Notices...");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Notice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    NoticePojo notice = childSnapshot.getValue(NoticePojo.class);
                    if (notice.isPublished())
                        noticeList.add(notice);
                }
                dialog.cancel();
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }


}
