package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.unilink.Models.UserModel;
import com.example.unilink.R;
import com.example.unilink.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        binding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.userName.getText().toString();
                String email = binding.userEmail.getText().toString();
                String password1 = binding.userPassword1.getText().toString();
                String password2 = binding.userPassword2.getText().toString();

                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Creating Account");
                progressDialog.setMessage("Tip! \n Having sex 7x a day should be our goal");
                progressDialog.show();

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password1.trim())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .updateProfile(userProfileChangeRequest);
                                reset();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

}

    private void reset() {
        progressDialog.cancel();
        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
    }
}