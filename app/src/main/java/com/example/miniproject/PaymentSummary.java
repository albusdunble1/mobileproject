package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentSummary extends AppCompatActivity {
    private static final String TAG = "PaymentSummary";
    TextView tvFood, tvPrice, tvName, tvIC, tvPhone, tvEmail, tvPayID, tvPayStatus, tvPayReceiverName,tvPayReceiverPhone, tvPayDate;
    String id, userID;

    protected void onCreate(Bundle savedInstanceState) {
        // Write a message to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_paysummary);
        setTitle("Donation Summary");

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

        tvName = findViewById(R.id.tvName);
        tvIC = findViewById(R.id.tvIC);
        tvPhone = findViewById(R.id.tvPhone2);
        tvEmail = findViewById(R.id.tvEmail);
        tvFood = findViewById(R.id.tvFood);
        tvPrice = findViewById(R.id.tvPrice);
        tvPayID = findViewById(R.id.tvPayID);
        tvPayStatus = findViewById(R.id.tvPayStatus);
        tvPayReceiverName = findViewById(R.id.tvPayReceiverName);
        tvPayReceiverPhone = findViewById(R.id.tvPayReceiverPhone);
        tvPayDate = findViewById(R.id.tvPayDate);

        Bundle mainExtra = getIntent().getExtras();
        if(mainExtra!=null){
            id = mainExtra.getString("id");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Donation").child(id);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String name = dataSnapshot.child("name").getValue().toString();
//                String ic = dataSnapshot.child("ic").getValue().toString();
//                String phone = dataSnapshot.child("phone").getValue().toString();

                String food = dataSnapshot.child("food").getValue().toString();
                String amount = dataSnapshot.child("amount").getValue().toString();
                String paymentID = dataSnapshot.child("paymentID").getValue().toString();
                String paymentStatus = dataSnapshot.child("paymentStatus").getValue().toString();
                String paymentDate = dataSnapshot.child("paymentDate").getValue().toString();
                String receiverName = dataSnapshot.child("receiverName").getValue().toString();
                String receiverPhone = dataSnapshot.child("receiverPhone").getValue().toString();

                tvFood.setText(food);
                tvPrice.setText("RM "+ amount);
                tvPayReceiverName.setText(receiverName);
                tvPayReceiverPhone.setText(receiverPhone);
                tvPayID.setText(paymentID);
                tvPayStatus.setText(paymentStatus);
                tvPayDate.setText(paymentDate);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(PaymentSummary.this, "Database Error Fail to read value", Toast.LENGTH_SHORT).show();
            }
        });

        //get current user authentication
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();

        //get user data in real time database
        DatabaseReference userRef = database.getReference().child("Users").child(userID);

        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String ic = dataSnapshot.child("ic").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String fullName = dataSnapshot.child("fullName").getValue().toString();

                tvName.setText(fullName);
                tvIC.setText(ic);
                tvPhone.setText(phone);
                tvEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(PaymentSummary.this, "Database Error Fail to read value", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
