package com.example.myapplication;

import android.content.Intent;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class ReservationFragment extends Fragment {

    private static final String RESERVATION_NODE = "reserves";
    private EditText editTextStart, editTextEnd;
    private Button bookButton;

    private DatabaseReference dbReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.actvity_reservation, container, false);

        editTextStart = rootView.findViewById(R.id.editTextStartTime);
        editTextEnd = rootView.findViewById(R.id.editTextEndTime);
        bookButton = rootView.findViewById(R.id.BookButton);

        dbReference = FirebaseDatabase.getInstance().getReference(RESERVATION_NODE);

        String selectedUniversityName = getArguments().getString("selectedUniversityName");

        bookButton.setOnClickListener(e -> bookReservation(selectedUniversityName)); // Pass selected university name

        return rootView;
    }

    private void bookReservation(String selectedUniversityName) {
        String startTimeString = editTextStart.getText().toString().trim();
        String endTimeString = editTextEnd.getText().toString().trim();


        if (startTimeString.isEmpty() || endTimeString.isEmpty()) {
            showToast("Please Enter Start Time and End Time");
            return;
        }

        if (!isValidTimeFormat(startTimeString) || !isValidTimeFormat(endTimeString)) {
            showToast("Please use the format HH:mm");
            return;
        }

        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("HH:mm"));

        if (startTime.isAfter(endTime)) {
            showToast("End time must be after start time");
            return;
        }

        long duration = ChronoUnit.MINUTES.between(startTime, endTime);
        double totalHarga = calculateTotalHarga(duration);

        Reservation reservation = new Reservation(startTime.atDate(LocalDate.now()), endTime.atDate(LocalDate.now()), (int) totalHarga);

        reservation.setUniversityName(selectedUniversityName);
        String key = dbReference.push().getKey();
        if (key != null) {
            saveReservationToFirebase(key, reservation);
            navigateToPaymentActivity(selectedUniversityName, (int) totalHarga);
        } else {
            showToast("Failed to Book Reservation");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void saveReservationToFirebase(String key, Reservation reservationModel) {
        dbReference.child(key).setValue(reservationModel)
                .addOnSuccessListener(e -> showToast("Reservation Booked Successfully"))
                .addOnFailureListener(f -> showToast("Failed to Book Reservation"));
    }

    private boolean isValidTimeFormat(String timeString) {
        try {
            LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private double calculateTotalHarga(long duration) {
        double hargaPerHour = 15000;
        return (duration / 60.0) * hargaPerHour;
    }

    private void navigateToPaymentActivity(String location, int price) {
        Intent paymentIntent = PaymentActivity.createIntent(requireContext());
        paymentIntent.putExtra("location", location);
        paymentIntent.putExtra("price", price);
        startActivity(paymentIntent);
    }
}
