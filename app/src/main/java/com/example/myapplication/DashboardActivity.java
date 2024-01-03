package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.text.Layout;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private cookiesDb dbHandler;

    private ArrayList<User> userCookies;

    private TextView textViewBinusPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_1);
        // Retrieve the data from the Intent received in DashboardActivity

        dbHandler = new cookiesDb(this);

        userCookies = dbHandler.getUserCookies();

        textViewBinusPark = findViewById(R.id.textViewBinusPark);

        if(!userCookies.isEmpty()){
            User user = userCookies.get(0);

            String helloText = "Hello " + user.getName();

            textViewBinusPark.setText(helloText);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    replaceFragment(new DashboardFragment());
                    return true;
                } else if (item.getItemId() == R.id.action_calender) {
                    // Navigasi ke ReservationActivity
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(DashboardActivity.this, UniversityList.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    // Tampilkan ProfileFragment atau navigasi ke halaman profil
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(DashboardActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            // Membuat instance dari DashboardFragment
            DashboardFragment dashboardFragment = new DashboardFragment();

            // Memulai transaksi fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Menambahkan fragment ke container di layout activity_dashboard
            transaction.replace(R.id.fragmentContainer, dashboardFragment);

            // Menyelesaikan transaksi
            transaction.commit();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}