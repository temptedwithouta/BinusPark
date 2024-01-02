package com.example.myapplication;// IntentHelper.java

//Initial library imports
import android.content.Context;
import android.content.Intent;

//Helps intent passing for starting dashboard activity
public class IntentHelper {

    public static void startDashboardActivity(Context context, String name, String email, String username) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        context.startActivity(intent);
    }
}