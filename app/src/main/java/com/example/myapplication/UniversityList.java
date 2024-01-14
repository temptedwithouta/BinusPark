package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.adapter.UniversityListAdapter;
import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.University;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UniversityList extends AppCompatActivity {

    private RecyclerView universityRV;

    private ArrayList<University> universityList;

    private cookiesDb dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        dbHandler = new cookiesDb(this);

        ArrayList<User> userCookies = dbHandler.getUserCookies();

        if(userCookies.size() > 1 || userCookies.isEmpty()){
            dbHandler.deleteAll();

            Intent intent = new Intent(UniversityList.this, LoginActivity.class);

            startActivity(intent);
        }

        createData();

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
                        Intent profileIntent = new Intent(UniversityList.this, DashboardActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_calender) {
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(UniversityList.this, UniversityList.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    // Navigasi ke ReservationActivity
                    Intent receivedIntent = getIntent();
                    if (receivedIntent != null) {
                        String nameFromIntent = receivedIntent.getStringExtra("name");
                        String emailFromIntent = receivedIntent.getStringExtra("email");
                        String usernameFromIntent = receivedIntent.getStringExtra("username");

                        // Pass the Intent data to ProfileActivity
                        Intent profileIntent = new Intent(UniversityList.this, ProfileActivity.class);
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

        universityRV = findViewById(R.id.universityRV);

        universityRV.setLayoutManager(new GridLayoutManager(this, 2));

        UniversityListAdapter adapter = new UniversityListAdapter(universityList, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                universityRV.setAdapter(adapter);
            }
        }, 6000);

//        UniversityListAdapter adapter = new UniversityListAdapter(universityList, this);
//
//        universityRV.setAdapter(adapter);
    }

    public void onUniversitySelected(String selectedUniversityName) {
        // Check if the user has a reservation for the selected university
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reserves");

        reservationRef.orderByChild("universityName").equalTo(selectedUniversityName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User has a reservation, start ReservationActivity with SuccessReservationFragment
                    Intent reservationIntent = new Intent(UniversityList.this, ReservationActivity.class);
                    reservationIntent.putExtra("selectedUniversityName", selectedUniversityName);
                    reservationIntent.putExtra("hasReservation", true);
                    startActivity(reservationIntent);
                } else {
                    // User does not have a reservation, start ReservationActivity with ReservationFragment
                    Intent reservationIntent = new Intent(UniversityList.this, ReservationActivity.class);
                    reservationIntent.putExtra("selectedUniversityName", selectedUniversityName);
                    reservationIntent.putExtra("hasReservation", false);
                    startActivity(reservationIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                showToast("Error checking reservation: " + databaseError.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void createData() {
        universityList = new ArrayList<>();
//
//        University university1 = new University("@AlamSutera", "alamsutera","-6.22366", "106.64924", "18");
//
//        University university2 = new University("@Anggrek", "anggrek", "-6.20071", "106.78251", "48");
//
//        University university3 = new University("@Bandung", "bekasi", "-6.91320", "107.59407", "28");
//
//        University university4 = new University("@Bekasi", "sudirman", "-6.21936", "106.99986", "4");
//
//        universityList.add(university1);
//
//        universityList.add(university2);
//
//        universityList.add(university3);
//
//        universityList.add(university4);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("universities");

        reference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.child("name").getValue(String.class);

                    String location = child.child("location").getValue(String.class);

                    String latitude = child.child("latitude").getValue(String.class);

                    String longitude = child.child("longitude").getValue(String.class);

                    String parkingSlot = child.child("parkingslot").getValue(String.class);

                    if(Integer.parseInt(parkingSlot) <= 0){
                        parkingSlot = "Full";
                    }

                    University university = new University(name, location, latitude, longitude, parkingSlot);

                    universityList.add(university);
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}