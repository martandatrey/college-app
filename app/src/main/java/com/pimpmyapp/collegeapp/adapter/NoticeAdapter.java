package com.pimpmyapp.collegeapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import java.util.ArrayList;

import static com.pimpmyapp.collegeapp.R.id.content_main;
import static com.pimpmyapp.collegeapp.R.id.imageView;

/**
 * Created by marta on 20-Jul-17.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.PersonViewHolder>{

    private ArrayList<NoticePojo> noticePojoList = new ArrayList<>();
    private Context context;

    NoticePojo noticePojo;

    public NoticeAdapter( Context context,ArrayList<NoticePojo> noticePojoList) {
        this.noticePojoList = noticePojoList;
        this.context = context;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notice_list_item,null);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        return personViewHolder;


    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int i) {
         noticePojo = noticePojoList.get(i);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("user_id","Anonymous");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isAdmin = dataSnapshot.child(user_id).child("admin").getValue(boolean.class);
                if(!isAdmin){
                    holder.publishIV.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (!noticePojo.isPublished()) {
           holder.publishIV.setColorFilter(Color.parseColor("#ff0000"));
        } else {
            holder.publishIV.setColorFilter(Color.parseColor("#00ff00"));
        }
        holder.title.setText(noticePojo.getTitle());
        holder.date.setText(noticePojo.getDate());
        holder.des.setText(noticePojo.getDesc());
        if (noticePojo.getImage().equals("")) {
            holder.image.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.des.setVisibility(View.GONE);
            holder.image.setImageDrawable(null);
            Ion.with(context)
                    .load(noticePojo.getImage())
                    .withBitmap()
                    .crossfade(true)
                    .smartSize(true)
                    .intoImageView(holder.image)
                    .setCallback(new FutureCallback<ImageView>() {
                        @Override
                        public void onCompleted(Exception e, ImageView result) {
                            holder.progressBar.setVisibility(View.GONE);

                        }
                    });
        }
       /* Glide.with(context)
                .load(noticePojo.getImage())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        holder.progressBar.setVisibility(View.GONE);*/
    }


   /* @Override
    public void onViewRecycled(PersonViewHolder holder) {
        holder.image.setImageDrawable(null);
    }*/

    @Override
    public int getItemCount() {
        return noticePojoList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, des;
        ImageView image, publishIV;
        ProgressBar progressBar;
        public PersonViewHolder(View itemView) {
            super(itemView);
           title = (TextView) itemView.findViewById(R.id.noticeTitle);
           date = (TextView) itemView.findViewById(R.id.noticeDate);
           image = (ImageView) itemView.findViewById(imageView);
           publishIV = (ImageView) itemView.findViewById(R.id.publishedIv);
           progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            des = (TextView) itemView.findViewById(R.id.noticeDes);
        }

    }

}
