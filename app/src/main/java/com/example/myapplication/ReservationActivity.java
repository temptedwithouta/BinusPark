package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.MenuItem;
import android.widget.Toast;

public class ReservationActivity extends AppCompatActivity {

    private static final String RESERVATION_NODE = "reserves";

    public String title;

    public String latitude;

    public String longitude;

    private String selectedUniversityName;

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

        title = getIntent().getExtras().getString("title");

        latitude = getIntent().getExtras().getString("latitude");

        longitude = getIntent().getExtras().getString("longitude");

        if (userHasReservation()) {
            showSuccessReservationFragment();
        } else {
            selectedUniversityName = getIntent().getStringExtra("selectedUniversityName");
            showReservationFragment();
        }
    }

    private boolean userHasReservation() {
        String selectedUniversityName = getIntent().getStringExtra("selectedUniversityName");

        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reserves");

        reservationRef.orderByChild("universityName").equalTo(selectedUniversityName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User has a reservation, show success fragment
                    showSuccessReservationFragment();
                } else {
                    // User does not have a reservation, show reservation fragment
                    showReservationFragment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                showToast("Error checking reservation: " + databaseError.getMessage());
            }
        });

        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showReservationFragment() {
        // Pass the selected university name to ReservationFragment
        ReservationFragment reservationFragment = new ReservationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedUniversityName", selectedUniversityName);
        reservationFragment.setArguments(bundle);

        replaceFragment(reservationFragment);
    }
    private void showSuccessReservationFragment() {
        replaceFragment(new successReservation_fragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}