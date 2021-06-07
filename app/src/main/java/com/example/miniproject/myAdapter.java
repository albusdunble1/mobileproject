package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    Context context;
    ArrayList<Donation> list;
    ArrayList<ReceiverData> myReceiverData;
    //ArrayList<Receiver> list2;

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

        ReceiverData myReceiverDataList = new ReceiverData();
        holder.receiver.setText(payment.getReceiverName());
        holder.amount.setText("Donation Total: RM" + payment.getAmount().toString()+ "0");
        Glide.with(holder.receiverImage.getContext()).load(myReceiverDataList.getImageURL()).into(holder.receiverImage);

       // Glide.with(holder.receiverImage.getContext()).load(myAdapter.getImageURL()).into(holder.receiverImage);
//        Glide.with(holder.receiverImage.getContext()).load(payment.getImageURL()).into(holder.receiverImage);
        //holder.receiverImage.setImageBitmap(payment.getReceiverImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, payment.getReceiverName(), Toast.LENGTH_SHORT).show();
                Intent historyDetails = new Intent(v.getContext(), DonationHistoryDetails.class);
                historyDetails.putExtra("id", payment.getReceiverID());
                historyDetails.putExtra("img", payment.getImageURL());
                historyDetails.putExtra("name", payment.getReceiverName());
                historyDetails.putExtra("amt", payment.getAmount().toString());
                historyDetails.putExtra("date", payment.getPaymentDate());
                historyDetails.putExtra("food", payment.getFood());

                context.startActivity(historyDetails);
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
            //receiverImage = itemView.findViewById(R.id.imageView);

        }
    }



}
