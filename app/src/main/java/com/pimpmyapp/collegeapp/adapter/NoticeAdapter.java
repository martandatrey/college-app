package com.pimpmyapp.collegeapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.ArrayList;

import static com.pimpmyapp.collegeapp.R.id.imageView;

/**
 * Created by marta on 20-Jul-17.
 */

public class NoticeAdapter extends ArrayAdapter {
    private ArrayList<NoticePojo> noticeList = new ArrayList<>();
    private NoticePojo notice;
    private Context context;
    private int layoutRes;
    private LayoutInflater inflater;

    public NoticeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NoticePojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        this.noticeList = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        final boolean[] isAdmin = new boolean[1];
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        SharedPreferences pref = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        final String user_id = pref.getString("user_id", "Anonymous");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 isAdmin[0] = dataSnapshot.child(user_id).child("admin").getValue(boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (convertView == null) {
            convertView = inflater.inflate(layoutRes, null);
            notice = noticeList.get(position);
            Log.d("1234", "getView: notice " + notice);
            Log.d("1234", "getView: position " + position);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.noticeTitle);
            viewHolder.date = (TextView) convertView.findViewById(R.id.noticeDate);
            viewHolder.image = (ImageView) convertView.findViewById(imageView);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.deleteIV);
            viewHolder.publishIV = (ImageView) convertView.findViewById(R.id.publishedIv);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            if (!notice.isPublished()) {
                viewHolder.publishIV.setColorFilter(Color.parseColor("#ff0000"));
            } else {
                viewHolder.publishIV.setColorFilter(Color.parseColor("#00ff00"));
            }

            if (!isAdmin[0]){
                viewHolder.publishIV.setVisibility(View.GONE);
                viewHolder.delete.setVisibility(View.GONE);
            }
                viewHolder.title.setText(notice.getTitle());
            viewHolder.date.setText(notice.getDate());
            Ion.with(context)
                    .load(notice.getImage())
                    .withBitmap()
                    .crossfade(true)
                    .smartSize(true)
                    .intoImageView(viewHolder.image)
                    .setCallback(new FutureCallback<ImageView>() {
                        @Override
                        public void onCompleted(Exception e, ImageView result) {
                            viewHolder.progressBar.setVisibility(View.GONE);
                        }
                    });


            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteNotice();
                    Toast.makeText(context, "Title " + notice.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       /* *//*View view = inflater.inflate(layoutRes, null);
        notice = noticeList.get(position);
        TextView title = (TextView) view.findViewById(R.id.noticeTitle);
        TextView date = (TextView) view.findViewById(R.id.noticeDate);
        ImageView image = (ImageView) view.findViewById(imageView);
        ImageView delete = (ImageView) view.findViewById(R.id.deleteIV);
        ImageView publishIV = (ImageView) view.findViewById(R.id.publishedIv);*//*
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);*/
        /*if (!notice.isPublished()) {
            publishIV.setColorFilter(Color.parseColor("#ff0000"));
        } else {
            publishIV.setColorFilter(Color.parseColor("#00ff00"));
        }
        title.setText(notice.getTitle());
        date.setText(notice.getDate());
        Ion.with(context)
                .load(notice.getImage())
                .withBitmap()
                .crossfade(true)
                .smartSize(true)
                .intoImageView(image)

                .setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView result) {
                        progressBar.setVisibility(View.GONE);
                    }
                });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotice();
            }
        });*/
        return convertView;
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
                Log.d("1234", "onClick: ref child notice id " + notice.getNoticeID());
                ref.child(notice.getNoticeID()).removeValue();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("notices/" + notice.getNoticeID());
                storageRef.delete();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public static class ViewHolder {
        TextView title, date;
        ImageView image, delete, publishIV;
        ProgressBar progressBar;

    }


}
