package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.unilink.Models.PostModel;
import com.example.unilink.Models.UserModel;
import com.example.unilink.databinding.ActivityWritePostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class WritePostActivity extends AppCompatActivity {
    ActivityWritePostBinding binding;
    private int perReqCode = 1;
    private int REQUESTCODE = 1;
    private Uri pickedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWritePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check whether all fields are filled
                if (!binding.postTitle.getText().toString().isEmpty() && !binding.postDescription.getText().toString().isEmpty() && pickedImageUri != null) {
                    // store the contents in the firebase

                    // make a database path "Board_images" and specify image file path for each image
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Board_images");
                    StorageReference imageFilePath = storageReference.child(pickedImageUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageFilePath.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageDownloadLink = uri.toString();

                                                    // create postModel object
                                                    // TODO: user name to be stored
                                                    PostModel postModel = new PostModel(binding.postTitle.getText().toString(),
                                                                                        binding.postDescription.getText().toString(),
                                                                                        imageDownloadLink,
                                                                                        null);

                                                    // add postModel to firebase database
                                                    addPost(postModel);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // having troubles in uploading images. Send an error message
                                                    Toast.makeText(WritePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });


                } else {
                    Toast.makeText(WritePostActivity.this, "Do not leave empty fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void addPost(PostModel postModel) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts").push();

        // get post unique key
        String key = databaseReference.getKey();
        postModel.setPostKey(key);

        // add post data to firebase database
        databaseReference.setValue(postModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(WritePostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WritePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // TODO: Permission-asking message does not seem to be popped up
    private void accessToUserGallery() {
        if (ContextCompat.checkSelfPermission(WritePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(WritePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(WritePostActivity.this, "Please grant access to your gallery", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(WritePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, perReqCode);
            }

        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE) {
            if (data != null) {
                pickedImageUri = data.getData();
                binding.postImg.setImageURI(pickedImageUri);
                // Glide.with(WritePostActivity.this).load(pickedImageUri).into(binding.postImg);
            } else {
                Toast.makeText(this, "Pick an image bruh", Toast.LENGTH_SHORT).show();
            }
        }
    }

}


