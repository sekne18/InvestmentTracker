package com.example.investmenttracker.AnalysisFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class PatternsFragment extends Fragment {

    private View patternsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patternsView = inflater.inflate(R.layout.fragment_patterns, container, false);
        closebutton();
        return patternsView;
    }

    private void closebutton() {
        ImageButton closeButton = patternsView.findViewById(R.id.closeButton12);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container2);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }
}