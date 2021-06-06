package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.ViewHolder>{
    Context context;
    ArrayList<ReceiverData> myReceiverData;

    public ReceiverAdapter(Context context, ArrayList<ReceiverData> myReceiverData) {
        this.context = context;
        this.myReceiverData = myReceiverData;
    }


    @NonNull
    @Override
    public ReceiverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_list_receiver, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiverAdapter.ViewHolder holder, int position) {


        ReceiverData myReceiverDataList = myReceiverData.get(position);
        holder.textViewName.setText(myReceiverDataList.getReceiverName());
//        holder.receiverImage.setImageResource(myReceiverDataList.getReceiverImage());
        holder.receiverDesc.setText(myReceiverDataList.getReceiverDesc());

        Glide.with(holder.receiverImage.getContext()).load(myReceiverDataList.getImageURL()).into(holder.receiverImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, myReceiverDataList.getReceiverName(), Toast.LENGTH_SHORT).show();
                Intent receiverDetails = new Intent(v.getContext(), DetailsReceiver.class);

                receiverDetails.putExtra("id", myReceiverDataList.getReceiverId());
                receiverDetails.putExtra("desc", myReceiverDataList.getReceiverDesc());
                receiverDetails.putExtra("img", myReceiverDataList.getImageURL());
                receiverDetails.putExtra("name", myReceiverDataList.getReceiverName());
                receiverDetails.putExtra("phone", myReceiverDataList.getReceiverPhone());
                receiverDetails.putExtra("email", myReceiverDataList.getReceiverEmail());
                receiverDetails.putExtra("location", myReceiverDataList.getReceiverLocation());

                context.startActivity(receiverDetails);
            }
        });

        holder.textViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, myReceiverDataList.getReceiverName(), Toast.LENGTH_SHORT).show();
                Intent receiverDetails = new Intent(v.getContext(), DetailsReceiver.class);

                receiverDetails.putExtra("id", myReceiverDataList.getReceiverId());
                receiverDetails.putExtra("desc", myReceiverDataList.getReceiverDesc());
                receiverDetails.putExtra("img", myReceiverDataList.getImageURL());
                receiverDetails.putExtra("name", myReceiverDataList.getReceiverName());
                receiverDetails.putExtra("phone", myReceiverDataList.getReceiverPhone());
                receiverDetails.putExtra("email", myReceiverDataList.getReceiverEmail());
                receiverDetails.putExtra("location", myReceiverDataList.getReceiverLocation());
                context.startActivity(receiverDetails);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myReceiverData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView receiverImage;
        TextView textViewName;
        TextView textViewMore;
        TextView receiverDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverImage = itemView.findViewById(R.id.receiverImage);
            textViewName = itemView.findViewById(R.id.receiverName);
            textViewMore = itemView.findViewById(R.id.receiverViewMore);
            receiverDesc = itemView.findViewById(R.id.receiverDescription);
        }
    }
}
