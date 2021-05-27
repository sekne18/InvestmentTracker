package com.example.investmenttracker.TipsFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.Activities.Tips.TipsActivity;
import com.example.investmenttracker.R;

public class Spot_LeverageFragment extends Fragment {

    private View spotLevView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spotLevView = inflater.inflate(R.layout.fragment_spot__leverage, container, false);
        closebutton();
        return spotLevView;
    }

    private void closebutton() {
        ImageButton closeButton = spotLevView.findViewById(R.id.closeButton);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }

}