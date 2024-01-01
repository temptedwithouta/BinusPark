package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    replaceFragment(new DashboardFragment());
                    return true;
                } else if (item.getItemId() == R.id.action_calender) {
                    // Navigasi ke ReservationActivity
                    startActivity(ReservationActivity.createIntent(DashboardActivity.this));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    // Tampilkan ProfileFragment atau navigasi ke halaman profil
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
