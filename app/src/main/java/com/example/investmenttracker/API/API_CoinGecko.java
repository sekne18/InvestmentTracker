package com.example.investmenttracker.API;

import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.example.investmenttracker.Database.model.CoinViewModel;
import com.fasterxml.jackson.core.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class API_CoinGecko {
    public Map<String, Map<String, Float>> Coins = new HashMap<String, Map<String, Float>>();

    public void startToPullDataFromAPI() {
        DownloadTask getCoins = new DownloadTask();

        try {
            getCoins.execute("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                Log.i("URLContent", inline);

                JSONArray coinsArray = new JSONArray(inline);
                for (int i = 0; i < coinsArray.length(); i++) {
                    JSONObject coin = coinsArray.getJSONObject(i);
                    Map<String, Float> details = new HashMap<String, Float>();
                    details.put("current_price", Float.parseFloat(coin.getString("current_price")));
                    details.put("market_cap", Float.parseFloat(coin.getString("market_cap")));
                    details.put("total_volume", Float.parseFloat(coin.getString("total_volume")));
                    details.put("price_change_percentage_24h", Float.parseFloat(coin.getString("price_change_percentage_24h")));
                    details.put("ath", Float.parseFloat(coin.getString("ath")));
                    Coins.put(coin.getString("symbol"), details);
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }


}
