package com.example.investmenttracker.TipsFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class NoiseFragment extends Fragment {

    private View noiseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        noiseView = inflater.inflate(R.layout.fragment_noise, container, false);
        closebutton();
        return noiseView;
    }

    private void closebutton() {
        ImageButton closeButton = noiseView.findViewById(R.id.closeButton6);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }

}