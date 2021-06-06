package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReceiverList extends AppCompatActivity {


    private ListView lvReceivers;
    String link = "";


    //test
    private Button btnTest, btnTest2;
    private EditText edTest;

    DatabaseReference ref;
    StorageReference mStorage;
    ReceiverData receiver;
    long maxid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_list);

        setTitle("Receiver List");

        // test
        edTest = findViewById(R.id.ed_test);
        btnTest = findViewById(R.id.btn_test);
        btnTest2 = findViewById(R.id.btn_test2);

        lvReceivers = findViewById(R.id.lv_receivers);



        receiver = new ReceiverData();
//        ========= TO TRACK THE DATA ID =========
        ref = FirebaseDatabase.getInstance().getReference().child("Receiver");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxid= (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ============== GET ONE DATA ONLY ==============

//                ref = FirebaseDatabase.getInstance().getReference().child("Receiver").child("2");
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String name = snapshot.child("receiverName").getValue().toString();
//                        String phone = snapshot.child("receiverPhone").getValue().toString();
//                        String address = snapshot.child("receiverAddress").getValue().toString();
//
//                        Toast.makeText(AdminProfile.this, name + " " + phone + " " + address , Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


//                ============== GET ALL DATA ==============
//                ref = FirebaseDatabase.getInstance().getReference().child("Receiver");
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds: snapshot.getChildren()) {
//                            Receiver receiver = ds.getValue(Receiver.class);
//                            System.out.println(receiver.getReceiverName());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });


//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                receiver.setReceiverName(edTest.getText().toString().trim());
//                receiver.setReceiverPhone("01231232");
//                receiver.setReceiverDescription("Old folks home");
//                receiver.setReceiverEmail("glorydays@gmail.com");
//                receiver.setReceiverImage("https://ewscripps.brightspotcdn.com/dims4/default/11d2f13/2147483647/strip/true/crop/992x558+0+69/resize/1280x720!/quality/90/?url=https%3A%2F%2Fewscripps.brightspotcdn.com%2F48%2Fa0%2Fe37d8a024adeb3176014b1ab60e3%2F6.jpg");
////                receiver.setReceiverImage("foster-care-main.jpg");
//                ref.child(String.valueOf(maxid+1)).setValue(receiver);
//
//                Toast.makeText(ReceiverList.this, "Data inserted!", Toast.LENGTH_LONG).show();
//            }
//        });


//                  ============== GET ALL DATA ==============
        List<String> rnames = new ArrayList<>();
        List<String> rimage = new ArrayList<>();
        List<String> rid = new ArrayList<>();


        ref = FirebaseDatabase.getInstance().getReference().child("Receiver");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rnames.clear();
                rimage.clear();
                rid.clear();


                for (DataSnapshot ds: snapshot.getChildren()) {
                    ReceiverData receiver = ds.getValue(ReceiverData.class);
                    System.out.println(ds.getKey());

//                    ============ retrieve image from cloud storage using file name only =============
//                    mStorage = FirebaseStorage.getInstance().getReference();
//                    mStorage.child(receiver.getReceiverImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            // Got the download URL for 'users/me/profile.png'
//                            rnames.add(receiver.getReceiverName());
//                            rimage.add(uri.toString());
//
//                            System.out.println("HELOOOOO");
//
//                            Object [] receiverName = rnames.toArray();
//                            Object [] receiverImg = rimage.toArray();
//                            System.out.println("CALLED SO MANY TIMES LOL");
//
//                            System.out.println("HEREEEEE");
//                            System.out.println(rnames.toString());
//                            System.out.println("HEREEEEE");
//
//                            CustomAdapter arrayAdapter1 = new CustomAdapter(AdminProfile.this, receiverName, receiverImg);
//                            lvReceivers.setAdapter(arrayAdapter1);
//
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle any errors
//                        }
//                    });

//                    ============ retrieve image url from realtime database =============
                    rnames.add(receiver.getReceiverName());
                    rimage.add(receiver.getImageURL());
                    rid.add(ds.getKey());

                }


                Object [] receiverName = rnames.toArray();
                Object [] receiverImg = rimage.toArray();
                Object [] receiverId = rid.toArray();

                System.out.println(rnames.toString());
                System.out.println("ONCE ONLY");

                CustomAdapter arrayAdapter1 = new CustomAdapter(ReceiverList.this, receiverId, receiverImg, receiverName);
                lvReceivers.setAdapter(arrayAdapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        lvReceivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = (String) lvReceivers.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(), "ListView: Receiver ID is " + itemValue, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ReceiverList.this, ReceiverDetails.class);
                intent.putExtra("receiverid", itemValue);

                startActivity(intent);

            }
        });

    }
}