package com.workdone.svec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment implements View.OnClickListener,BottomNavigationView.OnNavigationItemSelectedListener {

    CardView hire,gethire,orders;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        hire=(CardView)myView.findViewById(R.id.hire);
        gethire=(CardView)myView.findViewById(R.id.gethire);
        orders=(CardView)myView.findViewById(R.id.orders);
        hire.setOnClickListener(this);
        gethire.setOnClickListener(this);
        orders.setOnClickListener(this);
        return myView;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new DashboardFragment();
                break;

            case R.id.navigation_notifications:
                fragment = new NotificationsFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hire:
                fragment = new Hire();
                break;

            case R.id.gethire:
                fragment = new GetHireFragment();
                break;

            case R.id.orders:
                fragment = new Order();
                break;

            default:
                break;
        }
        loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}