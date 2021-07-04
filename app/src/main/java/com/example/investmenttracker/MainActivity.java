package com.example.investmenttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.NavigationFragments.ExploreFragment;
import com.example.investmenttracker.NavigationFragments.FavouriteFragment;
import com.example.investmenttracker.NavigationFragments.PortfolioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static API_CoinGecko api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.nav_bar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);



        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new PortfolioFragment()).commit();
    }

    @Override
    protected void onStart() {
        api = new API_CoinGecko();
        api.RefreshDataFromAPI();
        super.onStart();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_port:
                    selectedFragment = new PortfolioFragment();
                    break;
                case R.id.nav_fav:
                    selectedFragment = new FavouriteFragment();
                    break;
                case R.id.nav_exp:
                    selectedFragment = new ExploreFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, selectedFragment).commit();

            return true;
        }
    };


}