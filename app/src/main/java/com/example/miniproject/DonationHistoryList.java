package com.example.miniproject;

import android.os.Bundle;
import android.util.Log;
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
    //TextView date;
    DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Donation");


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


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Donation payment = dataSnapshot.getValue(Donation.class);
                    //String img = dataSnapshot.child("imageURL").getValue().toString();
                    //Glide.with(DonationHistoryList.this).load(img).into(imgView);
                    list.add(payment);
                    final String id = payment.getReceiverID();

                    //String id2 = "-MbSb8itkhLXyU-Qxpdo";
                    Log.d(TAG, "User id test: " + id);
//                    payment.getReceiverImage();

             DatabaseReference re2 = FirebaseDatabase.getInstance().getReference().child("Receiver").child(id);
             re2.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                     String img = snapshot.child("imageURL").getValue().toString();
                     Log.d(TAG, "User test image: " + img);
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