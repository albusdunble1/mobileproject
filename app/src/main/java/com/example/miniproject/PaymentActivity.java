package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Button btnPay;
    EditText etFood, etAmount, etName, etIC, etPhone;
    Donation donation;
    long maxID;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnPay = findViewById(R.id.btnPay);
        etName = findViewById(R.id.etName);
        etIC = findViewById(R.id.etIC);
        etPhone = findViewById(R.id.etPhone);
        etFood = findViewById(R.id.etFood);
        etAmount = findViewById(R.id.etAmount);

        donation = new Donation();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Donation");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double amount = Double.parseDouble(etAmount.getText().toString().trim());
                donation.setName(etName.getText().toString().trim());
                donation.setIC(etIC.getText().toString().trim());
                donation.setPhone(etPhone.getText().toString().trim());
                donation.setFood(etFood.getText().toString().trim());
                donation.setAmount(amount);
                donation.setReceiverName("ABCD Yatim"); //temporarily hardcore *needs to be changed

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            maxID = (dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PaymentActivity.this, "Database Error Fail to read value", Toast.LENGTH_SHORT).show();
                    }
                });

                paymentMethod(amount);

//                Toast.makeText(PaymentActivity.this, "Data Insert Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void paymentMethod(double amount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_oWN4YlK4Lu0JRw");

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "Food Donation App");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "MYR");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //multiply with 100 to get exact amount in rupee
            amount = amount * 100;
            //amount
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "jenicehoe@gmail.com");
            //contact
            preFill.put("contact", "0123456789");

            options.put("prefill", preFill);

            checkout.open(PaymentActivity.this, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
            Toast.makeText(PaymentActivity.this, "Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Donation");
        donation.setPaymentID(razorpayPaymentID);
        donation.setPaymentStatus("Paid");
        donation.setPaymentDate(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));

        id = maxID+1;
        myRef.child(String.valueOf(id)).setValue(donation);

        Intent intent2summary = new Intent(PaymentActivity.this, PaymentSummary.class);
        intent2summary.putExtra("id", (String.valueOf(id)));

        Toast.makeText(this, "Payment Successful! Razor Payment ID:" + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        int timeout = 3000; // make the activity visible for 3 seconds

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                finish();
                startActivity(intent2summary);
            }
        }, timeout);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
    }

}