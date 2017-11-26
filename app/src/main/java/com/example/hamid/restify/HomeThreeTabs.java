package com.example.hamid.restify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.hamid.restify.Externals.Blank;
import com.example.hamid.restify.Externals.CheckNetwork;
import com.example.hamid.restify.Fragments.MyAccount;
import com.example.hamid.restify.Fragments.Settings;
import com.example.hamid.restify.Tabs.AllRequests;
import com.example.hamid.restify.Tabs.Events;
import com.example.hamid.restify.Tabs.MyRequests;

/**'MainNav' is extended so that this tabbed Activity class can have the
 * Navigation drawer*/

public class HomeThreeTabs extends MainNav
        implements NavigationView.OnNavigationItemSelectedListener {

    protected FloatingActionButton fab1;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @SuppressWarnings("all")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //since super class is called, subclass inherits  all of the layout as well

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home_three_tabs, null, false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(1).select(); //start at 'All Requests'

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fab1.show();
                        break;
                    case 1:
                        fab1.show();
                        break;
                    case 2:
                        fab1.hide();
                        break;
                }
            } @Override
            public void onPageScrollStateChanged(int state) {}
        });

        fab1 = (FloatingActionButton) findViewById(R.id.fab);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {

            case R.id.home: //opens home fragment (with the 3 tabs)
                fab1.show();
                actionbar.setTitle("Restify");
                tabLayout.setVisibility(View.VISIBLE);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.setCurrentItem(1); //'All Requests' is the home page

                Blank home = new Blank(); //so fragments don't overlap
                fragTransaction.replace(R.id.main_content, home, "fragment1").commit();
                drawer.closeDrawers();
                return true;
            case R.id.account: //opens account fragment (no tabs)
                actionbar.setTitle("My Account");
                tabLayout.setVisibility(View.GONE);
                mViewPager.setCurrentItem(2); //no FAB ('Events' tab)

                mViewPager.setAdapter(null); //no swiping through fragment tabs
                MyAccount account = new MyAccount();
                fragTransaction.replace(R.id.main_content, account, "fragment2").commit();
                drawer.closeDrawers();
                return true;
            case R.id.settings: //opens settings fragment (no tabs)
                actionbar.setTitle("Settings");
                tabLayout.setVisibility(View.GONE);
                mViewPager.setCurrentItem(2);

                mViewPager.setAdapter(null);
                Settings settings = new Settings();
                fragTransaction.replace(R.id.main_content, settings, "fragment3").commit();
                drawer.closeDrawers();
                return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //returns a fragment corresponding to one of the sections/tabs/pages
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            switch (position) {
                case 0:
                    return new MyRequests();
                case 1:
                    return new AllRequests();
                case 2:
                    return new Events();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Requests";
                case 1:
                    return "All";
                case 2:
                    return "Events";
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if device is not connected to internet..
         *we want this to show every time the user re-opens this Activity*/
        if(!CheckNetwork.isInternetAvailable(HomeThreeTabs.this)) {
            Snackbar.make(findViewById(R.id.main_content), "No connection",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}