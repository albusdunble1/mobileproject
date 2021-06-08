package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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


public class CustomerProfileView extends AppCompatActivity {
    private static final String TAG = "CustomerProfileView";
    TextView tvCustUsername, tvCustEmail, tvCustPhone, tvCustIC;
    String id;
    Button btnEditCustProfile, logout;

    protected void onCreate(Bundle savedInstanceState) {
        // Write a message to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        setTitle("Customer Profile");

        logout = findViewById(R.id.logout);

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

        tvCustUsername = findViewById(R.id.tvCustUsername);
        tvCustEmail = findViewById(R.id.tvCustEmail);
        tvCustPhone = findViewById(R.id.tvCustPhone);
        tvCustIC = findViewById(R.id.tvCustIC);
        btnEditCustProfile = findViewById(R.id.btnEditCustProfile);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        if (currentFirebaseUser != null) {
            id = currentFirebaseUser.getUid();
            boolean emailVerified = currentFirebaseUser.isEmailVerified();

//            Toast.makeText(this, "User ID:" + id + "\nEmail Verified: " + emailVerified, Toast.LENGTH_SHORT).show();

            if(emailVerified != true)
            {
                Toast.makeText(this, "Email not verified, Please check your inbox", Toast.LENGTH_SHORT).show();
                Intent intent2login = new Intent(CustomerProfileView.this, MainActivity.class);
                startActivity(intent2login);
            }
        } else {
            // No user is signed in
            Toast.makeText(this, "User Not Signed In:", Toast.LENGTH_SHORT).show();
            Intent intent2login = new Intent(CustomerProfileView.this, MainActivity.class);
            startActivity(intent2login);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(id);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String ic = dataSnapshot.child("ic").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String fullName = dataSnapshot.child("fullName").getValue().toString();

                tvCustUsername.setText(fullName);
                tvCustEmail.setText(email);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CustomerProfileView.this, MainActivity.class));
            }
        });

    }
}
