package com.example.investmenttracker.AnalysisFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class FundamentalsFragment extends Fragment {

    private View fundView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fundView = inflater.inflate(R.layout.fragment_fundamentals, container, false);
        closebutton();
        return fundView;
    }

    private void closebutton() {
        ImageButton closeButton = fundView.findViewById(R.id.closeButton10);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container2);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }

}