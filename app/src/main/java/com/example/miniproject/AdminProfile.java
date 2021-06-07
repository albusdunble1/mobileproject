package com.example.miniproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class AdminProfile extends AppCompatActivity {
    //EditText name, phone;
//TextView nameView, phoneView;
    TextView txtUsername1, txtEmail1, txtPass1, txtphone1, txtic1;
    Button btnEdit;
    ImageView imgView;
    //Button save,upload,choose,view;
    StorageReference mStorageReff;
    public Uri imguri;
    private StorageTask uploadTask;
    DatabaseReference reff;
    String id;
    Admin member;
    Bitmap bitmap;
    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize and assign bottom navigation Admin
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationAdmin);

        //Set Profile selected bottom Navigation Admin
        bottomNavigationView.setSelectedItemId(R.id.navprofile);

        //Perform ItemSelectedListener  bottom Navigation Admin
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
        //end bottom navigation admin

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
        mStorageReff = FirebaseStorage.getInstance().getReference("Images");
        member = new Admin();
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

//                editScreenIntent.putExtra("name",txtUsername1.getText().toString());
//                editScreenIntent.putExtra("email",txtEmail1.getText().toString());
//                editScreenIntent.putExtra("password",txtPass1.getText().toString());
//                editScreenIntent.putExtra("ic",txtic1.getText().toString());
//                editScreenIntent.putExtra("phone",txtphone1.getText().toString());
//                imgView.buildDrawingCache();
//                Bitmap bitmap = imgView.getDrawingCache();
//                editScreenIntent.putExtra("image", bitmap);

                startActivity(intent2edit);
            }
        });


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reff = FirebaseDatabase.getInstance().getReference().child("Member").child("5");
//                reff.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String name = snapshot.child("name").getValue().toString();
//                        String phone = snapshot.child("phone").getValue().toString();
//                        nameView.setText(name);
//                        phoneView.setText(phone);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                member.setName(name.getText().toString().trim());
//                member.setPhone(phone.getText().toString().trim());
//                reff.child(String.valueOf(maxID+1)).setValue(member);
//                Toast.makeText(AdminProfile.this, "Data inserted successfully!", Toast.LENGTH_SHORT).show();
//            }
//        });


    }
}