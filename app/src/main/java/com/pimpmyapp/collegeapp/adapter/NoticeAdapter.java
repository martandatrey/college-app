package com.pimpmyapp.collegeapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pimpmyapp.collegeapp.R;
import com.pimpmyapp.collegeapp.pojo.NoticePojo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.string.no;

/**
 * Created by marta on 20-Jul-17.
 */

public class NoticeAdapter extends ArrayAdapter {
    ArrayList<NoticePojo> noticeList = new ArrayList<>();
    Context context;
    int layoutRes;
    LayoutInflater inflater;
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
        View view = inflater.inflate(layoutRes,null);
        NoticePojo notice = noticeList.get(position);
        TextView title = (TextView) view.findViewById(R.id.noticeTitle);
        TextView date = (TextView) view.findViewById(R.id.noticeDate);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        title.setText(notice.getTitle());
        date.setText(notice.getDate());
        Glide.with(context).load(notice.getImage()).crossFade().into(image);
        return view;

    }
}
