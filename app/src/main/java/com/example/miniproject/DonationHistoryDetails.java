package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DonationHistoryDetails extends AppCompatActivity {
    TextView receiver, date, food, amount;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history_details);

        final String rID = getIntent().getStringExtra("id").toString();
        final String rImg = getIntent().getStringExtra("img");
        final String rName = getIntent().getStringExtra("name");
        final String rAmt = getIntent().getStringExtra("amt").toString();
        final String rDate = getIntent().getStringExtra("date");
        final String rFood = getIntent().getStringExtra("food").toString();

        setTitle(rName);

        receiver = findViewById(R.id.tv_rname);
        date = findViewById(R.id.tv_date);
        food = findViewById(R.id.tv_food);
        amount = findViewById(R.id.tv_amt);
        img = findViewById(R.id.img_receiver);

        Glide.with(this).load(rImg).into(img);
        receiver.setText(rName);
        date.setText(rDate);
        food.setText(rFood);
        amount.setText("RM" + String.valueOf(rAmt) + "0");



    }
}