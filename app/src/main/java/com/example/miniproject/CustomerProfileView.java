package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class CustomerProfileView extends AppCompatActivity {
    private static final String TAG = "CustomerProfileView";
    TextView tvCustUsername, tvCustEmail, tvCustPass, tvCustPhone, tvCustIC;
    String id;
    ImageView imgViewCust;
    Button btnEditCustProfile;

    protected void onCreate(Bundle savedInstanceState) {
        // Write a message to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);



        //Initialize and assign bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Profile selected
        bottomNavigationView.setSelectedItemId(R.id.navprofile);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navprofile:
                        startActivity(new Intent(getApplicationContext()
                                ,CustomerProfileView.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navfoodlist:
                        startActivity(new Intent(getApplicationContext()
                                ,FoodList.class));
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

        tvCustUsername = findViewById(R.id.tvCustUsername);
        tvCustEmail = findViewById(R.id.tvCustEmail);
        tvCustPass = findViewById(R.id.tvCustPass);
        tvCustPhone = findViewById(R.id.tvCustPhone);
        tvCustIC = findViewById(R.id.tvCustIC);
        btnEditCustProfile = findViewById(R.id.btnEditCustProfile);

//        Bundle mainExtra = getIntent().getExtras();
//        if(mainExtra!=null){
//            id = mainExtra.getString("id");
//        }
        id = String.valueOf(1);

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



                tvCustUsername.setText(username);
                tvCustEmail.setText(email);
                tvCustPass.setText(password);
                tvCustPhone.setText(phone);
                tvCustIC.setText(ic);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(CustomerProfileView.this, "Database Error Fail to read value", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditCustProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2edit = new Intent(CustomerProfileView.this, CustomerProfileEdit.class);
                intent2edit.putExtra("id", (String.valueOf(id)));
                startActivity(intent2edit);
            }
        });


    }
}
