package com.example.miniproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class CustomAdapter extends ArrayAdapter {

    final Context context;
    final Object receiverName[];
    final Object receiverImg[];

    private LayoutInflater inflater;

    public CustomAdapter(@NonNull Context context, Object receiverId[], Object receiverImg[], Object receiverName[]) {
        super(context, R.layout.custom_layout, receiverId);

        this.context = context;
        this.receiverName = receiverName;
        this.receiverImg = receiverImg;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.custom_layout, parent, false);
        }

        TextView title = convertView.findViewById(R.id.tv_receivername);
        ImageView image = convertView.findViewById(R.id.img_receiver2);


        title.setText(receiverName[position].toString());
        Glide.with(context)
                .load(receiverImg[position].toString())
                .into(image);

        System.out.println("SHOULD ONLY BE CALLED ONCE");

        return convertView;
    }
}