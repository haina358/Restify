package com.example.hamid.restify.Externals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.hamid.restify.HomeThreeTabs;
import com.example.hamid.restify.SignUpLogin;
import com.google.firebase.auth.FirebaseAuth;

public class DispatcherActivity extends Activity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private static boolean active; //indicates if activity is active

    ////////////////////////
    ///TODO:: Rename to 'SplashScreen' & let it serve as the launcher activity///////
    ////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        active = true;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //TODO:: DO EACH 'IF' STATEMENT ON A THREAD, SO THERE'S
                ///TODO:: NO LAG WHEN WE FIRST OPEN EACH SCREEN

                //take user to 'SignUpLogin' if logged out
                if (firebaseAuth.getCurrentUser() == null && active) {
                    Intent sign = new Intent(DispatcherActivity.this, SignUpLogin.class);

                    sign.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    sign.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevents black screen upon start
                    startActivity(sign);
                    overridePendingTransition(0, 0);
                }
                //if user is logged in & email verified
                else if (firebaseAuth.getCurrentUser() != null &&
                        firebaseAuth.getCurrentUser().isEmailVerified() && active) {
                    Intent home = new Intent(DispatcherActivity.this, HomeThreeTabs.class);

                    home.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevents black screen upon start
                    startActivity(home);
                    overridePendingTransition(0, 0);
                }

                //if user is logged in & email NOT verified
                 if (firebaseAuth.getCurrentUser() != null &&
                        !firebaseAuth.getCurrentUser().isEmailVerified() && active) {
                     Intent sign = new Intent(DispatcherActivity.this, SignUpLogin.class);

                     sign.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                     sign.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevents black screen upon start
                     startActivity(sign);
                     overridePendingTransition(0, 0);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() { //overriding this so the listeners don't clash
        super.onStop();
        active = false;
    }
}