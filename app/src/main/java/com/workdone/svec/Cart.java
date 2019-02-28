package com.workdone.svec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

public class Cart extends Fragment {
    private RecyclerView mRecyclerView;
    private MyAdapter mListadapter;
    ArrayList<User> data = new ArrayList<User>();
    Set<User> set = new HashSet<User>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.cartpage, container, false);
        final Controller aController = (Controller) getActivity().getApplicationContext();
        data=aController.getItems();
        set.addAll(data);
        data.clear();
        data.addAll(set);
        mRecyclerView = (RecyclerView) myview.findViewById(R.id.recycleview);
        mRecyclerView.invalidate();
        mListadapter = new MyAdapter(getContext(), data );
        mRecyclerView.setAdapter(mListadapter);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        return myview;
    }


}
