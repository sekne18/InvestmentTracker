package com.example.investmenttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.NavigationFragments.ExploreFragment;
import com.example.investmenttracker.NavigationFragments.FavouriteFragment;
import com.example.investmenttracker.NavigationFragments.PortfolioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static API_CoinGecko api;
    private boolean connected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.nav_bar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new PortfolioFragment()).commit();
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(this));
        builder.setTitle("No internet. Please check your connection status!");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connected = CheckConnection();
                if (!connected) {
                    openDialog();
                }
                else {
                    api = new API_CoinGecko();
                    api.RefreshDataFromAPI();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        if (!connected)
            openDialog();
        super.onResume();
    }

    private boolean CheckConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onStart() {
        connected = CheckConnection();

        if (connected) {
            api = new API_CoinGecko();
            api.RefreshDataFromAPI();
        }
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