package com.example.investmenttracker.TipsFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;

public class BuyFragment extends Fragment {

    private View buyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buyView = inflater.inflate(R.layout.fragment_buy, container, false);
        closebutton();
        return buyView;
    }

    private void closebutton() {
        ImageButton closeButton = buyView.findViewById(R.id.closeButton5);
        FrameLayout container = getActivity().findViewById(R.id.fragment_container);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.INVISIBLE);
            }
        });
    }


}