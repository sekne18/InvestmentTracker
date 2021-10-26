package com.example.investmenttracker.NavigationFragments;

import static com.example.investmenttracker.MainActivity.api_currencies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

public class SettingsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View favView = inflater.inflate(R.layout.fragment_settings, container, false);
        Spinner currSpinner = (Spinner) favView.findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.currencies));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currSpinner.setAdapter(myAdapter);
        switch (Helper.currency) {
            case "$":
                currSpinner.setSelection(0);
                break;
            case "€":
                currSpinner.setSelection(1);
                break;
        }
        currSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.fonts));
                ((TextView) view).setTextSize(17);
                String oldCurr = Helper.currency;
                Helper.currency = currSpinner.getItemAtPosition(position).toString();
                if (!oldCurr.equals(Helper.currency)) {
                    api_currencies.RefreshDataFromAPI(Helper.currency);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return favView;
    }



}
