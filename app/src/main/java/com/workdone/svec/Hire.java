package com.workdone.svec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static android.app.Activity.RESULT_OK;

public class Hire extends Fragment implements CardView.OnClickListener {
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    Dialog dialog;
    ImageView loc;
    TextView loca;
    private LocationManager locationManager;
    private LocationListener locationListener;
    String str="";
    int PLACE_PICKER_REQUEST = 1;
    public PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.locationpopoup);
        loc = (ImageView) dialog.findViewById(R.id.location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        loca =(TextView)dialog.findViewById(R.id.loc);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        View myview = inflater.inflate(R.layout.fragment_hire, container, false);
            CardView transport, plumber, electrician, cengineer, construct, carpenter;
            transport = (CardView) myview.findViewById(R.id.transport);
            plumber = (CardView) myview.findViewById(R.id.plumber);
            electrician = (CardView) myview.findViewById(R.id.electrician);
            cengineer = (CardView) myview.findViewById(R.id.cengineer);
            construct = (CardView) myview.findViewById(R.id.construction);
            carpenter = (CardView) myview.findViewById(R.id.carpenter);
            transport.setOnClickListener((View.OnClickListener) this);
            plumber.setOnClickListener((View.OnClickListener) this);
            electrician.setOnClickListener((View.OnClickListener) this);
            cengineer.setOnClickListener((View.OnClickListener) this);
            construct.setOnClickListener((View.OnClickListener) this);
            carpenter.setOnClickListener((View.OnClickListener) this);
            loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                        dialog.dismiss();
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }

            });
        return myview;
    }
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void   onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                loca.setText(place.getAddress()+" "+place.getName());
                Toast.makeText(getActivity(), place.getAddress()+" "+place.getName(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }
    }
    Fragment fragment;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.transport: fragment=new Transport();
                break;
            case R.id.electrician: fragment=new Electrical();
                break;
            case R.id.plumber: fragment=new Plumbing();
                break;
            case R.id.carpenter: fragment=new Carpenting();
                break;
            case R.id.construction: fragment=new Construction();
                break;
            case R.id.cengineer: fragment=new Cengineer();
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
