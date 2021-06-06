package com.example.miniproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DonationHistoryList extends AppCompatActivity {

    RecyclerView lvHistory;
    Donation payment;
    //TextView date;
    DatabaseReference reff =FirebaseDatabase.getInstance().getReference().child("Donation");
    myAdapter  myAdapter;
    ArrayList<Donation> list;
    //long maxID = 0;
    //StorageReference mStorageReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history_list);

        //date = findViewById(R.id.txtDate);
        //mStorageReff = FirebaseStorage.getInstance().getReference("Images");

        //reff = FirebaseDatabase.getInstance().getReference().child("Payment");
        lvHistory = findViewById(R.id.lvHistory);
        lvHistory.setHasFixedSize(true);
        lvHistory.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new myAdapter(this,list);
        lvHistory.setAdapter(myAdapter);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Donation payment = dataSnapshot.getValue(Donation.class);
                    list.add(payment);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}