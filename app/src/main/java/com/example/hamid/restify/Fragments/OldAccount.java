package com.example.hamid.restify.Fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hamid.restify.R;
import com.example.hamid.restify.SignUpLogin;
import com.google.firebase.auth.FirebaseAuth;

public class OldAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_account);

        mAuth = FirebaseAuth.getInstance();

        //initialize the button and listen for a click
        Button logOutBtn = (Button) findViewById(R.id.logOut);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent signUpLogin = new Intent(OldAccount.this, SignUpLogin.class);
                //clear the activity stack, then start the signUpLogin activity
                signUpLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signUpLogin);
            }
        });

        //IN THE ACCOUNT PAGE, GIVE THE OPTION TO RESET PASSWORD

    }

}