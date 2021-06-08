package com.example.miniproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
    ImageView imgView,choose,upload;
    String id;
    DatabaseReference reff;
    Admin admin;
    Bitmap bitmap;
    StorageReference mStorageReff;
    public Uri imguri;
    Uri FilePathUri;
    ProgressDialog progressDialog ;
    private StorageTask uploadTask;
    int Image_Request_Code = 7;
    String Storage_Path = "Admin";
    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);

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

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etIc = findViewById(R.id.etIc);
        etPassword = findViewById(R.id.etPassword);
        save = findViewById(R.id.btnSave);
        imgView = findViewById(R.id.imgView);
        upload = findViewById(R.id.btnUpload);
        choose = findViewById(R.id.btnChoose);


       // admin = new Admin();
        //String id = getIntent().getStringExtra("id");
        Bundle mainExtra = getIntent().getExtras();
        if(mainExtra!=null){
            id = mainExtra.getString("id");
        }
        //admin = new Admin();

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

                setTitle("Edit Profile");

                etUsername.setText(username);
                etEmail.setText(email);
                etIc.setText(ic);
                etPassword.setText(password);
                etPhone.setText(phone);
                upload.setVisibility(View.GONE);

//
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

                    if(etPhone.getText().toString().matches("") || etIc.getText().toString().matches("") || etPassword.getText().toString().matches("") ||imgView.getDrawable()==null){
                        Toast.makeText(AdminEditProfile.this, "Please provide the information", Toast.LENGTH_SHORT).show();
                    }else{

                        reff.updateChildren(map);
                        Toast.makeText(AdminEditProfile.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent2AdminProfile = new Intent(AdminEditProfile.this, AdminProfile.class);
                        startActivity(intent2AdminProfile);
                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AdminEditProfile.this, "Data Not Updated!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage(FilePathUri);

            }
        });

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage(Uri FilePathUri) {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
           // progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            //progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = mStorageReff.child(Storage_Path + System.currentTimeMillis() + "." + getExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            //String TempImageName = "test";

                            // Hiding the progressDialog after done uploading.
                            //progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

//                            @SuppressWarnings("VisibleForTests")
                            //Admin imageUploadInfo = new Admin(taskSnapshot.getStorage().getDownloadUrl().toString());

                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                            if (downloadUri.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Succes token", Toast.LENGTH_LONG).show();
                                String generatedFilePath = downloadUri.getResult().toString();

                                @SuppressWarnings("VisibleForTests")
                                Admin imageUploadInfo = new Admin(generatedFilePath);
//
//                                // Getting image upload ID.
                                String ImageUploadId = reff.push().getKey();

                                // Adding image upload id s child element into databaseReference.

                                reff.child("image").setValue(imageUploadInfo);
                                //System.out.println("## Stored path is "+generatedFilePath);
                            }
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(AdminEditProfile.this, exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            //progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(AdminEditProfile.this, "Please Select An Image", Toast.LENGTH_LONG).show();

        }
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
                        Toast.makeText(com.example.miniproject.AdminEditProfile.this, "Image updated succesfully!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Image_Request_Code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Image_Request_Code && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgView.setImageBitmap(bitmap);
//                choose.setImageResource(0);
                upload.setVisibility(View.VISIBLE);
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


