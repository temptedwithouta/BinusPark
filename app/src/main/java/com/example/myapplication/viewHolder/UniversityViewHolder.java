package com.example.myapplication.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class UniversityViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTV;

    public TextView locationTV;

    public TextView parkingSlotTV;

    public UniversityViewHolder(View itemView){
        super(itemView);

        nameTV = itemView.findViewById(R.id.nameTV);

        locationTV = itemView.findViewById(R.id.locationTV);

        parkingSlotTV = itemView.findViewById(R.id.parkingSlotTV);
    }
}
