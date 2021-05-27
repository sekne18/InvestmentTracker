package com.example.investmenttracker.StrategieFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class RuleFragment extends Fragment {

    private View ruleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ruleView = inflater.inflate(R.layout.fragment_rule, container, false);
        closebutton();
        return ruleView;
    }

    private void closebutton() {
        ImageButton closeButton = ruleView.findViewById(R.id.closeButton8);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container3);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }

}