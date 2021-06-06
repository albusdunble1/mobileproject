package com.example.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditProfile extends AppCompatActivity {
    EditText username, email, phone, ic, password;
    Button save;
    ImageView imgView;
    DatabaseReference reff;
    Admin member;
    Bitmap bitmap;
    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);
        username = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.etPhone);
        ic = findViewById(R.id.etIc);
        password = findViewById(R.id.etPassword);
        save = findViewById(R.id.btnSave);
        imgView = findViewById(R.id.imgView);
        username.setText(getIntent().getStringExtra("name").toString());
        email.setText(getIntent().getStringExtra("email").toString());
        phone.setText(getIntent().getStringExtra("phone").toString());
        ic.setText(getIntent().getStringExtra("ic").toString());
        password.setText(getIntent().getStringExtra("password").toString());
//        Bitmap bitmap = getIntent().getParcelableExtra("image");
//        imgView.setImageBitmap(bitmap);


        reff = FirebaseDatabase.getInstance().getReference().child("Admin").child("1");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    HashMap map = new HashMap();
                    map.put("username", username.getText().toString());
                    map.put("phone", phone.getText().toString());
                    map.put("email", email.getText().toString());
                    map.put("password", password.getText().toString());
                    map.put("ic", ic.getText().toString());

                    reff.updateChildren(map);
                    Toast.makeText(AdminEditProfile.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent2AdminProfile = new Intent(AdminEditProfile.this,AdminProfile.class);
                    startActivity(intent2AdminProfile);
                }

                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AdminEditProfile.this, "Data Not Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent2AdminProfile = new Intent(AdminEditProfile.this,AdminProfile.class);
                    startActivity(intent2AdminProfile);
                }

            }
        });
    }
}


