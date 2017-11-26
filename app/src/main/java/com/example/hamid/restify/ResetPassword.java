package com.example.hamid.restify;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hamid.restify.Externals.CheckNetwork;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final EditText emailText = (EditText) findViewById(R.id.resetEmail);
        emailText.setText(LogInActivity.loginEmail); //populates email field

        Button resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                if (!email.equals("")) {
                    //if device is not connected to internet..
                    if(!CheckNetwork.isInternetAvailable(ResetPassword.this)) {
                        makeToast("Please connect to the internet");
                    }
                    else { //internet connection
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) makeToast("Email sent");
                                else makeToast("We can't find an account with that email");
                            }
                        });
                    }
                } else makeToast("Enter email");
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}