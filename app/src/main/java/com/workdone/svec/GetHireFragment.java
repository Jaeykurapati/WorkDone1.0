package com.workdone.svec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class GetHireFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    public String str="";
    int PLACE_PICKER_REQUEST = 1;
    EditText loc,dob;
    Button reg;
    public PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    TextView mItemSelected;
    private GeoFire geoFire;
    private LatLng temp;
    Double lat,lag;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    AlertDialog.Builder mBuilder;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    String userId=firebaseAuth.getUid();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.activity_regi, container, false);
        TextView cate=(TextView)myview.findViewById(R.id.categories);
        reg=(Button)myview.findViewById(R.id.register);
        mBuilder = new AlertDialog.Builder(getActivity());
        listItems = getResources().getStringArray(R.array.categories);
        checkedItems = new boolean[listItems.length];
        loc=(EditText)myview.findViewById(R.id.worklocation);
        mItemSelected=(TextView)myview.findViewById(R.id.categories);
        dob=(EditText)myview.findViewById(R.id.birthday);
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);
                TextView tvDt = (TextView)myview.findViewById(R.id.birthday);
                tvDt.setText(day1 + "/" + month1 + "/" + year1);

            }
        };
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                R.style.AppBlackTheme, datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });


// Listener

        loc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }

    });

        cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }

                }
                cate.setText(item);
            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                    mItemSelected.setText("");
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {EditText name=(EditText) myview.findViewById(R.id.email);
                String username=name.getText().toString();
                Button reg=(Button)myview.findViewById(R.id.register);
                EditText temp=(EditText) myview.findViewById(R.id.birthday);
                String date=temp.getText().toString();
                List<String> categories=new ArrayList<String>();
                TextView temp2=(TextView) myview.findViewById(R.id.categories);
                String[] cat=temp2.getText().toString().split(",");
                for(int i=0;i<cat.length;i++)
                    categories.add(cat[i]);
                RadioGroup radioGroup = (RadioGroup)myview.findViewById(R.id.radioGroup1);

// get selected radioButton from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

// find the radioButton by returned id
                RadioButton radioButton = (RadioButton)myview.findViewById(selectedId);

// radioButton text
                String gender = String.valueOf(radioButton.getText());
                EditText temp3=(EditText) myview.findViewById(R.id.address);
                String addr=temp3.getText().toString();
                EditText str=(EditText)myview.findViewById(R.id.exp);
                int exp=Integer.parseInt(str.getText().toString());
                temp3=(EditText) myview.findViewById(R.id.worklocation);
                String location=temp3.getText().toString();
                temp3=(EditText) myview.findViewById(R.id.amount);
                int amount=Integer.parseInt(temp3.getText().toString());
                User user=new User();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                geoFire = new GeoFire(database.getReference().child("geofire_location"));

                geoFire.setLocation(userId,new GeoLocation(lat, lag),new
                        GeoFire.CompletionListener(){
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                //Do some stuff if you want to
                                if (error != null){
                                    Toast.makeText(getActivity(), "Location stored", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Write Failed:\t" + error.getMessage(),Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                user.writeUser(userId,username,gender,date,categories,addr,exp,location,amount,firebaseAuth.getCurrentUser().getEmail());
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
                    requestPermission();
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
                lat=place.getLatLng().latitude;
                lag=place.getLatLng().longitude;
                str=(place.getAddress()+" "+place.getName());
                loc.setText(str);
            }
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        dob.setText(month+"/"+day+"/"+year);
    }

}
