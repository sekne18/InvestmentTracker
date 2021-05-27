package com.example.investmenttracker.AnalysisFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;


public class IndicatorsFragment extends Fragment {

    private View ruleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ruleView = inflater.inflate(R.layout.fragment_indicators, container, false);
        closebutton();
        return ruleView;
    }

    private void closebutton() {
        ImageButton closeButton = ruleView.findViewById(R.id.closeButton9);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container2);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }
}