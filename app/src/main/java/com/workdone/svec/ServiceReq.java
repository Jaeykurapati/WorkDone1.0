package com.workdone.svec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ServiceReq extends Fragment {

    public static final String s1 = "Pipe/Tap Fitting";
    public static final String s2 = "Water Leakages";
    public static final String s3 = "Repairs & Fixes";
    public static final String s4 = "Installation Services";

    public static final String s11 = "Fixed Price Installation/Repair Services";
    public static final String s12 = "Clearing Blockage";
    public static final String s13 = "Other Installation/Repair Services";

    public static final String s111 = "Bath Fittings";
    public static final String s112 = "Shower";
    public static final String s113 = "Health Faucet (Jet)";
    public static final String s114 = "Tap & Mixer";
    public static final String s115 = "Health Faucet (Jet)";
    public static final String s116 = "Water Closet (W.C)";
    public static final String s117 = "Sink";
    public static final String s118 = "Geyser";
    public static final String s119 = "Water Tank Installation";
    public static final String s1110 = "Water Tank Cleaning";
    public static final String s1111 = "Additional Service";

    public static final String s121 = "Clearing Blockage";
    public static final String s131 = "Other/ Bulk Works";
    String[] filter1 = {"Choose", s1, s2,s3,s4};

    String[] s1list = {"Choose", s11,s12,s13};
    String[] s11list = {"Choose", s111,s112,s113,s114,s115,s116,s117,s118,s119,s1110,s1111};
    String[] s12list = {"Choose", s121};
    String[] s13list = {"Choose", s131};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.servicereq, container, false);
        Spinner spinner1,spinner2,spinner3;
            spinner1 = (Spinner)myview. findViewById(R.id.filter1);
            spinner2 = (Spinner)myview. findViewById(R.id.filter2);
            spinner3 = (Spinner)myview. findViewById(R.id.filter3);

            spinner1.setSelection(0);
            spinner2.setSelection(0);
            spinner3.setSelection(0);

            setSpinner(spinner1, filter1);
            spinner2.setEnabled(false);
            spinner3.setEnabled(false);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filter1);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);


            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    switch (i){

                        case 0:
                            spinner1.setSelection(0);
                            spinner2.setSelection(0);

                            break;

                        case 1:
                            setSpinner(spinner2, s1list);
                            spinner2.setEnabled(true);
                            break;

                        case 2:
                            setSpinner(spinner2, s1list);
                            spinner2.setEnabled(true);
                            break;

                        case 3:
                            setSpinner(spinner2, s1list);
                            spinner2.setEnabled(true);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String selectedItem = adapterView.getSelectedItem().toString();

                    if (spinner1.getSelectedItemPosition() == 0) {
                        spinner2.setSelection(0);
                        return;

                    }
                    if (i == 0){
                        return;
                    }



                    switch (i){

                        case  1:
                            setSpinner(spinner3, s11list);
                            spinner3.setEnabled(true);
                            break;

                        case 2 :
                            setSpinner(spinner3, s12list);
                            spinner3.setEnabled(true);
                            break;

                        case 3:
                            setSpinner(spinner3, s13list);
                            spinner3.setEnabled(true);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (spinner2.getSelectedItemPosition() == 0 || spinner1.getSelectedItemPosition() == 0) {
                        spinner2.setSelection(0);
                        spinner3.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        Button register=(Button)myview.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mFragmentMgr= getFragmentManager();
                FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
                mTransaction.replace(R.id.fragment_container, new Cengineer())
                        .addToBackStack(null)
                        .commit();
            }
        });
            return myview;
        }
    private void setSpinner(Spinner spinner2, String[] states) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner2.setAdapter(adapter);
    }
}

