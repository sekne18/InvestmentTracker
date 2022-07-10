package com.example.investmenttracker;

import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Database.model.CoinViewModel;

public class Helper {

    public static boolean connected = false;
    public static String currency = "$";
    public static CoinViewModel coinViewModel;
    public static ArrayList<Coin> mCoinsList = new ArrayList<>();
    public static SharedPreferences sharedPrefs;
    public static UiModeManager uiModeManager;
    public static boolean returnToSettings;
    public static API_CoinGecko api_coin;
    public static boolean canRefresh = true;

    public static class InternetCheck extends AsyncTask<Void,Void,Boolean> {

        private Consumer mConsumer;
        public interface Consumer { void accept(Boolean internet); }

        public InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

        @Override protected Boolean doInBackground(Void... voids) { try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; } }

        @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
    }

    public static void getCoinsData(API_CoinGecko.OnAsyncRequestComplete caller) {
        api_coin = new API_CoinGecko(caller);
        try {
            switch (Helper.currency) {
                case "$":
                    api_coin.execute("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false");
                    api_coin.currentStatus = AsyncTask.Status.RUNNING;
                    break;
                case "€":
                    api_coin.execute("https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&per_page=100&page=1&sparkline=false");
                    api_coin.currentStatus = AsyncTask.Status.RUNNING;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFragmentOnlyOnce(FragmentManager fragmentManager, Fragment fragment, String tag) {
        fragmentManager.executePendingTransactions();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_container,fragment, tag).commit();
        }
    }

    public static boolean CheckConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    public static void openDialogForNetworkConnection(Context context) {
        Helper.connected = Helper.CheckConnection(context);
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
        builder.setTitle("No internet. Please check your connection status!");

        builder.setCancelable(false).setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!connected) {
                    openDialogForNetworkConnection(context);
                }
                else if (canRefresh){
                    Helper.getCoinsData((API_CoinGecko.OnAsyncRequestComplete) context);
                }
            }
        });
        builder.show();
    }
}
