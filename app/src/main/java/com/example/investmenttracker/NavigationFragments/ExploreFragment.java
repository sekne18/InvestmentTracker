package com.example.investmenttracker.NavigationFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.investmenttracker.Activities.NewsActivity;
import com.example.investmenttracker.Activities.SearchActivity;
import com.example.investmenttracker.Activities.StrategiesActivity;
import com.example.investmenttracker.Activities.TechnicalsActivity;
import com.example.investmenttracker.Activities.TipsActivity;
import com.example.investmenttracker.MainActivity;
import com.example.investmenttracker.R;

public class ExploreFragment extends Fragment {

    private View exploreView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        exploreView = inflater.inflate(R.layout.fragment_explore, container, false);

        CreateButtonListeners();

        return exploreView;
    }

    private void CreateButtonListeners() {

        CardView newsCard = exploreView.findViewById(R.id.newsCard);
        CardView tipsCard = exploreView.findViewById(R.id.tipsCard);
        CardView strategiesCard = exploreView.findViewById(R.id.strategiesCard);
        CardView technicalsCard = exploreView.findViewById(R.id.technicalsCard);
        CardView searchCard = exploreView.findViewById(R.id.searchCard);

        newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewsActivity.class));
            }
        });

        tipsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TipsActivity.class));
            }
        });

        strategiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StrategiesActivity.class));
            }
        });

        technicalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TechnicalsActivity.class));
            }
        });

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });


    }


}
