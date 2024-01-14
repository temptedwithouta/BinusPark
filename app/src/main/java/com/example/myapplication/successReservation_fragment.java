package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.SuccessReservationAdapter;
import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.Reservation;
import com.example.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class successReservation_fragment extends Fragment {

//    private RecyclerView recyclerView;
//
//    private SuccessReservationAdapter adapter;
//
//    private DatabaseReference dbReference;
//
//    private ArrayList<Reservation> reservationModelList;
//
//    private cookiesDb dbHandler;
//
////    Button openMapButton;
//
//    private String title;
//
//    private String latitude;
//
//    private String longitude;

    public successReservation_fragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.reservation_success,containter, false);

//        getResevationData();
//
//        dbReference = FirebaseDatabase.getInstance().getReference("reserves");
//
//        dbHandler = new cookiesDb(getContext());
//
//        title = getActivity().getIntent().getExtras().getString("title");
//
//        latitude = getActivity().getIntent().getExtras().getString("latitude");
//
//        longitude = getActivity().getIntent().getExtras().getString("longitude");

//        if(rootView instanceof RecyclerView){
//            recyclerView = (RecyclerView) rootView;
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//            adapter = new SuccessReservationAdapter(getContext(), reservationModelList, title, longitude, latitude);
//
//            recyclerView.setAdapter(adapter);
//        }

//        recyclerView = rootView.findViewById(R.id.reservationRecyclerView);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        getResevationData();
//
//        adapter = new SuccessReservationAdapter(getActivity(), reservationModelList, title, longitude, latitude);
//
//        recyclerView.setAdapter(adapter);

//        getResevationData();

        //Buat pindah funsgi pindah map
//        openMapButton = rootView.findViewById(R.id.openMap);
//        openMapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Manggil function open map, diisi yang bang dibawah intennnya
//
////                Intent intent = new Intent(getContext(), DashboardActivity.class);
//            }
//        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView;

//        SuccessReservationAdapter adapter;

        ArrayList<Reservation> reservationModelList = getResevationData();

        String title;

        String latitude;

        String longitude;

        title = getActivity().getIntent().getExtras().getString("title");

        latitude = getActivity().getIntent().getExtras().getString("latitude");

        longitude = getActivity().getIntent().getExtras().getString("longitude");

        recyclerView = view.findViewById(R.id.reservationRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


//        getResevationData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SuccessReservationAdapter adapter = new SuccessReservationAdapter(getContext(), reservationModelList, title, longitude, latitude);

                recyclerView.setAdapter(adapter);
            }
        }, 6000);

//        title = getActivity().getIntent().getExtras().getString("title");
//
//        latitude = getActivity().getIntent().getExtras().getString("latitude");
//
//        longitude = getActivity().getIntent().getExtras().getString("longitude");
//
//        recyclerView = view.findViewById(R.id.reservationRecyclerView);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        adapter = new SuccessReservationAdapter(getContext(), reservationModelList, title, longitude, latitude);
//
//        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Reservation> getResevationData(){
        ArrayList<Reservation> reservationModelList = new ArrayList<>();

        cookiesDb dbHandler;

        dbHandler = new cookiesDb(getContext());

        ArrayList<User> userCookies = dbHandler.getUserCookies();

        String selectedUniversityName = getActivity().getIntent().getExtras().getString("location");

        DatabaseReference dbReference;

        dbReference = FirebaseDatabase.getInstance().getReference("reserves");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot child : dataSnapShot.getChildren()){
                    String location = child.child("universityName").getValue(String.class);

                    String username = child.child("username").getValue(String.class);

                    if(!userCookies.isEmpty()){
                        User user = userCookies.get(0);

                        if(selectedUniversityName.equals(location) && user.getUsername().equals(username)){
                            int startDate = child.child("startTime").child("dayOfMonth").getValue(Integer.class);

                            int startMonth = child.child("startTime").child("monthValue").getValue(Integer.class);

                            int startYear = child.child("startTime").child("year").getValue(Integer.class);

                            int startHour = child.child("startTime").child("hour").getValue(Integer.class);

                            int startMinute = child.child("startTime").child("minute").getValue(Integer.class);

                            int endDate = child.child("endTime").child("dayOfMonth").getValue(Integer.class);

                            int endMonth = child.child("endTime").child("monthValue").getValue(Integer.class);

                            int endYear = child.child("endTime").child("year").getValue(Integer.class);

                            int endHour = child.child("endTime").child("hour").getValue(Integer.class);

                            int endMinute = child.child("endTime").child("minute").getValue(Integer.class);

                            String startTimeString = startDate + "/" + startMonth + "/" + startYear + " " + startHour + ":" + startMinute;

                            String endTimeString = endDate + "/" + endMonth + "/" + endYear + " " + endHour + ":" + endMinute;


                            if(Integer.toString(startHour).length() == 1 && Integer.toString(startMinute).length() == 1){
                                startTimeString = startDate + "/" + startMonth + "/" + startYear + " 0" + startHour + ":0" + startMinute;
                            }
                            else if(Integer.toString(startHour).length() == 1){
                                startTimeString = startDate + "/" + startMonth + "/" + startYear + " 0" + startHour + ":" + startMinute;
                            }
                            else if(Integer.toString(startMinute).length() == 1){
                                startTimeString = startDate + "/" + startMonth + "/" + startYear + " " + startHour + ":0" + startMinute;
                            }

                            if(Integer.toString(endHour).length() == 1 && Integer.toString(endMinute).length() == 1){
                                endTimeString = endDate + "/" + endMonth + "/" + endYear + " 0" + endHour + ":0" + endMinute;
                            }
                            else if(Integer.toString(endHour).length() == 1){
                                endTimeString = endDate + "/" + endMonth + "/" + endYear + " 0" + endHour + ":" + endMinute;
                            }
                            else if(Integer.toString(endMinute).length() == 1){
                                endTimeString = endDate + "/" + endMonth + "/" + endYear + " " + endHour + ":0" + endMinute;
                            }

//                            LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
//
//                            LocalTime endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("HH:mm"));

//                            long startTime = child.child("startTime").getValue(Long.class);
//
//                            long endTime = child.child("endTime").getValue(Long.class);

                            double totalHarga = child.child("totalHarga").getValue(Double.class);

//                            LocalDateTime startTimeLDT = LocalDateTime.ofEpochSecond(startTime, 0, ZoneOffset.UTC);
//
//                            LocalDateTime endTimeLDT = LocalDateTime.ofEpochSecond(endTime, 0, ZoneOffset.UTC);

                            Reservation reservation = new Reservation(startTimeString, endTimeString, 15000, totalHarga, location, username);

//                            reservation.setTotalHarga(totalHarga);
//
//                            reservation.setUniversityName(location);
//
//                            reservation.setUsername(username);

                            reservationModelList.add(reservation);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        String startTimeString = "14:00";
//
//        String endTimeString = "16:00";
//
//        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
//
//        LocalTime endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("HH:mm"));
//
//        Reservation reservation = new Reservation(startTime.atDate(LocalDate.now()), endTime.atDate(LocalDate.now()), 15000);
//
//        reservation.setTotalHarga(200000);
//
//        reservation.setUniversityName("alamsutera");
//
//        reservation.setUsername("Daniel");
//
//        reservationModelList.add(reservation);
//
//        reservationModelList.add(reservation);
//
//        reservationModelList.add(reservation);
//
//        reservationModelList.add(reservation);

        return reservationModelList;
    }

//    private void openMap(){
//        String title = getActivity().getIntent().getExtras().getString("title");
//
//        String latitude = getActivity().getIntent().getExtras().getString("latitude");
//
//        String longitude = getActivity().getIntent().getExtras().getString("longitude");
//
////        String title = reservation.title;
////
////        String latitude = reservation.latitude;
////
////        String longitude = reservation.longitude;
//
//        Intent intent = new Intent(getContext(), MapActivity.class);
//
//        intent.putExtra("title", title);
//
//        intent.putExtra("latitude", latitude);
//
//        intent.putExtra("longitude", longitude);
//
//        startActivity(intent);
//    }
}
