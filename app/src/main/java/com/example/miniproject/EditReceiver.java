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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditReceiver extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etLocation, etDesc;
    TextView textView;
    ImageView imageView, imgIcon;
    Button editBtn;

    ProgressDialog progressDialog;
    ProgressBar progressBar;
    Uri FilePathUri;
    int Image_Request_Code = 7;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String id;

    String currentPhotoPath;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receiver);


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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);

//                askCameraPermissions();
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




//    private void askCameraPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
//        } else {
//            dispatchTakePictureIntent();
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERM_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                dispatchTakePictureIntent();
//            } else {
//                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "net.smallacademy.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//            }
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
////        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    private void uploadImageToFirebase(String name, Uri contentUri) {
//        progressDialog.setTitle("Updating receiver...");
//        progressDialog.show();
//
//        final StorageReference image = storageReference.child("pictures/" + name);
//        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
////                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
//                        String receiverDataId = databaseReference.push().getKey();
//                        String receiverName = etName.getText().toString().trim();
//                        String receiverDesc = etDesc.getText().toString().trim();
//                        String receiverPhone = etPhone.getText().toString().trim();
//                        String receiverEmail = etEmail.getText().toString().trim();
//                        String receiverLocation = etLocation.getText().toString().trim();
//
//                        ReceiverData receiverData = new ReceiverData(id, receiverName, receiverDesc, receiverPhone,
//                                receiverEmail, receiverLocation, 0, uri.toString());
//
//                        databaseReference.setValue(receiverData);
////                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(EditReceiver.this, "Update Successfully", Toast.LENGTH_SHORT).show();
////                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
//
//                        progressDialog.hide();
//                    }
//                });
//
//                Toast.makeText(EditReceiver.this, "Update Successfully", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(EditReceiver.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }





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


//        if (requestCode == CAMERA_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                File f = new File(currentPhotoPath);
//                imageView.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
//
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
//
//                uploadImageToFirebase(f.getName(), contentUri);
//
//
//            }
//        }


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