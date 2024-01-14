package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MapActivity;
import com.example.myapplication.PaymentActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Reservation;

import java.util.List;

public class SuccessReservationAdapter extends RecyclerView.Adapter<SuccessReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;

    private Context context;

    private String title;

    private String latitude;

    private String longitude;

    public SuccessReservationAdapter(Context context, List<Reservation> reservationList, String title, String longitude, String latitude) {
        this.context = context;

        this.reservationList = reservationList;

        this.title = title;

        this.latitude = latitude;

        this.longitude = longitude;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_suceess_reservation, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation currentReservation = reservationList.get(position);

        // Customize based on your item_reservation.xml layout
        holder.textViewUniversityLocation.setText(currentReservation.getUniversityName());
        holder.textViewStartTime.setText(currentReservation.getStartTimeString());
        holder.textViewEndTime.setText(currentReservation.getEndTimeString());
        holder.textViewTotalHarga.setText(Double.toString(currentReservation.getTotalHarga()));

        holder.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapActivity.class);

                intent.putExtra("title", title);

                intent.putExtra("latitude", latitude);

                intent.putExtra("longitude", longitude);

                context.startActivity(intent);
            }
        });

        holder.scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaymentActivity.class);

                intent.putExtra("location", currentReservation.getUniversityName());

                intent.putExtra("price", currentReservation.getTotalHarga());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList == null ? 0 : reservationList.size();
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUniversityLocation;
        TextView textViewStartTime;
        TextView textViewEndTime;
        TextView textViewTotalHarga;

        Button mapBtn;

        Button scanQr;

        ReservationViewHolder(View itemView) {
            super(itemView);

            // Initialize views from item_reservation.xml
            textViewUniversityLocation = itemView.findViewById(R.id.parkingInfo);
            textViewStartTime = itemView.findViewById(R.id.startTime);
            textViewEndTime = itemView.findViewById(R.id.endTime);
            textViewTotalHarga = itemView.findViewById(R.id.price);
            mapBtn = itemView.findViewById(R.id.openMap);
            scanQr = itemView.findViewById(R.id.scanQRCode);
        }
    }
}
