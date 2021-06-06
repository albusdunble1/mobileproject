package com.example.miniproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    Context context;
    ArrayList<Donation> list;

    public myAdapter(Context context, ArrayList<Donation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.MyViewHolder holder, int position) {
        Donation payment = list.get(position);
        holder.receiver.setText(payment.getReceiver());
        holder.amount.setText(payment.getAmount().toString());
//        Glide.with(holder.receiverImage.getContext()).load(payment.getImageURL()).into(holder.receiverImage);
        //holder.receiverImage.setImageBitmap(payment.getReceiverImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, payment.getReceiver(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView receiver;
        TextView amount;
        ImageView receiverImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            receiver = itemView.findViewById(R.id.textView);
            amount = itemView.findViewById(R.id.txtAmt);
            receiverImage = itemView.findViewById(R.id.imageView);
        }
    }


}
