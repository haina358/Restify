package com.example.hamid.restify.Tabs;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hamid.restify.HomeThreeTabs;
import com.example.hamid.restify.R;

public class MyRequests extends Fragment {


    public MyRequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_my_requests, container, false);
 //show the fab for this fragment

        return v;
    }

}