package com.example.myapplication;// IntentHelper.java

import android.content.Context;
import android.content.Intent;

public class IntentHelper {

    public static void startDashboardActivity(Context context, String name, String email, String username) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        context.startActivity(intent);
    }

    public static void startProfileActivity(Context context, String name, String email, String username) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        context.startActivity(intent);
    }

    // You can create similar methods for other classes if needed
}
