package com.example.hamid.restify.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hamid.restify.R;
import com.example.hamid.restify.SignUpLogin;
import com.google.firebase.auth.FirebaseAuth;

public class MyAccount extends Fragment {

    private FirebaseAuth mAuth;

    public MyAccount() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_account, container, false);

        mAuth = FirebaseAuth.getInstance();

        //initialize the button and listen for a click
        Button logOutBtn = (Button) v.findViewById(R.id.logOut);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent signUpLogin = new Intent(getActivity(), SignUpLogin.class);
                //clear the activity stack, then start the signUpLogin activity
                signUpLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signUpLogin);
            }
        });

        return v;
    }

}