package com.example.hamid.restify;

import android.app.ProgressDialog;
import android.content.Intent; import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod; import android.widget.Toast;
import android.text.method.PasswordTransformationMethod; import android.view.View;
import android.widget.Button; import android.widget.EditText;
import android.widget.TextView;

import com.example.hamid.restify.Externals.CheckNetwork;
import com.google.android.gms.tasks.OnCompleteListener; import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult; import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.mail.internet.AddressException; import javax.mail.internet.InternetAddress;

public class LogInActivity extends AppCompatActivity {

    private EditText emailIn, passwordIn;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    public static FirebaseUser user;
    protected static String loginEmail;
    private static boolean active = false;
    private TextView resend;
    //counts how many times the listener calls isEmailVerified()
    //so we don't get "please verify" toast multiple times
    private static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //making object references to the two editable text fields
        emailIn = (EditText) findViewById(R.id.email);
        passwordIn = (EditText) findViewById(R.id.password);

        resend = (TextView) findViewById(R.id.resend); //reference to 'resend' textView

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification();
                makeToast("Verification email sent");
            }
        });

        mProgress = new ProgressDialog(this); //create the progress dialog box
        mAuth = FirebaseAuth.getInstance();

        //prevents "please verify email" text from showing if not
        //verified & nothing was typed
        mAuth.signOut();

        //used to know whether the user is logged in or not
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && active) { //user has logged in
                    user = firebaseAuth.getCurrentUser();

                    if (user.isEmailVerified()) { //ensure email is verified
                        String id = user.getUid();
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference mUsers = mRef.child("Users");
                        final DatabaseReference currUser = mUsers.child(id);

                        currUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChild("University")) { // first login
                                    Intent pickUni = new Intent(LogInActivity.this, PickUni.class);
                                    //clear the activity stack, then start the pickUni activity
                                    pickUni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(pickUni);
                                } else {
                                    //redirect the user to the home screen of the app
                                    Intent home = new Intent(LogInActivity.this, HomeThreeTabs.class);
                                    //clear the activity stack, then start the home activity
                                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(home);
                                }
                            } @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                    else //makes the text visible again if email isn't verified
                        resend.setVisibility(View.VISIBLE);

                    //only execute once per 'LogIn' button click
                    if (count == 1 && !user.isEmailVerified()){

                        /*we're gonna sign them out in 'Resend' once we send the
                        verification email*/
                        makeToast("Please verify your email");
                        count = 0; //reset the count
                    }
                }
            }
        };
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
                count = 1; //count is set every time login button is hit
            }
        });

        TextView forgotPass = (TextView) findViewById(R.id.resetPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, ResetPassword.class));
            }
        });

        final TextView showhide = (TextView) findViewById(R.id.showHide);
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showhide.getText().equals("HIDE")) {
                    showhide.setText(getString(R.string.SHOW));
                    passwordIn.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (showhide.getText().equals("SHOW")) {
                    showhide.setText(getString(R.string.HIDE));
                    passwordIn.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }



    //TODO:: RUN THIS METHOD ON A THREAD,, IT MIGHT BE SLOWING OVERALL EXECUTION
    ////TODO:: IT'S MAKING THE PROGRESS DIALOG APPEAR TOO LONG!!!////



    private void startSignIn() {
        String email = emailIn.getText().toString().trim();
        String password = passwordIn.getText().toString().trim();
        loginEmail = email;

        if (!email.equals("") && !password.equals("")) {
            boolean isEDU = false;

            //make sure the email is long enough to use substring
            if (email.length() > 4) {
                //makes sure the email ends in ".edu"
                String checkEDU = email.substring(email.length() - 4);
                isEDU = checkEDU.equals(".edu");
            }

            //makes sure the email is in a valid format & ends in ".edu"
            if (isValidEmailAddress(email) && isEDU) {
                //if device is not connected to internet..
                if(!CheckNetwork.isInternetAvailable(LogInActivity.this)) {
                    makeToast("Please connect to the internet");
                }
                else { //connected to internet
                    mProgress.setMessage("Logging in ...");
                    mProgress.show();
                    mProgress.setCanceledOnTouchOutside(false);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) mProgress.dismiss(); //successful login
                            else { //login failed, wrong login info..
                                mProgress.dismiss();
                                makeToast("Incorrect email or password");
                            }
                        }
                    });
                }
            } else makeToast("We can't find an account with that email");
        } else makeToast("Please enter email and password");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        active = true;
    }

    @Override
    protected void onStop() { //overriding this so the listeners don't clash
        super.onStop();
        active = false;
    }

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {result = false;}
        return result;
    }

    private void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}