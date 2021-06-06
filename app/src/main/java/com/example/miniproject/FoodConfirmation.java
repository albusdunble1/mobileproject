package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                Intent checkout = new Intent(FoodConfirmation.this, checkout.class);
                if(!rice.isEmpty()){
                    checkout.putExtra("rice", "rice");
                }else{
                    checkout.putExtra("rice", "");
                }

                if(!fruit.isEmpty()){
                    checkout.putExtra("fruit", "fruit");
                }else{
                    checkout.putExtra("fruit", "");
                }

                if(!noodle.isEmpty()){
                    checkout.putExtra("noodle", "noodle");
                }else{
                    checkout.putExtra("noodle", "");
                }

                checkout.putExtra("receiverid", receiverid);
                startActivity(checkout);
            }
        });
    }
}