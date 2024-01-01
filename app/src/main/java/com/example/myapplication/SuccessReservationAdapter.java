package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuccessReservationAdapter extends RecyclerView.Adapter<SuccessReservationAdapter.ReservationViewHolder> {

    private List<reservationModel> reservationList;
    private OnItemClickListener listener;

    public void setReservationList(List<reservationModel> reservationList) {
        this.reservationList = reservationList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_reservation, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        reservationModel currentReservation = reservationList.get(position);

        // Customize based on your item_reservation.xml layout
        holder.textViewStartTime.setText("Start Time: " + currentReservation.getStartTime().toString());
        holder.textViewEndTime.setText("End Time: " + currentReservation.getEndTime().toString());
        holder.textViewDuration.setText("Duration: " + currentReservation.getDurasi() + " minutes");
        holder.textViewTotalHarga.setText("Total Harga: Rp. " + currentReservation.getTotalHarga());
    }

    @Override
    public int getItemCount() {
        return reservationList == null ? 0 : reservationList.size();
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStartTime;
        TextView textViewEndTime;
        TextView textViewDuration;
        TextView textViewTotalHarga;

        ReservationViewHolder(View itemView) {
            super(itemView);

            // Initialize views from item_reservation.xml
            textViewStartTime = itemView.findViewById(R.id.startTime);
            textViewEndTime = itemView.findViewById(R.id.endTime);


            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
