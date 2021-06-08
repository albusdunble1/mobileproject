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
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditReceiver extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etLocation, etDesc;
    TextView textViewUpload, textViewCamera, textViewLocation;
    ImageView imageView, imgUploadIcon, imgCameraIcon, imgLocation;
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

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receiver);

        //Initialize and assign bottom navigation AdminData
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationAdmin);

        //Set Profile selected bottom Navigation AdminData
        bottomNavigationView.setSelectedItemId(R.id.navreceiverlist);

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        textViewLocation = findViewById(R.id.tvLocation);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        imageView = findViewById(R.id.imgIcon);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        imgUploadIcon = findViewById(R.id.imgUpload);
        imgCameraIcon = findViewById(R.id.imgCamera);
        textViewUpload = findViewById(R.id.tvUpload);
        textViewCamera = findViewById(R.id.tvCamera);
        editBtn = findViewById(R.id.editReceiverBtn);
        imgLocation = findViewById(R.id.ivLocation);

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


        textViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

        imgUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);

            }
        });

        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });

        imgCameraIcon.setOnClickListener(new View.OnClickListener() {
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

        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditReceiver.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(EditReceiver.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditReceiver.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(EditReceiver.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

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
                    Toast.makeText(EditReceiver.this, "Please Select New Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(EditReceiver.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        etLocation.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                //imgUploadIcon.setImageResource(0);
                FilePathUri = data.getData();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
//            imgUploadIcon.setImageResource(0);

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

                        Intent receiverDetails = new Intent(EditReceiver.this, DetailsReceiver.class);

                        receiverDetails.putExtra("id", receiverDataId);
                        receiverDetails.putExtra("desc", receiverDesc);
                        receiverDetails.putExtra("img", uri.toString());
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