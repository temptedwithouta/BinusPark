package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    Button butt1;

    cookiesDb dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dbHandler = new cookiesDb(SplashScreen.this);

        ArrayList<User> userCookies = dbHandler.getUserCookies();

        if(userCookies.size() >= 1){
            dbHandler.deleteAll();
        }

        if(!userCookies.isEmpty()){
            Intent intent = new Intent(SplashScreen.this, DashboardActivity.class);

            startActivity(intent);
        }

        butt1 = findViewById(R.id.button1);

        FirebaseMessaging.getInstance().subscribeToTopic("Park")
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Done";
                if(!task.isSuccessful())
                {
                    msg = "Failed";
                }
            }
        });

        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}