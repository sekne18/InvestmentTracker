package com.example.investmenttracker.Activities.Strategies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;
import com.example.investmenttracker.StrategieFragments.HODLFragment;
import com.example.investmenttracker.StrategieFragments.RuleFragment;

public class StrategiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategies);
        backbutton();
        CreateButtonListeners();
    }

    private void CreateButtonListeners() {
        CardView hodlCard = findViewById(R.id.hodlCard);
        CardView ruleCard = findViewById(R.id.ruleCard);
        FrameLayout container = findViewById(R.id.fragment_container3);


        hodlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container3, new HODLFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        ruleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container3, new RuleFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });
    }

    private void backbutton() {
        ImageButton backButton = findViewById(R.id.backButton4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}