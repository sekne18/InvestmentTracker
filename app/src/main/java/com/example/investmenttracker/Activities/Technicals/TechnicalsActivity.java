package com.example.investmenttracker.Activities.Technicals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.AnalysisFragments.ChartLinesFragment;
import com.example.investmenttracker.AnalysisFragments.FundamentalsFragment;
import com.example.investmenttracker.AnalysisFragments.IndicatorsFragment;
import com.example.investmenttracker.AnalysisFragments.PatternsFragment;
import com.example.investmenttracker.R;
import com.example.investmenttracker.StrategieFragments.HODLFragment;
import com.example.investmenttracker.StrategieFragments.RuleFragment;

public class TechnicalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        backbutton();
        CreateButtonListeners();
    }

    private void CreateButtonListeners() {
        CardView fundCard = findViewById(R.id.fundCard);
        CardView indiCard = findViewById(R.id.indicatorsCard);
        CardView hodlCard = findViewById(R.id.chartCard);
        CardView ruleCard = findViewById(R.id.patternsCard);
        FrameLayout container = findViewById(R.id.fragment_container2);


        fundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new FundamentalsFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        indiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new IndicatorsFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        hodlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new ChartLinesFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        ruleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new PatternsFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });
    }

    private void backbutton() {
        ImageButton backButton = findViewById(R.id.backButton5);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}