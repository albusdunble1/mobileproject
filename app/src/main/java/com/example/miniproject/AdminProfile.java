package com.example.miniproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
TextView txtUsername1,txtEmail1,txtPass1,txtphone1,txtic1;
Button btnEdit;
ImageView imgView;
//Button save,upload,choose,view;
StorageReference mStorageReff;
public Uri imguri;
private StorageTask uploadTask;
DatabaseReference reff;
Admin member;
Bitmap bitmap;
long maxID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(AdminProfile.this, "Firebase connected!", Toast.LENGTH_SHORT).show();
        txtUsername1 = findViewById(R.id.txtUsername1);
        txtEmail1 = findViewById(R.id.txtEmail1);
        txtPass1 = findViewById(R.id.txtPass1);
        txtphone1 = findViewById(R.id.txtphone1);
        txtic1 = findViewById(R.id.txtic1);
        //txtimg1 = findViewById(R.id.txtimg1);
        btnEdit = findViewById(R.id.btnSave);
        imgView = findViewById(R.id.imgView);
        mStorageReff = FirebaseStorage.getInstance().getReference("Images");
        member = new Admin();
        reff = FirebaseDatabase.getInstance().getReference().child("Admin").child("1");

        reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    maxID=(snapshot.getChildrenCount());
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
                        Glide.with(AdminProfile.this)
                                .load(img)
                                .into(imgView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("The read failed: " + error.getCode());

                    }

                });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editScreenIntent = new Intent(com.example.miniproject.AdminProfile.this, AdminEditProfile.class);

                editScreenIntent.putExtra("name",txtUsername1.getText().toString());
                editScreenIntent.putExtra("email",txtEmail1.getText().toString());
                editScreenIntent.putExtra("password",txtPass1.getText().toString());
                editScreenIntent.putExtra("ic",txtic1.getText().toString());
                editScreenIntent.putExtra("phone",txtphone1.getText().toString());
//                imgView.buildDrawingCache();
//                Bitmap bitmap = imgView.getDrawingCache();
//                editScreenIntent.putExtra("image", bitmap);

                startActivity(editScreenIntent);
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

//        choose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FileChooser();
//            }
//        });
//
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(uploadTask != null && uploadTask.isInProgress()){
//                    Toast.makeText(AdminProfile.this, "Upload is in progress!", Toast.LENGTH_SHORT).show();
//                }else{
//                    Fileuploader();
//                }
//
//            }
//        });
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
                Toast.makeText(com.example.miniproject.AdminProfile.this, "Image uploaded succesfully!", Toast.LENGTH_SHORT).show();
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
            imguri = data.getData();
            imgView.setImageURI(imguri);
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