package com.pimpmyapp.collegeapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.fragment.AdminFragment;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.ArrayList;

import static android.R.attr.fragment;

/**
 * Created by marta on 20-Jul-17.
 */

public class NoticeAdapter extends ArrayAdapter {
    private ArrayList<NoticePojo> noticeList = new ArrayList<>(), noticeShowList = new ArrayList<>();
    private NoticePojo notice;
    private Context context;
    private int layoutRes;
    private LayoutInflater inflater;

    public NoticeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NoticePojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        this.noticeList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(layoutRes, null);
        notice = noticeList.get(position);
        TextView title = (TextView) view.findViewById(R.id.noticeTitle);
        TextView date = (TextView) view.findViewById(R.id.noticeDate);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        ImageView delete = (ImageView) view.findViewById(R.id.deleteIV);
        title.setText(notice.getTitle());
        date.setText(notice.getDate());
        Glide.with(context).load(notice.getImage()).crossFade().into(image);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotice();
            }
        });
        return view;
    }

    private void deleteNotice() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notice");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm?");
        builder.setMessage("Delete this notice");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ref.child(notice.getNoticeID()).removeValue();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


}
