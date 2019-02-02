package com.workdone.svec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class GetHireFragment extends Fragment implements MultiSpinner.OnMultipleItemsSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.activity_regi, container, false);
        String[] array = {"Carpenter", "Construction", "Plumber", "Electrician", "Transportation", "Computer Engineer"};
        MultiSpinner multiSelectionSpinner = (MultiSpinner) myview.findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setSelection(new int[]{});
        multiSelectionSpinner.setListener(this);
        EditText dob=(EditText)myview.findViewById(R.id.Birthday);
        return  myview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

}
