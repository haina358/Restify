package com.example.hamid.restify.signUp_pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hamid.restify.R;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class SignUpActivity2 extends AppCompatActivity {

    protected static String email;
    private EditText emailIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        emailIn = (EditText) findViewById(R.id.emailIn);

        Button continue2 = (Button) findViewById(R.id.continue2);
        continue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {processing();}
        });

        //ENABLE/DISABLE "CONTINUE" BUTTON(S) BASED ON THE EDIT TEXTS!!!
    }

    private void processing() {
       email = emailIn.getText().toString().trim();
        if (!email.equals("")) {
            boolean isEDU = false;

            if (email.length() > 4) {
                String checkEDU = email.substring(email.length() - 4);
                isEDU = checkEDU.equals(".edu");
            }

            if (isValidEmailAddress(email) && isEDU) {

                startActivity(new Intent(SignUpActivity2.this, SignUpActivity3.class));
            } else makeToast("Please enter a valid University email");
        } else makeToast("Please enter email");
    }

    private void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {result = false;}
        return result;
    }
}