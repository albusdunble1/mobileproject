package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ReceiverDetails extends AppCompatActivity {

    DatabaseReference ref;
    StorageReference mStorage;

    Button btnDonate;
    TextView tvReceiver, tvDescription, tvPhone, tvEmail, tvAddress;
    ImageView imgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_details);



        btnDonate = findViewById(R.id.btn_donate);
        tvReceiver = findViewById(R.id.tv_receiver);
        tvDescription = findViewById(R.id.tv_description);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvAddress = findViewById(R.id.tv_address);
        imgReceiver = findViewById(R.id.img_receiver);


        setTitle("Receiver Details");

        final String receiverid =getIntent().getStringExtra("receiverid");

        //                ============== GET ONE DATA ONLY ==============

        ref = FirebaseDatabase.getInstance().getReference().child("Receiver").child(receiverid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReceiverData receiver = snapshot.getValue(ReceiverData.class);

                tvReceiver.setText(receiver.getReceiverName());
                tvDescription.setText(receiver.getReceiverDesc());
                tvPhone.setText(receiver.getReceiverPhone());
                tvEmail.setText(receiver.getReceiverEmail());
                tvAddress.setText(receiver.getReceiverLocation());

                Glide.with(ReceiverDetails.this)
                        .load(receiver.getImageURL())
                        .into(imgReceiver);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phonecall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvPhone.getText().toString()));
                startActivity(phonecall);
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:"+tvEmail.getText().toString()));
                startActivity(email);
            }
        });

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodlist = new Intent(ReceiverDetails.this, FoodList.class);
                foodlist.putExtra("receiverid", receiverid);
                startActivity(foodlist);
            }
        });

    }
}