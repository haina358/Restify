package com.example.hamid.restify.signUp_pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hamid.restify.R;
import com.example.hamid.restify.SignUpLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.hamid.restify.signUp_pages.SignUpActivity1.firstLast;
import static com.example.hamid.restify.signUp_pages.SignUpActivity2.email;

public class SignUpActivity3 extends AppCompatActivity {

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListenerE;
    private EditText pass, confirmPass;
    private boolean firstAuth = true, active = false;
    private static HashMap<String, String> map1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        pass = (EditText) findViewById(R.id.pass);
        confirmPass = (EditText) findViewById(R.id.confirmPass);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {processing();}
        });

        mAuthListenerE = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && firstAuth && active) {
                    firstAuth = false;
                    sendEmailVerification();

                    FirebaseAuth.getInstance().signOut();
                }
            }
        };
        //ENABLE/DISABLE "CONTINUE" BUTTON(S) BASED ON THE EDIT TEXTS!!!
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification();
    }

    private void processing() {
        final String password1 = pass.getText().toString().trim();
        String password2 = confirmPass.getText().toString().trim();

        if (!password1.equals("") && !password2.equals("")) {
            if (password1.equals(password2)) {
                if (password1.length() >= 8) {
                    mProgress.setMessage("Creating account ...");
                    mProgress.show();
                    mProgress.setCanceledOnTouchOutside(false);

                    mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //adding to database/////////////////////
                                final String id = task.getResult().getUser().getUid();
                                final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                                final DatabaseReference mUsers = mRef.child("Users");

                                map1 = new HashMap<>();
                                map1.put("Name", firstLast);
                                mUsers.child(id).setValue(map1);

                                mProgress.dismiss();

                                //MAKE THIS A DIALOG WINDOW/////////////////

                                makeToast("Account created. Check email for verification link");
                                Intent signUpLogin = new Intent(SignUpActivity3.this, SignUpLogin.class);

                                //clear the activity stack, then start the signUpLogin activity
                                signUpLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(signUpLogin);
                            } else {
                                mProgress.dismiss();
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    makeToast("User with that email already exists");
                                else
                                    makeToast("We couldn't create your account");
                            }
                        }
                    });
                } else
                    makeToast("Passwords must be at least 8 characters");
            } else
                makeToast("Passwords do not match");
        } else
            makeToast("Please fill all fields");
    }

    private void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListenerE);
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}