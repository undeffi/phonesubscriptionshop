package com.example.ppshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private FirebaseAuth mAuth;
    private CollectionReference mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText uFullName, uEmail, uPhoneNum, uPassword, uPasswordConfirm;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        uFullName = findViewById(R.id.userFullName);
        uEmail = findViewById(R.id.userEmailAddress);
        uPhoneNum = findViewById(R.id.userPhoneNumber);
        uPassword = findViewById(R.id.userPassword);
        uPasswordConfirm = findViewById(R.id.userPasswordConfirm);

        btnRegister = findViewById(R.id.userRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = uFullName.getText().toString().trim();
                String email = uEmail.getText().toString().trim();
                String phoneNum = uPhoneNum.getText().toString().trim();
                String password = uPassword.getText().toString().trim();
                String passwordConfirm = uPasswordConfirm.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();

                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("phoneNum", phoneNum);

                            DocumentReference userRef = db.collection("users").document(userId);
                            userRef.set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(RegisterActivity.this, ShopActivity.class);
                                            startActivity(intent);
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + userRef.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}