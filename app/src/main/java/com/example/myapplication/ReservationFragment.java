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

import com.example.myapplication.cookies.cookiesDb;
import com.example.myapplication.model.Reservation;
import com.example.myapplication.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ReservationFragment extends Fragment {

    private static final String RESERVATION_NODE = "reserves";
    private EditText editTextStart, editTextEnd;
    private Button bookButton;

    private DatabaseReference dbReference;

    private cookiesDb dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        editTextStart = rootView.findViewById(R.id.editTextStartTime);
        editTextEnd = rootView.findViewById(R.id.editTextEndTime);
        bookButton = rootView.findViewById(R.id.BookButton);

        dbReference = FirebaseDatabase.getInstance().getReference(RESERVATION_NODE);

        dbHandler = new cookiesDb(getContext());

        String selectedUniversityName = getActivity().getIntent().getExtras().getString("selectedUniversityName");

        try {
            rootView.setBackgroundResource(R.drawable.class.getField(selectedUniversityName).getInt(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            rootView.setBackgroundResource((R.drawable.alamsutera));
        }

        bookButton.setOnClickListener(e -> bookReservation(selectedUniversityName)); // Pass selected university name

        return rootView;
    }

    private void bookReservation(String selectedUniversityName) {
        String startTimeString = editTextStart.getText().toString().trim();
        String endTimeString = editTextEnd.getText().toString().trim();

        ArrayList<User> userCookies = dbHandler.getUserCookies();

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

        Reservation reservation = new Reservation(startTime.atDate(LocalDate.now()), endTime.atDate(LocalDate.now()),15000);

        reservation.setTotalHarga(totalHarga);

        reservation.setUniversityName(selectedUniversityName);

        if(!userCookies.isEmpty()){
            User user = userCookies.get(0);

            reservation.setUsername(user.getUsername());
        }

        String key = dbReference.push().getKey();
        if (key != null) {
            saveReservationToFirebase(key, reservation);
            navigateToPaymentActivity(selectedUniversityName, totalHarga);
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

    private void navigateToPaymentActivity(String location, double price) {
        Intent paymentIntent = new Intent(getContext(), PaymentActivity.class);
        paymentIntent.putExtra("location", location);
        paymentIntent.putExtra("price", price);
        startActivity(paymentIntent);
    }
}
