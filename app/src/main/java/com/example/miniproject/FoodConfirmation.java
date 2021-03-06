package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FoodConfirmation extends AppCompatActivity {

    ListView lvFood;
    Button btnCheckout;
    TextView tvTotal;

    List<String> foodarr = new ArrayList<>();

    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_confirmation);

        //Initialize and assign bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Profile selected bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.navreceiverlist);

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

        lvFood = findViewById(R.id.lv_foodlist);
        btnCheckout = findViewById(R.id.btn_checkout);
        tvTotal = findViewById(R.id.tv_total);

        setTitle("Food Confirmation");


        final String receiverid = getIntent().getStringExtra("receiverid");
        final String rice = getIntent().getStringExtra("rice");
        final String fruit = getIntent().getStringExtra("fruit");
        final String noodle = getIntent().getStringExtra("noodle");


        if(!rice.isEmpty()){
            foodarr.add(rice);
            total += 10;
        }

        if(!fruit.isEmpty()){
            foodarr.add(fruit);
            total += 3;
        }
        if(!noodle.isEmpty()){
            foodarr.add(noodle);
            total += 5;
        }

        tvTotal.setText("Total:     RM " + total);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, foodarr);
        lvFood.setAdapter(arrayAdapter);


        Toast.makeText(FoodConfirmation.this, rice + " " + fruit + " " + noodle,Toast.LENGTH_SHORT).show();


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkout = new Intent(FoodConfirmation.this, PaymentActivity.class);
                if(!rice.isEmpty()){
                    checkout.putExtra("rice", "Mixed Rice (RM10)");
                }else{
                    checkout.putExtra("rice", "");
                }

                if(!fruit.isEmpty()){
                    checkout.putExtra("fruit", "Packed Fruits (RM 3)");
                }else{
                    checkout.putExtra("fruit", "");
                }

                if(!noodle.isEmpty()){
                    checkout.putExtra("noodle", "Noodle Soup (RM 5)");
                }else{
                    checkout.putExtra("noodle", "");
                }

                checkout.putExtra("total", total);
                checkout.putExtra("receiverid", receiverid);
                startActivity(checkout);
            }
        });
    }
}