package com.example.investmenttracker.Activities.Tips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.investmenttracker.R;
import com.example.investmenttracker.TipsFragments.BuyFragment;
import com.example.investmenttracker.TipsFragments.DollarCostAvgFragment;
import com.example.investmenttracker.TipsFragments.NoiseFragment;
import com.example.investmenttracker.TipsFragments.Spot_LeverageFragment;
import com.example.investmenttracker.TipsFragments.FomoFragment;
import com.example.investmenttracker.TipsFragments.WalletFragment;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        backbutton();
        CreateButtonListeners();
    }

    private void CreateButtonListeners() {
        CardView spot_levCard = findViewById(R.id.spot_levCard);
        CardView fomoCard = findViewById(R.id.fomoCard);
        CardView walletCard = findViewById(R.id.walletCard);
        CardView noiseCard = findViewById(R.id.noiseCard);
        CardView costAvgCard = findViewById(R.id.costAvgCard);
        CardView buyCard = findViewById(R.id.buyCard);
        FrameLayout container = findViewById(R.id.fragment_container);


        spot_levCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Spot_LeverageFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        fomoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FomoFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        walletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        noiseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoiseFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        costAvgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DollarCostAvgFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

        buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BuyFragment()).commit();
                container.setVisibility(View.VISIBLE);
            }
        });

    }

    private void backbutton() {
        ImageButton backButton = findViewById(R.id.backButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}