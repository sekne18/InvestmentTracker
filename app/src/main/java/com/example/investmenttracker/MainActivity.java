package com.example.investmenttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.API.API_CurrencyExchange;
import com.example.investmenttracker.API.API_News;
import com.example.investmenttracker.Database.model.CoinViewModel;
import com.example.investmenttracker.NavigationFragments.ExploreFragment;
import com.example.investmenttracker.NavigationFragments.FavouriteFragment;
import com.example.investmenttracker.NavigationFragments.PortfolioFragment;
import com.example.investmenttracker.NavigationFragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static API_CoinGecko api_coin;
    public static API_News api_news;
    public static API_CurrencyExchange api_currencies;
    public static String EUR, USD;
    public static boolean canRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.nav_bar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Helper.coinViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(CoinViewModel.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new PortfolioFragment()).commit();
    }

    @Override
    protected void onResume() {
        if (!Helper.connected)
            Helper.openDialogForNetworkConnection(this);
        super.onResume();
    }

    @Override
    protected void onStart() {
        Helper.connected = Helper.CheckConnection(this);
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });

        if (Helper.connected) {
            api_coin = new API_CoinGecko();
            api_coin.RefreshDataFromAPI();
            api_news = new API_News();
            api_news.RefreshDataFromAPI();
            api_currencies = new API_CurrencyExchange();
        }
        super.onStart();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            String tag = "";
            switch (item.getItemId()) {
                case R.id.nav_port:
                    tag = "nav";
                    selectedFragment = new PortfolioFragment();
                    break;
                case R.id.nav_fav:
                    tag = "fav";
                    selectedFragment = new FavouriteFragment();
                    break;
                case R.id.nav_exp:
                    tag = "exp";
                    selectedFragment = new ExploreFragment();
                    break;
                case R.id.nav_set:
                    tag = "set";
                    selectedFragment = new SettingsFragment();
                    break;
            }
            addFragmentOnlyOnce(getSupportFragmentManager(),selectedFragment,tag);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, selectedFragment, tag).commit();
            return true;
        }
    };

    public static void addFragmentOnlyOnce(FragmentManager fragmentManager, Fragment fragment, String tag) {
        // Make sure the current transaction finishes first
        fragmentManager.executePendingTransactions();

        // If there is no fragment yet with this tag...
        if (fragmentManager.findFragmentByTag(tag) == null) {
            // Add it
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_container,fragment, tag).commit();
        }
    }


}