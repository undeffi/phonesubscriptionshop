package com.example.ppshop;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProfileActivity.class.getName();
    Button deleteProfile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneNumTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        deleteProfile = findViewById(R.id.userDelete);
        mAuth = FirebaseAuth.getInstance();

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneNumTextView = findViewById(R.id.phoneNumTextView);

        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String phoneNum = document.getString("phoneNum");

                        nameTextView.setText(name);
                        emailTextView.setText(email);
                        phoneNumTextView.setText(phoneNum);
                    }
                }
            }
        });
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();

                    // Delete the user account from Firebase Authentication
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(LOG_TAG, "User account deleted.");

                                // Delete the document from Firestore
                                Log.d(LOG_TAG, FirebaseFirestore.getInstance().collection("users").document(uid).toString());
                                FirebaseFirestore.getInstance().collection("users").document(uid)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(LOG_TAG, "DocumentSnapshot successfully deleted!");
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(ProfileActivity.this, "Felhasználó törölve!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(LOG_TAG, "Error deleting document", e);
                                                Toast.makeText(ProfileActivity.this, "Nem sikerült a felhasználó törlése!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Log.w(LOG_TAG, "Error deleting user account.", task.getException());
                                Toast.makeText(ProfileActivity.this, "Nem sikerült a felhasználó törlése!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}