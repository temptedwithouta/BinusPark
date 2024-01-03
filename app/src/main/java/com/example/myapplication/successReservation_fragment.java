package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class successReservation_fragment extends Fragment {
    private SuccessReservationAdapter adapter;
    private DatabaseReference dbReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.reservation_success,containter, false);

        dbReference = FirebaseDatabase.getInstance().getReference("reserves");

        RecyclerView recyclerView = rootView.findViewById(R.id.reservationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SuccessReservationAdapter();
        recyclerView.setAdapter(adapter);

        getResevationData();

        //Buat pindah funsgi pindah map
        Button openMapButton = rootView.findViewById(R.id.openMap);
        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Manggil function open map, diisi yang bang dibawah intennnya
                openMap();
            }
        });

        return  rootView;
    }

    private void getResevationData(){
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapShot) {
                List<Reservation>reservationModelList = new ArrayList<>();

                for(DataSnapshot snapShot : dataSnapShot.getChildren()){
                    Reservation reservation = snapShot.getValue(Reservation.class);
                    reservationModelList.add(reservation);
                }
                adapter.setReservationList(reservationModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openMap(){
        ReservationActivity reservation = (ReservationActivity)getActivity();

        String title = reservation.title;

        String latitude = reservation.latitude;

        String longitude = reservation.longitude;

        Intent intent = new Intent(getActivity(), MapActivity.class);

        intent.putExtra("title", title);

        intent.putExtra("latitude", latitude);

        intent.putExtra("longitude", longitude);
    }
}
