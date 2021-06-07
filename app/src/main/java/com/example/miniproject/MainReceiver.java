package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainReceiver extends AppCompatActivity {

    ReceiverAdapter myReceiverAdapter;
    ArrayList<ReceiverData> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Receiver");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_receiver);

        //Initialize and assign bottom navigation Admin
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationAdmin);

        //Set Profile selected bottom Navigation Admin
        bottomNavigationView.setSelectedItemId(R.id.navreceiverlist);

        //Perform ItemSelectedListener  bottom Navigation Admin
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navprofile:
                        startActivity(new Intent(getApplicationContext()
                                ,AdminProfile.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navreceiverlist:
                        startActivity(new Intent(getApplicationContext()
                                ,MainReceiver.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navaddreceiver:
                        startActivity(new Intent(getApplicationContext()
                                ,AddReceiver.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //end bottom navigation admin

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        list = new ArrayList<>();
        myReceiverAdapter = new ReceiverAdapter(this, list);
        recyclerView.setAdapter(myReceiverAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReceiverData myReceiverData = dataSnapshot.getValue(ReceiverData.class);
                    list.add(myReceiverData);
                }
                myReceiverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//    @Override
//    public void onBackPressed() {
//        finishAffinity();
//    }
    }
}