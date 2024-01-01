package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ProfileActivity extends AppCompatActivity {
    ImageButton backBtn;
    EditText nameHere;
    EditText emailHere;
    EditText phoneHere;
    EditText passwordHere;
    Button updateProfileBtn;
    TextView keluar;

    UserModel currentUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameHere = findViewById(R.id.full_name);
        emailHere = findViewById(R.id.email);
        phoneHere = findViewById(R.id.phone_number);
        passwordHere = findViewById(R.id.password);
        backBtn = findViewById(R.id.back);
        updateProfileBtn = findViewById(R.id.profile_update);


        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra("email")) {
            String emails = receivedIntent.getStringExtra("email");
            getUserData(emails);
        }

        updateProfileBtn.setOnClickListener(v -> {
            if (receivedIntent != null && receivedIntent.hasExtra("email")) {
                String emails = receivedIntent.getStringExtra("email");
                editBtnClick(emails);
            }
        });
    }

    void editBtnClick(String email) {
        String newFullName = nameHere.getText().toString();
        if (newFullName.isEmpty() || newFullName.length() < 2) {
            nameHere.setError("Full name must be at least 2 characters");
            return;
        } else {
            updateToFirebase(email, newFullName);
        }
    }

    public void showToast(String message){
        Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
    }
    void updateToFirebase(String email, String name) {
        com.example.myapplication.FirebaseUtil.currentUserDetails(email, new com.example.myapplication.FirebaseUtil.UserDetailListener() {
            @Override
            public void onUserDetail(DataSnapshot dataSnapshot) {
                // Update name in the database for the user with the provided email
                DatabaseReference userRef = dataSnapshot.getRef();
                userRef.child("name").setValue(name)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                               showToast("Updated successfully");
                            } else {
                                showToast("Edit failed");
                            }
                        });
            }

            @Override
            public void onError(DatabaseError databaseError) {
                // Handle error while retrieving user details
            }

            @Override
            public void onUserNotFound() {
                // Handle case when user data is not found
            }
        });
    }

    void getUserData(String email) {
        com.example.myapplication.FirebaseUtil.currentUserDetails(email, new com.example.myapplication.FirebaseUtil.UserDetailListener() {
            @Override
            public void onUserDetail(DataSnapshot dataSnapshot) {
                // Handle user details fetched from Realtime Database
                String name = dataSnapshot.child("name").getValue(String.class);
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);

                if (name != null && email != null && phone != null) {
                    nameHere.setText(name);
                    emailHere.setText(userEmail);
                    phoneHere.setText(phone);
                }
            }

            @Override
            public void onError(DatabaseError databaseError) {
                // Handle error while retrieving user details from Realtime Database
            }

            @Override
            public void onUserNotFound() {
                // Handle case when user data is not found in Realtime Database
            }
        });
    }



}