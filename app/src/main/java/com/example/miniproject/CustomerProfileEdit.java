package com.example.miniproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class CustomerProfileEdit extends AppCompatActivity {
    private static final String TAG = "CustomerProfileEdit";
    TextView tvCustUsername2, tvCustEmail2;
    EditText etCustPhone, etCustIC;
    String id;
    User user;
    Button btnUpdateCustProfile, btnUpdatePass;

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
        etCustPhone = findViewById(R.id.etCustPhone);
        etCustIC = findViewById(R.id.etCustIC);
        btnUpdateCustProfile = findViewById(R.id.btnUpdateCustProfile);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);


        Bundle mainExtra = getIntent().getExtras();
        if(mainExtra!=null){
            id = mainExtra.getString("id");
        }
        user = new User();

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser authUser = fAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(id);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String email = dataSnapshot.child("email").getValue().toString();
                final String ic = dataSnapshot.child("ic").getValue().toString();
                final String phone = dataSnapshot.child("phone").getValue().toString();
                final String fullName = dataSnapshot.child("fullName").getValue().toString();

                tvCustUsername2.setText(fullName);
                tvCustEmail2.setText(email);
                etCustPhone.setText(phone);
                etCustIC.setText(ic);

                myRef.child(String.valueOf(id)).setValue(user);
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
                    map.put("fullName", tvCustUsername2.getText().toString());
                    map.put("phone", etCustPhone.getText().toString());
                    map.put("email", tvCustEmail2.getText().toString());
                    map.put("ic", etCustIC.getText().toString());

                    if(etCustPhone.getText().toString().matches("") ||  etCustIC.getText().toString().matches("")){
                        Toast.makeText(CustomerProfileEdit.this, "Please fill in the information", Toast.LENGTH_SHORT).show();
                    }else{
                        myRef.updateChildren(map);
                        Toast.makeText(CustomerProfileEdit.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent2custProfile = new Intent(CustomerProfileEdit.this, CustomerProfileView.class);
                        startActivity(intent2custProfile);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerProfileEdit.this, "Data Not Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent2custProfile = new Intent(CustomerProfileEdit.this, CustomerProfileView.class);
                    startActivity(intent2custProfile);
                }

            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText updatePass = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Change New Password ?");
                passwordResetDialog.setMessage("Enter New Password >= 6 Characters long.");
                passwordResetDialog.setView(updatePass);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = updatePass.getText().toString();
                        authUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CustomerProfileEdit.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerProfileEdit.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }
}
