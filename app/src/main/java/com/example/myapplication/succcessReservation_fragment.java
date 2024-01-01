//package com.example.myapplication;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class succcessReservation_fragment extends Fragment {
//private succcessReservation_adapter adapter;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstance){
//        View rootView = inflater.inflate(R.layout.reservation_success,containter, false);
//
//        RecyclerView recyclerView = rootView.findViewById(R.id.reservationRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        adapter = new succcessReservation_adapter();
//        recyclerView.setAdapter(adapter);
//
//        //Buat Database dulu buat nampilin datanya
////        List<Data dari Firebase> dummy = getDummy;
////        adapter.setReservationList(dummy);
////
////        adapter.setOnItemClickListener(position ->{
////            replaceFragmentWithDetails(position);
////        });
//
//        return  rootView;
//    }
//}
