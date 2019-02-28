package com.workdone.svec;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Dialog dialog;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    TextView manage;
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //loading the default fragment
        loadFragment(new DashboardFragment());
        dialog=new Dialog(this);
        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }
    public void ShowPopup(){
        TextView txtclse;
        Button logout;
        dialog.setContentView(R.layout.fragment_profile_popup);
        txtclse=(TextView)dialog.findViewById(R.id.txtclose);
        txtclse.setText("X");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        manage=(TextView)dialog.findViewById(R.id.manage);
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fragment=new ProfileFragment();
                loadFragment(fragment);
            }
        });
        TextView email =(TextView)dialog.findViewById(R.id.email);
        email.setText(firebaseAuth.getCurrentUser().getEmail());
        TextView name =(TextView)dialog.findViewById(R.id.name);
        name.setText(firebaseAuth.getCurrentUser().getDisplayName());
        logout=(Button)dialog.findViewById(R.id.logout);
        txtclse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                dialog.dismiss();
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new DashboardFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new Cart();
                break;

            case R.id.navigation_notifications:
                fragment = new NotificationsFragment();
                break;

            case R.id.navigation_profile:
                ShowPopup();;
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}