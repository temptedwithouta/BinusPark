package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class    RegisterActivity extends AppCompatActivity {

    EditText registerName, registerUsername, registerEmail, registerPassword, registerPhonenumber;
    TextView loginRedirectText;
    Button registerButton;
    FirebaseDatabase database;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerName = findViewById(R.id.register_name);
        registerEmail = findViewById(R.id.register_email);
        registerUsername = findViewById(R.id.register_username);
        registerPassword = findViewById(R.id.register_password);
        registerPhonenumber = findViewById(R.id.register_phonenumber);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                dbReference = database.getReference("users");
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String username = registerUsername.getText().toString();
                String password = registerPassword.getText().toString();
                String phone = registerPhonenumber.getText().toString();
                User user = new User(name, email, username, password, Integer.parseInt(phone));
                dbReference.child(username).setValue(user);
                Toast.makeText(RegisterActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}