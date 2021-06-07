package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DonationHistoryDetails extends AppCompatActivity {
    TextView receiver, date, food, amount;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history_details);

        //Initialize and assign bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Profile selected bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.navdonationlist);

        //Perform ItemSelectedListener  bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navprofile:
                        startActivity(new Intent(getApplicationContext()
                                ,CustomerProfileView.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navreceiverlist:
                        startActivity(new Intent(getApplicationContext()
                                ,ReceiverList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navdonationlist:
                        startActivity(new Intent(getApplicationContext()
                                ,DonationHistoryList.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //end bottom navigation

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