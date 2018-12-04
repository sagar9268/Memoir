package com.sagar.memoir;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, CalendarFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener, MainActivityFragment.OnFragmentInteractionListener {

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //load home fragment by default
        loadFragment(new MainActivityFragment());


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement
        if (id == R.id.navigation_settings) {
            fragment = new SettingFragment();
            return loadFragment(fragment);
        }
        if (id == R.id.navigation_home) {
            fragment = new MainActivityFragment();
            return loadFragment(fragment);
        }
        if (id == R.id.navigation_calendar) {
            fragment = new CalendarFragment();
            return loadFragment(fragment);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_settings:
                fragment = new SettingFragment();
                break;

            case R.id.navigation_calendar:
                fragment = new CalendarFragment();
                break;

            case R.id.navigation_home:
                fragment = new MainActivityFragment();
                break;

        }
        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfileSettingsSaveFragmentInteraction()
    {
        Toast.makeText(MainActivity.this,"Profile Updated!", Toast.LENGTH_SHORT).show();
    }
}
