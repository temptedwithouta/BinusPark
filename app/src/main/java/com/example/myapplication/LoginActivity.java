package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class  LoginActivity extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView registerRedirectText;
    Switch swtBtn;
    cookiesDb dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);


        //Switch light to dark mode settings
        swtBtn = findViewById(R.id.switchTheme);
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

        boolean isDarkmodeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        swtBtn.setChecked(isDarkmodeOn);
        if(isDarkmodeOn){
            swtBtn.setText("Dark Mode");
        }else{
            swtBtn.setText("Light Mode");
        }


        dbHandler = new cookiesDb(this);

        ArrayList<User> userCookies = dbHandler.getUserCookies();

        if(userCookies.size() >= 1){
            dbHandler.deleteAll();
        }

        if(!userCookies.isEmpty()){
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

            startActivity(intent);
        }

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        registerRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUsername() | !validatePassword()){
                } else {
                    validateUser();
                }
            }
        });
        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    public Boolean validateUsername(){
        String str = loginUsername.getText().toString();
        if (str.isEmpty()){
            loginUsername.setError("Username must be filled");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String str = loginPassword.getText().toString();
        if (str.isEmpty()){
            loginPassword.setError("Password must be filled");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void validateUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query validateUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        validateUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){
                        loginUsername.setError(null);
                        //ini buat passing data ke profile buat sena kalo mau pake ya
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        Integer phoneFromDB = snapshot.child(userUsername).child("phone").getValue(Integer.class);

                        dbHandler.createUserCookies(usernameFromDB, nameFromDB, emailFromDB, passwordFromDB, phoneFromDB);

                        IntentHelper.startDashboardActivity(LoginActivity.this, nameFromDB, emailFromDB, usernameFromDB);

                    } else {
                        loginPassword.setError("Password Incorrect");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("Username has not been registered");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}