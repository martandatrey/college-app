package com.pimpmyapp.collegeapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.UserPojo;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marta on 29-Jul-17.
 */

public class UserAdapter extends ArrayAdapter {
    ArrayList<UserPojo> userList = new ArrayList<>();
    Context context;
    int layoutRes;
    LayoutInflater inflater;
    public UserAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<UserPojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.userList = objects;
        this.layoutRes = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(layoutRes,null);
        TextView name = (TextView) view.findViewById(R.id.user_name);
        TextView rollNo = (TextView) view.findViewById(R.id.userRollNo);
        TextView branch = (TextView) view.findViewById(R.id.userBranch);
        TextView year = (TextView) view.findViewById(R.id.yearTextView);
        CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        UserPojo userPojo = userList.get(position);
        name.setText(userPojo.getName());
        rollNo.setText(userPojo.getRollNo());
        branch.setText(userPojo.getBranch());
        year.setText(userPojo.getYear() + " year " + userPojo.getSem() + " semester");
        Glide.with(context)
                .load(userPojo.getProfileImage())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);

        return view;
    }
}
