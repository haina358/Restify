package com.example.hamid.restify.signUp_pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hamid.restify.Externals.CheckNetwork;
import com.example.hamid.restify.R;

public class SignUpActivity1 extends AppCompatActivity {

    protected static String firstLast;
    private String first, last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        final EditText firstIn = (EditText) findViewById(R.id.fName);
        final EditText lastIn = (EditText) findViewById(R.id.lName);

        Button continueBtn = (Button) findViewById(R.id.continue1);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = firstIn.getText().toString().trim();
                last = lastIn.getText().toString().trim();

                if (!first.equals("") && !last.equals("")) {
                    //if device is not connected to internet..
                    if(!CheckNetwork.isInternetAvailable(SignUpActivity1.this)) {
                        makeToast("Please connect to the internet");
                    }
                    else {
                        firstLast = first + " " + last;
                        startActivity(new Intent(SignUpActivity1.this, SignUpActivity2.class));
                    }
                } else makeToast("Please enter first and last name");
            }
        });
        //ENABLE/DISABLE "CONTINUE" BUTTON(S) BASED ON INPUT' LENGTH
    }

    private void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}