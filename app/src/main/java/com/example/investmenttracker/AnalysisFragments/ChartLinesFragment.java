package com.example.investmenttracker.AnalysisFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class ChartLinesFragment extends Fragment {

    private View chartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chartView = inflater.inflate(R.layout.fragment_chart_lines, container, false);
        closebutton();
        return chartView;
    }

    private void closebutton() {
        ImageButton closeButton = chartView.findViewById(R.id.closeButton11);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container2);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }
}