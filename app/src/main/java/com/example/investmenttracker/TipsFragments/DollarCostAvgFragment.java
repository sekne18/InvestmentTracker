package com.example.investmenttracker.TipsFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class DollarCostAvgFragment extends Fragment {

    private View dollarCostAvgView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dollarCostAvgView = inflater.inflate(R.layout.fragment_dollar_cost_avg, container, false);
        closebutton();
        return dollarCostAvgView;
    }

    private void closebutton() {
        ImageButton closeButton = dollarCostAvgView.findViewById(R.id.closeButton4);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }
}