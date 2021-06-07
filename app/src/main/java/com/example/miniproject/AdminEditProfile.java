package com.example.miniproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.io.IOException;
import java.util.HashMap;

public class AdminEditProfile extends AppCompatActivity {
    EditText etUsername, etEmail, etPhone, etIc, etPassword;
    Button save;
    ImageView imgView, upload, choose;
    String id;
    DatabaseReference reff;
    Admin admin;
    Bitmap bitmap;
    StorageReference mStorageReff;
    public Uri imguri;
    Uri FilePathUri;
    private StorageTask uploadTask;
    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etIc = findViewById(R.id.etIc);
        etPassword = findViewById(R.id.etPassword);
        save = findViewById(R.id.btnSave);
        imgView = findViewById(R.id.imgView);
       // upload = findViewById(R.id.imgUpload);
        choose = findViewById(R.id.btnChoose);
        setTitle("Edit Profile");


       // admin = new Admin();
        //String id = getIntent().getStringExtra("id");
        Bundle mainExtra = getIntent().getExtras();
        if(mainExtra!=null){
            id = mainExtra.getString("id");
        }
        admin = new Admin();


        mStorageReff = FirebaseStorage.getInstance().getReference("Admin");
        reff = FirebaseDatabase.getInstance().getReference().child("Admin").child(id);

        // Read from the database
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue().toString();
                String ic = dataSnapshot.child("ic").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String img = dataSnapshot.child("image").getValue().toString();

                etUsername.setText(username);
                etEmail.setText(email);
                etIc.setText(ic);
                etPassword.setText(password);
                etPhone.setText(phone);

//                username.setText(getIntent().getStringExtra("name").toString());
//        email.setText(getIntent().getStringExtra("email").toString());
//        phone.setText(getIntent().getStringExtra("phone").toString());
//        ic.setText(getIntent().getStringExtra("ic").toString());
//        password.setText(getIntent().getStringExtra("password").toString());
//        password.setText(getIntent().getStringExtra("password").toString());



                //Picasso.get().load(img).into(imgView);
                Glide.with(AdminEditProfile.this).load(img).into(imgView);

                reff.child(String.valueOf(id)).setValue(admin);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(AdminEditProfile.this, "Database Error Fail to read value", Toast.LENGTH_SHORT).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    HashMap map = new HashMap();
                    map.put("username", etUsername.getText().toString());
                    map.put("phone", etPhone.getText().toString());
                    map.put("email", etEmail.getText().toString());
                    map.put("password", etPassword.getText().toString());
                    map.put("ic", etIc.getText().toString());
                    if(uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(AdminEditProfile.this, "Upload is in progress!", Toast.LENGTH_SHORT).show();

                    }else{
                        Fileuploader();
                    }
                    map.put("image", Glide.with(AdminEditProfile.this).load(uploadTask).into(imgView));
                    if(etPhone.getText().toString().matches("") || etIc.getText().toString().matches("") ||etPassword.getText().toString().matches("")) {
                        Toast.makeText(AdminEditProfile.this, "Please enter your information", Toast.LENGTH_SHORT).show();
                    }else{

                        reff.updateChildren(map);
                        Toast.makeText(AdminEditProfile.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent2AdminProfile = new Intent(AdminEditProfile.this, AdminProfile.class);
                        startActivity(intent2AdminProfile);
                    }

                }

                catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(AdminEditProfile.this, "Data Not Updated!", Toast.LENGTH_SHORT).show();
//                    Intent intent2AdminProfile = new Intent(AdminEditProfile.this,AdminProfile.class);
//                    startActivity(intent2AdminProfile);
                }

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
//
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(uploadTask != null && uploadTask.isInProgress()){
//                    Toast.makeText(AdminEditProfile.this, "Upload is in progress!", Toast.LENGTH_SHORT).show();
//                }else{
//                    Fileuploader();
//                }
//
//            }
//        });
//
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader(){
        StorageReference Ref = mStorageReff.child(System.currentTimeMillis()+"."+getExtension(imguri));
        uploadTask = Ref.putFile(imguri)

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(com.example.miniproject.AdminEditProfile.this, "Image uploaded succesfully!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgView.setImageBitmap(bitmap);
                choose.setImageResource(0);
                FilePathUri = data.getData();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "image";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
}


