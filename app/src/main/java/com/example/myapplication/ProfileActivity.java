package com.example.myapplication;

//Initial library imports
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    //UI Declarations
    ImageButton backBtn;
    EditText nameHere;
    EditText emailHere;
    EditText phoneHere;
    EditText passwordHere;
    Button updateProfileBtn;

    Button logoutProfile;
    TextView keluar;

    cookiesDb dbHandler;

    ArrayList<User> userCookies;

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
        logoutProfile = findViewById(R.id.profile_logout);
        Switch swtBtn = findViewById(R.id.switch1);

        dbHandler = new cookiesDb(this);

        userCookies = dbHandler.getUserCookies();

        if(userCookies.size() > 1){
            dbHandler.deleteAll();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);

            startActivity(intent);
        }

        if(!userCookies.isEmpty()){
            User user = userCookies.get(0);

            nameHere.setText(user.getName());
            emailHere.setText(user.getEmail());
            phoneHere.setText(Integer.toString(user.getPhone()));
            passwordHere.setText(user.getPassword());
        }
        else{
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);

            startActivity(intent);
        }

        //Displaying data of the authenticated user
        Intent receivedIntent = getIntent();
//        if (receivedIntent != null) {
//            String thisEmail = receivedIntent.getStringExtra("email");
//            String thisName = receivedIntent.getStringExtra("name");
//            String thisPhone = receivedIntent.getStringExtra("phone");
//            nameHere.setText(thisName);
//            emailHere.setText(thisEmail);
//            phoneHere.setText(thisPhone);
//            getUserData(emails);
//        }

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
                        Intent profileIntent = new Intent(ProfileActivity.this, UniversityList.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    finish();
                    return true;

                } else if (item.getItemId() == R.id.profile) {
                    // Tampilkan ProfileFragment atau navigasi ke halaman profil
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
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
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);

                startActivity(intent);
            }
        });

        //Finds the whole data with "email" for profile update
        updateProfileBtn.setOnClickListener(v -> {
//            if (receivedIntent != null && receivedIntent.hasExtra("email")) {
//                String emails = receivedIntent.getStringExtra("email");
//                editBtnClick(emails);
//            }

            if(nameHere.length() <= 0 || emailHere.length() <= 0 || passwordHere.length() <= 0 || phoneHere.length() <= 0){
                showToast("Data Must Be Field!");
            }
            else{
                editBtnClick();
            }
        });

        logoutProfile.setOnClickListener(v -> {
            dbHandler.deleteAll();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);

            startActivity(intent);
        });

        //Switch light to dark mode settings
        swtBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    buttonView.setText("Dark Mode");
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    buttonView.setText("Light Mode");
                }
            }
        });
    }

    //Validates filled name
//    void editBtnClick(String email) {
//        String newFullName = nameHere.getText().toString();
//        if (newFullName.isEmpty() || newFullName.length() < 2) {
//            nameHere.setError("Full name must be at least 2 characters");
//        } else {
//            updateToFirebase(email, newFullName);
//        }
//    }

    private void editBtnClick(){
        User oldUser = userCookies.get(0);

        String newName = nameHere.getText().toString();

        String newEmail = emailHere.getText().toString();

        String newPassword = passwordHere.getText().toString();

        int newPhone = Integer.parseInt(phoneHere.getText().toString());

        dbHandler.updateUserCookies(oldUser.getUsername(), newName, newEmail, newPassword, newPhone);

        updateToFirebase(oldUser.getUsername(), newName, newEmail, newPassword, newPhone);
    }

    //Shows Toast as feedback confirmation after user updated their data
    public void showToast(String message){
        Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
    }

    //Saves the updated data to Firebase
//    void updateToFirebase(String email, String name) {
//        com.example.myapplication.FirebaseUtil.currentUserDetails(email, new com.example.myapplication.FirebaseUtil.UserDetailListener() {
//            @Override
//            public void onUserDetail(DataSnapshot dataSnapshot) {
//                // Update name in the database for the user with the provided email
//                DatabaseReference userRef = dataSnapshot.getRef();
//                userRef.child("name").setValue(name)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                updateSuccess(name);
//                                showToast("Updated successfully");
//                            } else {
//                                showToast("Edit failed");
//                            }
//                        });
//            }
//
//            @Override
//            public void onError(DatabaseError databaseError) {
//            }
//
//            @Override
//            public void onUserNotFound() {
//            }
//        });
//    }

    private void updateToFirebase(String username, String name, String email, String password, int phone){
        DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference();

        firebaseDb.child("users").child(username).child("name").setValue(name);

        firebaseDb.child("users").child(username).child("email").setValue(email);

        firebaseDb.child("users").child(username).child("password").setValue(password);

        firebaseDb.child("users").child(username).child("phone").setValue(phone);

        showToast("Updated Successfully");
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