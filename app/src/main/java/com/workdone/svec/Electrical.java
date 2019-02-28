package com.workdone.svec;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class Electrical extends Fragment {
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    int PLACE_PICKER_REQUEST = 1;
    RecyclerView.Adapter adapter;
    Double lat,lag,distance=0.0;
    String[] array;
    Button details,show;
    String str="",name,fin="";
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    Set<String> set=new LinkedHashSet<String>();
    public PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    public Controller aController;
    EditText dist;
    TextView loc;
    private RecyclerView mRecyclerView;
    private GeoFire geoFire;
    private MyAdapter mListadapter;
    private LatLng temp;
    Dialog dialog;
    Button porder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_cengineer, container, false);
        mRecyclerView = (RecyclerView) myview.findViewById(R.id.recycleview);
        ArrayList<User> data = new ArrayList<User>();
        Set<User> dataset = new HashSet<User>();
        loc=(TextView) myview.findViewById(R.id.location);
        aController= (Controller) getActivity().getApplicationContext();
        dist=(EditText)myview.findViewById(R.id.distance);
        details=(Button) myview.findViewById(R.id.details);
        show=(Button) myview.findViewById(R.id.showd);
        porder=(Button)myview.findViewById(R.id.place);
        show.setEnabled(false);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.clear();
                dataset.clear();
                data.clear();
                try{
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                }catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.invalidate();
                aController.reset();
                str=null;
                set.clear();
                data.clear();
                dataset.clear();
                show.setEnabled(false);
                show.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show.setEnabled(true);
                    }
                }, 5000);
                if(loc.getText().toString().isEmpty()){
                    loc.setError("Pick Location");
                    return;
                }
                distance=Double.parseDouble(dist.getText().toString());
                database = FirebaseDatabase.getInstance();
                geoFire = new GeoFire(database.getReference().child("geofire_location"));
                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lag), distance);
                geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {

                    @Override
                    public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            str = messageSnapshot.getRef().getParent().getKey();
                            if(set.add(str)==true) {
                                db = database.getReference().child("Users").child(str);
                                db.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User profile = dataSnapshot.getValue(User.class);
                                        if (profile.categories.contains("Electrician"))
                                            data.add(profile);

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onDataExited(DataSnapshot dataSnapshot) {
                        // ...
                    }

                    @Override
                    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
                        // ...
                    }

                    @Override
                    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                        // ...
                    }

                    @Override
                    public void onGeoQueryReady() {
                        // ...
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {
                        // ...
                    }

                });
            }
        });
        porder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aController.getProductsArraylistSize()<1){
                    Toast.makeText(getContext(), "Please select atleast one Professional", Toast.LENGTH_SHORT).show();
                }
                else {
                    PlaceOrder();
                }
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataset.clear();
                dataset.addAll(data);
                data.clear();
                data.addAll(dataset);
                mListadapter = new MyAdapter(getContext(),data);
                mRecyclerView.setAdapter(mListadapter);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(layoutManager);
                dialog=dialog=new Dialog(getContext());
                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                        mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, final int position) {
                        //Values are passing to activity & to fragment as well
                        if(dialog.isShowing()!=true)
                            ShowPopup(data.get(position));
                    }
                }));
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
    public void PlaceOrder(){
        FragmentManager mFragmentMgr= getFragmentManager();
        FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
        mTransaction.replace(R.id.fragment_container, new Cart())
                .addToBackStack(null)
                .commit();
    }
    public void ShowPopup(User user){
        TextView txtclse,name,no;
        Button logout;
        dialog.setContentView(R.layout.profileview);
        txtclse=(TextView)dialog.findViewById(R.id.txtclose);
        txtclse.setText("X");
        Button pickup=(Button)dialog.findViewById(R.id.pickup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if(dialog.isShowing()!=true)
            dialog.show();
        no=(TextView) dialog.findViewById(R.id.mobile);
        TextView email =(TextView)dialog.findViewById(R.id.email);
        name =(TextView)dialog.findViewById(R.id.name);
        name.setText(user.getUsername());
        no.setText(user.getNo());
        email.setText(user.getEmail());
        TextView exp=(TextView)dialog.findViewById(R.id.exp);
        exp.setText(user.getExp().toString());
        TextView city=(TextView)dialog.findViewById(R.id.city);
        city.setText(user.getAddress().toString());
        Button back=(Button)dialog.findViewById(R.id.back);
        if(aController.find(user)==true){
            pickup.setText("Drop");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                dialog.dismiss();
            }
        });
        txtclse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                dialog.dismiss();
            }
        });
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pickup.getText().toString().equalsIgnoreCase("PickUp"))
                    aController.setProducts(user);
                else if(pickup.getText().toString().equalsIgnoreCase("Drop"))
                    aController.removeProducts(user);
                dialog.dismiss();
            }
        });
    }
    @Override
    public void   onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loc.setError(null);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                lat = place.getLatLng().latitude;
                lag = place.getLatLng().longitude;
                String temp=String.valueOf(lat)+"\t"+String.valueOf(lag);
                loc.setText(place.getName());
            }
        }
    }
    public static interface ClickListener{
        public void onClick(View view,int position);
    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
