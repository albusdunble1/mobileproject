package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsReceiver extends AppCompatActivity {

    ImageView image;
    TextView textDesc, textPhone, textEmail, textLocation;
    Button addBtn, editBtn, deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_receiver);

        //Initialize and assign bottom navigation Admin
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationAdmin);

        //Set Profile selected bottom Navigation Admin
        bottomNavigationView.setSelectedItemId(R.id.navreceiverlist);

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

        final String id = getIntent().getStringExtra("id").toString();
        final String desc = getIntent().getStringExtra("desc").toString();
        final String img = getIntent().getStringExtra("img");
        final String name = getIntent().getStringExtra("name").toString();
        final String phone = getIntent().getStringExtra("phone").toString();
        final String email = getIntent().getStringExtra("email");
        final String location = getIntent().getStringExtra("location").toString();

        setTitle(name);

        textDesc = findViewById(R.id.textDesc);
        textEmail = findViewById(R.id.textViewEmail);
        textPhone = findViewById(R.id.textViewPhone);
        textLocation = findViewById(R.id.textViewLocation);
        image = findViewById(R.id.receiverImg);

//        image.setImageResource(img);
        Glide.with(this).load(img).into(image);
        textDesc.setText(desc);
        textPhone.setText(phone);
        textEmail.setText(email);
        textLocation.setText(location);

        editBtn = findViewById(R.id.editReceiverBtn);
        deleteBtn = findViewById(R.id.deleteReceiverBtn);
//        addBtn = findViewById(R.id.addReceiverBtn);




        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editReceiver = new Intent(DetailsReceiver.this, EditReceiver.class);

                editReceiver.putExtra("id",id);
                editReceiver.putExtra("name",name);
                editReceiver.putExtra("desc",desc);
                editReceiver.putExtra("img",img);
                editReceiver.putExtra("phone",phone);
                editReceiver.putExtra("email",email);
                editReceiver.putExtra("location",location);
                startActivity(editReceiver);
            }
        });

        textPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phonecall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+textPhone.getText().toString()));
                startActivity(phonecall);
            }
        });

        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:"+textEmail.getText().toString()));
                startActivity(email);
            }
        });



//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addReceiver = new Intent(DetailsReceiver.this, AddReceiver.class);
//                startActivity(addReceiver);
//            }
//        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteArtist(id)){
                    Intent mainIntent = new Intent(DetailsReceiver.this, MainReceiver.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                };
            }
        });


    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Receiver").child(id);

        //removing receiver
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Receiver Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(DetailsReceiver.this, MainReceiver.class));
        finish();

    }
}