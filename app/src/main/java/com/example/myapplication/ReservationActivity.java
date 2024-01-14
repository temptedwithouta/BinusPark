package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.Reservation;
import com.example.myapplication.model.University;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {

    private static final String RESERVATION_NODE = "reserves";

    public String title;

    public String latitude;

    public String longitude;

    private String selectedUniversityName;

    private cookiesDb dbHandler;

    public static Intent createIntent(Context context) {
        return new Intent(context, ReservationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_reservation);

        dbHandler = new cookiesDb(this);

        replaceFragment(new ReservationFragment());

//        userHasReservation();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.action_calender);

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
//                    replaceFragment(new ReservationFragment());
                    Intent profileIntent = new Intent(ReservationActivity.this, UniversityList.class);

                    startActivity(profileIntent);
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

        ArrayList<User> userCookies = dbHandler.getUserCookies();

        User user = null;

        if(!userCookies.isEmpty()){
            user = userCookies.get(0);
        }

        String selectedUniversityName = getIntent().getExtras().getString("location");

        ArrayList<Reservation> reservations = userHasReservation();

        User finalUser = user;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!reservations.isEmpty()){
                    if(finalUser != null){
                        for(Reservation reservation : reservations){
                            if(selectedUniversityName.equals(reservation.getUniversityName()) && finalUser.getUsername().equals(reservation.getUsername())){
                                replaceFragment((new successReservation_fragment()));

                                break;
                            }
                        }
                    }
                }
            }
        }, 3000);

//        if(reservations.isEmpty()){
//            replaceFragment(new ReservationFragment());
//        }
//        else if(!reservations.isEmpty()){
//            if(user != null){
//                for(Reservation reservation : reservations){
//                    if(selectedUniversityName.equals(reservation.getUniversityName()) && user.getUsername().equals(reservation.getUsername())){
//                        replaceFragment((new successReservation_fragment()));
//
//                        break;
//                    }
//                }
//            }
//            else{
//                replaceFragment(new ReservationFragment());
//            }
//        }

//        ArrayList<Reservation> reservations = new ArrayList<>();
//
//        ArrayList<User> userCookies = dbHandler.getUserCookies();
//
//        String selectedUniversityName = getIntent().getExtras().getString("location");
//
//        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reserves");
//
//        reservationRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    String location = child.child("universityName").getValue(String.class);
//
//                    String username = child.child("username").getValue(String.class);
//
//                    Reservation reservation = new Reservation(location, username);
//
//                    reservations.add(reservation);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

//        title = getIntent().getExtras().getString("title");
//
//        latitude = getIntent().getExtras().getString("latitude");
//
//        longitude = getIntent().getExtras().getString("longitude");

//        if (userHasReservation()) {
//            showSuccessReservationFragment();
//        } else {
//            selectedUniversityName = getIntent().getStringExtra("selectedUniversityName");
//            showReservationFragment();
//        }

//        userHasReservation();

//        replaceFragment(new successReservation_fragment());
    }

    private ArrayList<Reservation> userHasReservation() {
        ArrayList<Reservation> reservations = new ArrayList<>();

        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reserves");

        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String location = child.child("universityName").getValue(String.class);

                    String username = child.child("username").getValue(String.class);

                    Reservation reservation = new Reservation(location, username);

                    reservations.add(reservation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        ArrayList<User> userCookies = dbHandler.getUserCookies();
//
//        String selectedUniversityName = getIntent().getExtras().getString("location");
//
//        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reserves");
//
//        reservationRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    String location = child.child("universityName").getValue(String.class);
//
//                    String username = child.child("username").getValue(String.class);
//
//                    if(!userCookies.isEmpty()){
//                        User user = userCookies.get(0);
//
//                        if(selectedUniversityName.equals(location) && user.getUsername().equals(username)){
//                            replaceFragment(new successReservation_fragment());
//
//                            break;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        return reservations;
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}