package com.example.investmenttracker.NavigationFragments;

import android.app.UiModeManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.investmenttracker.Activities.News.NewsActivity;
import com.example.investmenttracker.Activities.Search.SearchActivity;
import com.example.investmenttracker.Activities.Strategies.StrategiesActivity;
import com.example.investmenttracker.Activities.Technicals.TechnicalsActivity;
import com.example.investmenttracker.Activities.Tips.TipsActivity;
import com.example.investmenttracker.Helper;
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
        ImageView newsImg = view.findViewById(R.id.newsImage);
        ImageView tipsImg = view.findViewById(R.id.imageTips);
        ImageView technicalsImg = view.findViewById(R.id.imageTechnicals);
        ImageView strategiesImg = view.findViewById(R.id.strategieImage);
        ImageView searchImg = view.findViewById(R.id.imageSearch);

        if (Helper.uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            newsImg.setColorFilter(Color.WHITE);
            tipsImg.setColorFilter(Color.WHITE);
            technicalsImg.setColorFilter(Color.WHITE);
            strategiesImg.setColorFilter(Color.WHITE);
            searchImg.setColorFilter(Color.WHITE);
        } else {
            newsImg.setColorFilter(Color.BLACK);
            tipsImg.setColorFilter(Color.BLACK);
            technicalsImg.setColorFilter(Color.BLACK);
            strategiesImg.setColorFilter(Color.BLACK);
            searchImg.setColorFilter(Color.BLACK);
        }

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
