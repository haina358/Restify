package com.example.hamid.restify.Tabs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hamid.restify.HomeThreeTabs;
import com.example.hamid.restify.R;


public class AllRequests extends Fragment {


    public AllRequests() {
        // Required empty public constructor
    }

    //FIGURE OUT HOW TO IMPLEMENT THE 3 TABS W/ THIS AS THE START (MIDDLE)///////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_requests, container, false);

        return v;
    }

}