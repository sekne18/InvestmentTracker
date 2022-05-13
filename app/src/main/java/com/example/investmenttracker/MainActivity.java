package com.example.investmenttracker;

import static com.example.investmenttracker.Helper.openDialogForNetworkConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Database.model.CoinViewModel;
import com.example.investmenttracker.NavigationFragments.ExploreFragment;
import com.example.investmenttracker.NavigationFragments.FavouriteFragment;
import com.example.investmenttracker.NavigationFragments.PortfolioFragment;
import com.example.investmenttracker.NavigationFragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements API_CoinGecko.OnAsyncRequestComplete {

    public static BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        Helper.sharedPrefs = getSharedPreferences("InvestmentTracker", 0);
        Helper.connected = Helper.CheckConnection(this);
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        if (!Helper.connected) {
            openDialogForNetworkConnection(this);
        } else {
            loadData();
        }
    }

    private void loadData() {
        Helper.getCoinsData(this);
        Helper.currency = Helper.sharedPrefs.getString("currency", "$");
        //Sets theme
        if (Helper.sharedPrefs.getBoolean("nightMode", false)) {
            Helper.uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        } else {
            Helper.uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        Helper.connected = Helper.CheckConnection(this);
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        if (!Helper.connected) {
            openDialogForNetworkConnection(this);
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
            Helper.addFragmentOnlyOnce(getSupportFragmentManager(),selectedFragment,tag);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, selectedFragment, tag).commit();
            return true;
        }
    };

    @Override
    public void onPostExecute(Map<String, Map<String, BigDecimal>> coins) {
        //After coins are gathered, create objects
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.nav_bar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Helper.coinViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(CoinViewModel.class);
        if (Helper.returnToSettings) {
            Helper.returnToSettings = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new SettingsFragment()).commit();
            bottomNav.getMenu().getItem(3).setChecked(true);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new PortfolioFragment()).commit();
        }
    }


}