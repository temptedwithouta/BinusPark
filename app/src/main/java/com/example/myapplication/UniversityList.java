package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.adapter.UniversityListAdapter;
import com.example.myapplication.model.University;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

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
                        Intent profileIntent = new Intent(UniversityList.this, DashboardActivity.class);
                        profileIntent.putExtra("name", nameFromIntent);
                        profileIntent.putExtra("email", emailFromIntent);
                        profileIntent.putExtra("username", usernameFromIntent);
                        startActivity(profileIntent);
                    }
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.action_calender) {
                    // Navigasi ke ReservationActivity
                    finish();
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
                    finish();
                    return true;
                }
                return false;
            }
        });

        createData();

        universityRV = findViewById(R.id.universityRV);

        UniversityListAdapter adapter = new UniversityListAdapter(universityList, this);

        universityRV.setLayoutManager(new GridLayoutManager(this, 2));

        universityRV.setAdapter(adapter);
    }

    public void createData() {
        universityList = new ArrayList<>();

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

                    University university = new University(name, location, latitude, longitude, parkingSlot);

                    universityList.add(university);
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}