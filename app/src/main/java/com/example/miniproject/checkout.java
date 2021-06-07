package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        final String receiverid = getIntent().getStringExtra("receiverid");
        final String rice = getIntent().getStringExtra("rice");
        final String fruit = getIntent().getStringExtra("fruit");
        final String noodle = getIntent().getStringExtra("noodle");

        Toast.makeText(checkout.this, "Receiver ID: "+ receiverid + " " +rice + " " + fruit + " " + noodle, Toast.LENGTH_SHORT).show();
    }
}