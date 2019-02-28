package com.workdone.svec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Hire extends Fragment implements CardView.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        return myview;
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
            case R.id.cengineer: fragment=new ServiceReq();
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
