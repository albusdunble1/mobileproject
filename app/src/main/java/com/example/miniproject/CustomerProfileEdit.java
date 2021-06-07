package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class CustomerProfileEdit extends AppCompatActivity {
    private static final String TAG = "CustomerProfileEdit";
    TextView tvCustUsername2, tvCustEmail2;
    EditText etCustPass, etCustPhone, etCustIC;
    String id;
    ImageView imgViewCust;
    Customer customer;
    Button btnUpdateCustProfile;

    protected void onCreate(Bundle savedInstanceState) {
        // Write a message to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        setTitle("Customer Profile");

        //Initialize and assign bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Profile selected bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.navprofile);

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

        tvCustUsername2 = findViewById(R.id.tvCustUsername2);
        tvCustEmail2 = findViewById(R.id.tvCustEmail2);
        etCustPass = findViewById(R.id.etCustPass);
        etCustPhone = findViewById(R.id.etCustPhone);
        etCustIC = findViewById(R.id.etCustIC);
        btnUpdateCustProfile = findViewById(R.id.btnUpdateCustProfile);

        Bundle mainExtra = getIntent().getExtras();
        if(mainExtra!=null){
            id = mainExtra.getString("id");
        }
        customer = new Customer();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference().child("Customer").child(id);
        DatabaseReference myRef = database.getReference().child("Customer").child(id);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue().toString();
                String ic = dataSnapshot.child("ic").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();

                tvCustUsername2.setText(username);
                tvCustEmail2.setText(email);
                etCustPass.setText(password);
                etCustPhone.setText(phone);
                etCustIC.setText(ic);

                myRef.child(String.valueOf(id)).setValue(customer);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(CustomerProfileEdit.this, "Database Error Fail to read value", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdateCustProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    HashMap map = new HashMap();
                    map.put("username", tvCustUsername2.getText().toString());
                    map.put("phone", etCustPhone.getText().toString());
                    map.put("email", tvCustEmail2.getText().toString());
                    map.put("password", etCustPass.getText().toString());
                    map.put("ic", etCustIC.getText().toString());

                    myRef.updateChildren(map);
                    Toast.makeText(CustomerProfileEdit.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent2AdminProfile = new Intent(CustomerProfileEdit.this, CustomerProfileView.class);
                    startActivity(intent2AdminProfile);
                }

                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerProfileEdit.this, "Data Not Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent2AdminProfile = new Intent(CustomerProfileEdit.this, CustomerProfileView.class);
                    startActivity(intent2AdminProfile);
                }

            }
        });
    }
}
