package com.example.hamid.restify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hamid.restify.Tabs.AllRequests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ActionBar actionbar;
    private NavigationView navigationView;
    private static boolean active = false;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        actionbar = getSupportActionBar(); //so subclass can change ActionBar text

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        AllRequests home = new AllRequests();

        fragTransaction.replace(R.id.frame, home, "fragment1").commit();

        navigationView.getMenu().getItem(0).setChecked(true);

        if (actionbar != null) actionbar.setTitle("Restify");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && active)
                    id1 = firebaseAuth.getCurrentUser().getUid();
            }
        };

        //TODO :: id1 ISN'T GETTING ASSIGNED THE VALUE OF THE USER'S ID FOR SOME REASON!!!!////

        //setName(); //this will set the name in the nav drawer to the 'name' in database
    }

    private void setName() {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mUsers = mRef.child("Users");
        final DatabaseReference currUser = mUsers.child(id1);

        currUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name =  dataSnapshot.child("Name").getValue().toString();

                View header = navigationView.getHeaderView(0);
                TextView nameField = (TextView) header.findViewById(R.id.name_field);
                nameField.setText(name);
            } @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
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
}