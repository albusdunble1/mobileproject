package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditReceiver extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etLocation, etDesc;
    TextView textView;
    ImageView imageView, imgIcon;
    Button editBtn;

    ProgressDialog progressDialog;
    ProgressBar progressBar;
    Uri FilePathUri = null;
    int Image_Request_Code = 7;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String id;

    String currentPhotoPath;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    final int PIC_CROP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receiver);

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


        id = getIntent().getStringExtra("id").toString();
        final String desc = getIntent().getStringExtra("desc").toString();
        final String img = getIntent().getStringExtra("img");
        final String name = getIntent().getStringExtra("name").toString();
        final String phone = getIntent().getStringExtra("phone").toString();
        final String email = getIntent().getStringExtra("email").toString();
        final String location = getIntent().getStringExtra("location").toString();

        storageReference = FirebaseStorage.getInstance().getReference("Receiver");
        databaseReference = FirebaseDatabase.getInstance().getReference("Receiver").child(id);

        progressDialog = new ProgressDialog(EditReceiver.this);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        imageView = findViewById(R.id.imgIcon);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        imgIcon = findViewById(R.id.imgUpload);
        textView = findViewById(R.id.tvUpload);
        editBtn = findViewById(R.id.editReceiverBtn);

        etName.setText(name);
        etDesc.setText(desc);
        etPhone.setText(phone);
        etEmail.setText(email);
        etLocation.setText(location);


//        imageView.setImageResource(img);
        Glide.with(this).load(img).into(imageView);
//        imageView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
//        final float scale = getResources().getDisplayMetrics().density;
////       int dpWidthInPx = (int) (950 * scale);
//        int dpHeightInPx = (int) (300 * scale);
//        imageView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
//        imageView.requestLayout();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 2);

//                askCameraPermissions();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                FilePathUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, FilePathUri);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FilePathUri != null) {
                    uploadToFirebase(FilePathUri);
//                    UploadImage();
                } else {
                    Toast.makeText(EditReceiver.this, "Please Select Image", Toast.LENGTH_SHORT).show();
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
                imageView.setImageBitmap(bitmap);
                imgIcon.setImageResource(0);
                FilePathUri = data.getData();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
//            imgIcon.setImageResource(0);

            ActivityCompat.requestPermissions(EditReceiver.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            FilePathUri = getImageUri(getApplicationContext(), imageBitmap);

//            Bitmap mImageUri1 = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(mImageUri1);


            Toast.makeText(this, "Image saved to:\n" +
                    data.getExtras().get("data"), Toast.LENGTH_LONG).show();


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(EditReceiver.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String GetFileExtension(Uri mUri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));

    }

    private void uploadToFirebase(Uri uri) {

        progressDialog.setTitle("Updating receiver...");
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

                        ReceiverData receiverData = new ReceiverData(id, receiverName, receiverDesc, receiverPhone,
                                receiverEmail, receiverLocation, 0, uri.toString());

                        databaseReference.setValue(receiverData);
//                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditReceiver.this, "Update Successfully", Toast.LENGTH_SHORT).show();
//                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);

                        progressDialog.hide();

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog.setTitle("Updating receiver...");
                progressDialog.show();
                //progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditReceiver.this, "Updating receiver failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}