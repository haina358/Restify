package com.example.hamid.restify;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hamid.restify.Externals.CheckNetwork;
import com.example.hamid.restify.signUp_pages.SignUpActivity1;

public class SignUpLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //keeps users activity open when they press home on phone
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

        Button signUp = (Button) findViewById(R.id.signUpButton);
        Button logIn = (Button) findViewById(R.id.loginButton);

        //opens the sign up page when the sign up button is clicked
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpLogin.this, SignUpActivity1.class));
            }
        });
        //opens the log in page when the login button is clicked
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpLogin.this, LogInActivity.class));
            }
        });
    }

    //go to home screen when back is pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if device is not connected to internet..
         *we want this to show every time the user re-opens this Activity*/
        if(!CheckNetwork.isInternetAvailable(SignUpLogin.this)) {
            Snackbar.make(findViewById(R.id.constLayout), "No connection",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}