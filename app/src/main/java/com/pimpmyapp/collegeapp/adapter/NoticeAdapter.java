package com.pimpmyapp.collegeapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

/*
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
    private  class ViewHolder {
        TextView title, date;
        ImageView image, delete, publishIV;
        ProgressBar progressBar;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        final boolean[] isAdmin = new boolean[1];
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        notice = (NoticePojo) getItem(position);


        if (convertView == null) {
            convertView = inflater.inflate(layoutRes, null);
            Log.d("1234", "getView: notice " + notice);
            Log.d("1234", "getView: position " + position);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.noticeTitle);
            viewHolder.date = (TextView) convertView.findViewById(R.id.noticeDate);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.deleteIV);
            viewHolder.publishIV = (ImageView) convertView.findViewById(R.id.publishedIv);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
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

        if (!notice.isPublished()) {
            viewHolder.publishIV.setColorFilter(Color.parseColor("#ff0000"));
        } else {
            viewHolder.publishIV.setColorFilter(Color.parseColor("#00ff00"));
        }


        SharedPreferences pref = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        final String user_id = pref.getString("user_id", "Anonymous");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Log.d("1234", "ref: " + ref);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isAdmin[0] = dataSnapshot.child(user_id).child("admin").getValue(boolean.class);

                if (!isAdmin[0]){
                    viewHolder.publishIV.setVisibility(View.GONE);
                    viewHolder.delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotice();
                Toast.makeText(context, "Title " + notice.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
       */
/* *//*
*/
/*View view = inflater.inflate(layoutRes, null);
        notice = noticeList.get(position);
        TextView title = (TextView) view.findViewById(R.id.noticeTitle);
        TextView date = (TextView) view.findViewById(R.id.noticeDate);
        ImageView image = (ImageView) view.findViewById(imageView);
        ImageView delete = (ImageView) view.findViewById(R.id.deleteIV);
        ImageView publishIV = (ImageView) view.findViewById(R.id.publishedIv);*//*
*/
/*
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);*//*

        */
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
        });*//*

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
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("notices/" + notice.getNoticeID());
                storageRef.delete();
                ref.child(notice.getNoticeID()).removeValue();

            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


}
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
        if (!noticePojo.isPublished()) {
           holder.publishIV.setColorFilter(Color.parseColor("#ff0000"));
        } else {
            holder.publishIV.setColorFilter(Color.parseColor("#00ff00"));
        }
        holder.title.setText(noticePojo.getTitle());
        holder.date.setText(noticePojo.getDate());
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

        TextView title, date;
        ImageView image, publishIV;
        ProgressBar progressBar;
        public PersonViewHolder(View itemView) {
            super(itemView);
           title = (TextView) itemView.findViewById(R.id.noticeTitle);
           date = (TextView) itemView.findViewById(R.id.noticeDate);
           image = (ImageView) itemView.findViewById(imageView);
           publishIV = (ImageView) itemView.findViewById(R.id.publishedIv);
           progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

    }

    private void deleteNotice() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notice");
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
                Log.d("1234", "onClick: ref child notice id " + noticePojo.getNoticeID());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("Notice/" + noticePojo.getNoticeID());
                storageRef.delete();
                ref.child(noticePojo.getNoticeID()).removeValue();

            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
