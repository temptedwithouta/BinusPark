package com.example.myapplication;

//Initial library imports
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ProfileActivity extends AppCompatActivity {
    //UI Declarations
    ImageButton backBtn;
    EditText nameHere;
    EditText emailHere;
    EditText phoneHere;
    EditText passwordHere;
    Button updateProfileBtn;
    TextView keluar;

    User currentUserModel;

    //UI initialization
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

        //Displaying data of the authenticated user
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            String thisEmail = receivedIntent.getStringExtra("email");
            String thisName = receivedIntent.getStringExtra("name");
            String thisPhone = receivedIntent.getStringExtra("phone");
            nameHere.setText(thisName);
            emailHere.setText(thisEmail);
            phoneHere.setText(thisPhone);
//            getUserData(emails);
        }

        //Bottom Navigation Bar contain passed intents of the authenticated user
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        Intent profileIntent = new Intent(ProfileActivity.this, DashboardActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    finish();
                    return true;

                } else if (item.getItemId() == R.id.action_calender) {
                    // Navigasi ke ReservationActivity
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(ProfileActivity.this, ReservationActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    finish();
                    return true;

                } else if (item.getItemId() == R.id.profile) {
                    // Tampilkan ProfileFragment atau navigasi ke halaman profil
                    finish();
                    return true;
                }
                return false;
            }
        });
        //Button to the previous openned page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Finds the whole data with "email" for profile update
        updateProfileBtn.setOnClickListener(v -> {
            if (receivedIntent != null && receivedIntent.hasExtra("email")) {
                String emails = receivedIntent.getStringExtra("email");
                editBtnClick(emails);
            }
        });
    }

    //Validates filled name
    void editBtnClick(String email) {
        String newFullName = nameHere.getText().toString();
        if (newFullName.isEmpty() || newFullName.length() < 2) {
            nameHere.setError("Full name must be at least 2 characters");
            return;
        } else {
            updateToFirebase(email, newFullName);
        }
    }

    //Shows Toast as feedback confirmation after user updated their data
    public void showToast(String message){
        Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
    }

    //Saves the updated data to Firebase
    void updateToFirebase(String email, String name) {
        com.example.myapplication.FirebaseUtil.currentUserDetails(email, new com.example.myapplication.FirebaseUtil.UserDetailListener() {
            @Override
            public void onUserDetail(DataSnapshot dataSnapshot) {
                // Update name in the database for the user with the provided email
                DatabaseReference userRef = dataSnapshot.getRef();
                userRef.child("name").setValue(name)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                updateSuccess(name);
                                showToast("Updated successfully");
                            } else {
                                showToast("Edit failed");
                            }
                        });
            }

            @Override
            public void onError(DatabaseError databaseError) {
            }

            @Override
            public void onUserNotFound() {
            }
        });
    }

    //Saves the updated data to the current intent
    private void updateSuccess(String newName) {
        nameHere.setText(newName);
        Intent updatedIntent = getIntent();
        if (updatedIntent != null) {
            updatedIntent.putExtra("name", newName);
        }
    }




}