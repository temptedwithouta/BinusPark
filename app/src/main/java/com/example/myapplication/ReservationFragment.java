package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.model.Reservation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReservationFragment extends Fragment {

    private EditText editTextStart, editTextEnd;
    private Button bookButton;

    private DatabaseReference dbReference;
    public ReservationFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.actvity_reservation, container, false);

        editTextStart = rootView.findViewById(R.id.editTextStartTime);
        editTextEnd = rootView.findViewById(R.id.editTextEndTime);
        bookButton = rootView.findViewById(R.id.BookButton);

        dbReference = FirebaseDatabase.getInstance().getReference("reserves");

        bookButton.setOnClickListener(e -> bookReservation()); //Buat ngelakuin Book

        return rootView;
    }

    private void bookReservation(){
        String starTime = editTextStart.getText().toString().trim();
        String endTime = editTextEnd.getText().toString().trim();

        if(starTime.isEmpty() || endTime.isEmpty()){
            Toast.makeText(requireContext(), "Please Enter Start Time and End Time", Toast.LENGTH_SHORT).show();

            return;
        }
        LocalDateTime starTimeData = converStringtoLocalDateTime(starTime);
        LocalDateTime endTimeData = converStringtoLocalDateTime(endTime);

        if(starTimeData != null || endTimeData != null){

            long durasi = calculateDuration(starTimeData,endTimeData);
            double totalHarga = calculateTotalHarga(durasi);

            Reservation reservation = new Reservation(starTimeData, endTimeData, (int) totalHarga);

            String key = dbReference.push().getKey();
            if(key!= null){
                dbReference.child(key).setValue(reservation);
                Toast.makeText(requireContext(), "Reservation Booked Successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireContext(), "Failed to Book Reservation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LocalDateTime converStringtoLocalDateTime(String timeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalDateTime.parse(timeString, formatter);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private long calculateDuration(LocalDateTime startTime, LocalDateTime endTime){
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }

    private double calculateTotalHarga(long durasi){
        double hargaPerHour = 15000;
        return (durasi/60.0)*hargaPerHour;
    }
}
