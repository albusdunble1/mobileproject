package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddReceiver extends AppCompatActivity {

    Button addBtn;
    LinearLayout linearLayout;
    ImageView imgReceiver, imgUpload, imgIcon;
    TextView textView;
    EditText etName, etDesc, etPhone, etEmail, etLocation;

    ProgressDialog progressDialog;
    ProgressBar progressBar;
    Uri FilePathUri;
    int Image_Request_Code = 7;
    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receiver);

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

        storageReference = FirebaseStorage.getInstance().getReference("Receiver");
        databaseReference = FirebaseDatabase.getInstance().getReference("Receiver");

        addBtn = findViewById(R.id.addReceiverBtn);

        linearLayout = findViewById(R.id.linearImage);
        imgReceiver = findViewById(R.id.imageViewReceiver);
        imgIcon = findViewById(R.id.imgUpload);
        imgUpload = findViewById(R.id.tvUpload);

        textView = findViewById(R.id.textViewUpload);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);

        progressDialog = new ProgressDialog(AddReceiver.this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FilePathUri != null) {
                    uploadToFirebase(FilePathUri);
//                    UploadImage();
                } else {
                    Toast.makeText(AddReceiver.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgReceiver.setImageBitmap(bitmap);
                imgIcon.setImageResource(0);
                FilePathUri = data.getData();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri mUri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));

    }

    private void uploadToFirebase(Uri uri) {

        progressDialog.setTitle("Image is Uploading...");
        progressDialog.show();
        StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(uri));
        storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String receiverDataId = databaseReference.push().getKey();
                        String receiverName = etName.getText().toString().trim();
                        String receiverDesc = etDesc.getText().toString().trim();
                        String receiverPhone = etPhone.getText().toString().trim();
                        String receiverEmail = etEmail.getText().toString().trim();
                        String receiverLocation = etLocation.getText().toString().trim();

                        ReceiverData receiverData = new ReceiverData(receiverDataId, receiverName, receiverDesc, receiverPhone,
                                receiverEmail, receiverLocation, 0, uri.toString());

                        databaseReference.child(receiverDataId).setValue(receiverData);
//                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddReceiver.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);

                        progressDialog.hide();
                        etName.setText("");
                        etDesc.setText("");
                        etPhone.setText("");
                        etEmail.setText("");
                        etLocation.setText("");
                        FilePathUri = null;

                        Intent receiverDetails = new Intent(AddReceiver.this, ReceiverDetails.class);

                        receiverDetails.putExtra("id", receiverDataId);
                        receiverDetails.putExtra("desc", receiverDesc);
                        receiverDetails.putExtra("img",uri.toString());
                        receiverDetails.putExtra("name", receiverName);
                        receiverDetails.putExtra("phone", receiverPhone);
                        receiverDetails.putExtra("email", receiverEmail);
                        receiverDetails.putExtra("location", receiverLocation);

                        startActivity(receiverDetails);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog.setTitle("Image is Uploading...");
                progressDialog.show();
                //progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddReceiver.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}