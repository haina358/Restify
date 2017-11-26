package com.example.hamid.restify.Externals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hamid.restify.R;

/**
 * Using the view of this blank fragment so that frags. don't overlap when 'Home'
 * is selected in navigation*/
public class Blank extends Fragment {

    public Blank() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

}