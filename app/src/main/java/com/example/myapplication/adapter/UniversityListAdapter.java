package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MapActivity;
import com.example.myapplication.R;
import com.example.myapplication.ReservationActivity;
import com.example.myapplication.model.University;
import com.example.myapplication.viewHolder.UniversityViewHolder;

import java.util.ArrayList;

public class UniversityListAdapter extends RecyclerView.Adapter<UniversityViewHolder> {
    private final ArrayList<University> universityList;

    private final Context context;

    public UniversityListAdapter(ArrayList<University> universityList, Context context){
        this.universityList = universityList;

        this.context = context;
    }

    public UniversityViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_item_layout, parent, false);

        return new UniversityViewHolder(view);
    }

    public void onBindViewHolder(UniversityViewHolder viewHolder, int position){
        University university = universityList.get(position);

        String universityLocation = university.getLocation();

        viewHolder.nameTV.setText(university.getName());

        viewHolder.locationTV.setText(university.getLocation());

        viewHolder.parkingSlotTV.setText(university.getParkingSlot());

//        viewHolder.itemView.setBackgroundResource(R.drawable.bekasi);

        try {
            viewHolder.itemView.setBackgroundResource(R.drawable.class.getField(universityLocation).getInt(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            viewHolder.itemView.setBackgroundResource((R.drawable.alamsutera));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(context, ReservationActivity.class);

                intent.putExtra("location", university.getLocation());

                intent.putExtra("title", "@" + university.getName() + " " + university.getLocation());

                intent.putExtra("latitude", university.getLatitude());

                intent.putExtra("longitude", university.getLongitude());

                intent.putExtra("selectedUniversityName", university.getLocation());

                context.startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        int universityListSize = universityList.size();

        return universityListSize;
    }
}
