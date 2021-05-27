package com.example.investmenttracker.StrategieFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class HODLFragment extends Fragment {

    private View hodlView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hodlView = inflater.inflate(R.layout.fragment_h_o_d_l, container, false);
        closebutton();
        return hodlView;
    }

    private void closebutton() {
        ImageButton closeButton = hodlView.findViewById(R.id.closeButton7);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container3);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }
}