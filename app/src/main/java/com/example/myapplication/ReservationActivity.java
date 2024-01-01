package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class ReservationActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ReservationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_reservation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    // Navigasi kembali ke DashboardActivity
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(ReservationActivity.this, DashboardActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.action_calender) {
                    // Tampilkan ReservationFragment
                    replaceFragment(new ReservationFragment());
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    // Tampilkan ProfileFragment atau navigasi ke halaman profil
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(ReservationActivity.this, ProfileActivity.class);
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

        if (userHasReservation()) {
            showSuccessReservationFragment();
        } else {
            showReservationFragment();
        }
    }

    private boolean userHasReservation() {
        // Implementasi untuk memeriksa apa user sudah melakukan pemesanan sebelumnya

        return false;
    }

    private void showReservationFragment() {
        replaceFragment(new ReservationFragment());
    }

    private void showSuccessReservationFragment() {
        replaceFragment(new succcessReservation_fragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
