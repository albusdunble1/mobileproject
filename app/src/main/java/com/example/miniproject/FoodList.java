package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    Button btnDonateFood;
    CheckBox cbRice, cbFruit, cbNoodle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        btnDonateFood = findViewById(R.id.btn_donatefood);
        cbRice = findViewById(R.id.rice);
        cbFruit = findViewById(R.id.fruits);
        cbNoodle = findViewById(R.id.noodle);

        setTitle("Food List");


        final String receiverid = getIntent().getStringExtra("receiverid");

        btnDonateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRice.isChecked() || cbFruit.isChecked() || cbNoodle.isChecked()) {
                    Intent foodconfirm = new Intent(FoodList.this, FoodConfirmation.class);

                    if (cbRice.isChecked()) {
                        foodconfirm.putExtra("rice", "Mixed Rice (RM 10)");
                    } else {
                        foodconfirm.putExtra("rice", "");
                    }

                    if (cbFruit.isChecked()) {
                        foodconfirm.putExtra("fruit", "Packed Fruits (RM 3)");
                    } else{
                        foodconfirm.putExtra("fruit", "");
                    }

                    if (cbNoodle.isChecked()) {
                        foodconfirm.putExtra("noodle", "Noodle Soup (RM 5)");
                    }else {
                        foodconfirm.putExtra("noodle", "");
                    }



                    foodconfirm.putExtra("receiverid", receiverid);
                    startActivity(foodconfirm);
                }else{
                    Toast.makeText(FoodList.this, "Please select one type of food!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}