package com.example.investmenttracker.TipsFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class FomoFragment extends Fragment {

    private View fomoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fomoView = inflater.inflate(R.layout.fragment_fomo, container, false);
        closebutton();
        return fomoView;
    }

    private void closebutton() {
        ImageButton closeButton = fomoView.findViewById(R.id.closeButton3);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }

}