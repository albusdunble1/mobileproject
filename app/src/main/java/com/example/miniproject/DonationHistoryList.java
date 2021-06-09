package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DonationHistoryList extends AppCompatActivity {

    private static final String TAG = "DonationHistoryList";
    RecyclerView lvHistory;
    ImageView imgView;
    Donation payment;
    String userID;
    //TextView date;



    //DatabaseReference reff;


//    var customers=reff.database().ref().child("Customer");
//customers.child(uid).once('value', function(snapshot) {
//        console.log(customerSnapshot.key, customerSnapshot.val());
//    });
    myAdapter  myAdapter;
    ArrayList<Donation> list;
    //ArrayList<Receiver> list2;
    //long maxID = 0;
    //StorageReference mStorageReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history_list);

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

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();
        //Log.d(TAG, "donation test: " + userID);

         DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Donation");
         //reff.orderByChild("userID").equalTo(userID);

        //end bottom navigation

        //date = findViewById(R.id.txtDate);
        //mStorageReff = FirebaseStorage.getInstance().getReference("Images");

        //reff = FirebaseDatabase.getInstance().getReference().child("Payment");
        lvHistory = findViewById(R.id.lvHistory);
        imgView = findViewById(R.id.imageView);
        lvHistory.setHasFixedSize(true);
        lvHistory.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new myAdapter(this,list);
        lvHistory.setAdapter(myAdapter);
        setTitle("Donation History");


        reff.orderByChild("userID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Donation donation = dataSnapshot.getValue(Donation.class);
                    //String img = dataSnapshot.child("imageURL").getValue().toString();
                    //Glide.with(DonationHistoryList.this).load(img).into(imgView);
                    list.add(donation);
                    final String id = donation.getReceiverID();

                    //String id2 = "-MbSb8itkhLXyU-Qxpdo";
                    Log.d(TAG, "User id test: " + id);
//                    donation.getReceiverImage();

             DatabaseReference re2 = FirebaseDatabase.getInstance().getReference().child("Receiver").child(id);
             re2.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                     //String img = snapshot.child("imageURL").getValue().toString();
                     //Log.d(TAG, "User test image: " + img);
                     //Glide.with(DonationHistoryList.this).load(img).into(imgView);



                 }

                 @Override
                 public void onCancelled(@NonNull @NotNull DatabaseError error) {

                 }
             });
//
//                    Log.d(TAG, "test: " + test);
                    Log.d(TAG, "User name: " + id);
                }
                myAdapter.notifyDataSetChanged();

            }else{
                    Toast.makeText(getApplicationContext(), "No donation has been made ", Toast.LENGTH_LONG).show();

                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        DatabaseReference test = FirebaseDatabase.getInstance().getReference().child("Donation").child();
//        test.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Donation id = snapshot.getValue(Donation.class);
//                //id.getReceiverId();
//                Log.d(TAG, "User name: " + id.getReceiverId());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                Log.w(TAG, "Failed to read value.", error.toException());
//
//            }
//        });
//


    }
}