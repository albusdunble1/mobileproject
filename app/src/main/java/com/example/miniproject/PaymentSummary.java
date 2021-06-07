package com.example.miniproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentSummary extends AppCompatActivity {
    private static final String TAG = "PaymentSummary";
    TextView tvFood, tvPrice, tvName, tvIC, tvPhone, tvPayID, tvPayStatus, tvPayReceiverName,tvPayReceiverPhone, tvPayDate;
    String id;

    protected void onCreate(Bundle savedInstanceState) {
        // Write a message to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_paysummary);

        tvName = findViewById(R.id.tvName);
        tvIC = findViewById(R.id.tvIC);
        tvPhone = findViewById(R.id.tvPhone);
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
                String name = dataSnapshot.child("name").getValue().toString();
                String ic = dataSnapshot.child("ic").getValue().toString();
                String food = dataSnapshot.child("food").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String amount = dataSnapshot.child("amount").getValue().toString();
                String paymentID = dataSnapshot.child("paymentID").getValue().toString();
                String paymentStatus = dataSnapshot.child("paymentStatus").getValue().toString();
                String paymentDate = dataSnapshot.child("paymentDate").getValue().toString();
                String receiverName = dataSnapshot.child("receiverName").getValue().toString();
                String receiverPhone = dataSnapshot.child("receiverPhone").getValue().toString();


<<<<<<< Updated upstream
                tvName.setText(name);
                tvIC.setText(ic);
                tvPhone.setText(food);
                tvFood.setText(phone);
                tvPrice.setText("RM "+ amount);
                tvPayReceiverName.setText(receiverName);
                tvPayReceiverPhone.setText(receiverPhone);
                tvPayID.setText(paymentID);
                tvPayStatus.setText(paymentStatus);
                tvPayDate.setText(paymentDate);
=======
                tvName.
                        setText("      Donor Name:     " + name);
                tvIC.
                        setText("                  IC No:     " + ic);
                tvPhone.
                        setText("          Phone No:     " + phone);
                tvFood.
                        setText("   Food Donated:   " + food);
                tvPrice.
                        setText("    Total Amount:     RM "+ amount);
                tvPayReceiverName.
                        setText(" Receiver Name:     " + receiverName);
                tvPayReceiverPhone.
                        setText(" Receiver Phone:    " + receiverPhone);
                tvPayID.
                        setText("        Payment ID:    " + paymentID);
                tvPayStatus.
                        setText("Payment Status:    " + paymentStatus);
                tvPayDate.
                        setText("   Payment Date:   " + paymentDate);
>>>>>>> Stashed changes
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
