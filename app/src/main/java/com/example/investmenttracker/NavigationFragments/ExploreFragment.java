package com.example.investmenttracker.NavigationFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.investmenttracker.Activities.News.NewsActivity;
import com.example.investmenttracker.Activities.Search.SearchActivity;
import com.example.investmenttracker.Activities.Strategies.StrategiesActivity;
import com.example.investmenttracker.Activities.Technicals.TechnicalsActivity;
import com.example.investmenttracker.Activities.Tips.TipsActivity;
import com.example.investmenttracker.R;

public class ExploreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView newsCard = view.findViewById(R.id.newsCard);
        CardView tipsCard = view.findViewById(R.id.tipsCard);
        CardView strategiesCard = view.findViewById(R.id.strategiesCard);
        CardView technicalsCard = view.findViewById(R.id.technicalsCard);
        CardView searchCard = view.findViewById(R.id.searchCard);

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
