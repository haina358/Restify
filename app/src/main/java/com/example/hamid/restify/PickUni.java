package com.example.hamid.restify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PickUni extends AppCompatActivity {

    private static String university;
    private static boolean active = false;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private int spinnerCount = 0, spinnersInitialized = 0;
    private String id1, chooseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_uni);

        Spinner spinner = (Spinner) findViewById(R.id.uniSpinner);
        ArrayAdapter<CharSequence> universities;
        universities = ArrayAdapter.createFromResource(this, R.array.universities, android.R.layout.simple_spinner_item);

        universities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(universities);

        spinnerCount = 1;
        chooseText = university = getString(R.string.choose);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinnersInitialized < spinnerCount)
                    spinnersInitialized++;
                else //only detect selection events that are not done whilst initializing
                    university = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && active)
                    id1 = firebaseAuth.getCurrentUser().getUid();
            }
        };

        Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!university.equals(chooseText)) //ensure selection is made
                    processing();
                else makeToast("Select an institution");
            }
        });
    }

    private void processing() {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mUsers = mRef.child("Users");
        final DatabaseReference currUser = mUsers.child(id1);

        final Map<String, String> map = new HashMap<>();

        currUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map.put("Name", dataSnapshot.child("Name").getValue().toString());
                map.put("University", university);
                currUser.setValue(map); //add uni to database
            } @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Intent home = new Intent(PickUni.this, HomeThreeTabs.class);
        //clear the activity stack, then start the home activity
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    private void makeToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}