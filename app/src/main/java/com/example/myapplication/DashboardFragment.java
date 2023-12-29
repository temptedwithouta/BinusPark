package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    public DashboardFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstantanceState){
        return inflater.inflate(R.layout.activity_dashboard_1,container,false);
    }
}
