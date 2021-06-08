package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class AdminProfile extends AppCompatActivity {
    //EditText name, phone;
//TextView nameView, phoneView;
    TextView txtUsername1, txtEmail1, txtPass1, txtphone1, txtic1;
    Button btnEdit, logoutadmin;
    ImageView imgView;
    //Button save,upload,choose,view;
    StorageReference mStorageReff;
    public Uri imguri;
    private StorageTask uploadTask;
    DatabaseReference reff;
    String id;
    AdminData member;
    Bitmap bitmap;
    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        logoutadmin = findViewById(R.id.logoutadmin);

        logoutadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logouta = new Intent(AdminProfile.this, admin.class);
                startActivity(logouta);
            }
        });

        //Initialize and assign bottom navigation AdminData
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationAdmin);

        //Set Profile selected bottom Navigation AdminData
        bottomNavigationView.setSelectedItemId(R.id.navprofile);

        //Perform ItemSelectedListener  bottom Navigation AdminData
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
        //end bottom navigation adminData

//        Toast.makeText(AdminProfile.this, "Firebase connected!", Toast.LENGTH_SHORT).show();
        txtUsername1 = findViewById(R.id.txtUsername1);
        txtEmail1 = findViewById(R.id.txtEmail1);
        txtPass1 = findViewById(R.id.txtPass1);
        txtphone1 = findViewById(R.id.txtphone1);
        txtic1 = findViewById(R.id.txtic1);
        //txtimg1 = findViewById(R.id.txtimg1);
        btnEdit = findViewById(R.id.btnSave);
        imgView = findViewById(R.id.imgView);
        id = String.valueOf(1);
        mStorageReff = FirebaseStorage.getInstance().getReference("Admin");
        //member = new AdminData();
        reff = FirebaseDatabase.getInstance().getReference().child("Admin").child(id);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists())
//                    maxID=(snapshot.getChildrenCount());
                String email = snapshot.child("email").getValue().toString();
                String ic = snapshot.child("ic").getValue().toString();
                String img = snapshot.child("image").getValue().toString();
                String pass = snapshot.child("password").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String name = snapshot.child("username").getValue().toString();
                //String img = snapshot.child("image").getValue().toString();
                //Glide.with(this).load(img).into(imgView);
                setTitle(name + "'s Profile");

                txtEmail1.setText(email);
                txtic1.setText(ic);
                //txtimg1.setText(img);
                txtPass1.setText(pass);
                txtphone1.setText(phone);
                txtUsername1.setText(name);
//                    Picasso.get().load(img).into(imgView);
                Glide.with(AdminProfile.this).load(img).into(imgView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());

            }

        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent editScreenIntent = new Intent(com.example.miniproject.AdminProfile.this, AdminEditProfile.class);
                Intent intent2edit = new Intent(AdminProfile.this, AdminEditProfile.class);
                intent2edit.putExtra("id", (String.valueOf(id)));


                startActivity(intent2edit);
            }
        });



    }
}