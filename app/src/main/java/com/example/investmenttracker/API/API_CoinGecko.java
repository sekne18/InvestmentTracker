package com.example.investmenttracker.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class API_CoinGecko {
    public Map<String, Map<String, BigDecimal>> Coins = new HashMap<String, Map<String, BigDecimal>>();
    public String last_updated;

    public void RefreshDataFromAPI() {
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
                Coins.clear();
                JSONArray coinsArray = new JSONArray(inline);
                for (int i = 0; i < coinsArray.length(); i++) {
                    JSONObject coin = coinsArray.getJSONObject(i);
                    Map<String, BigDecimal> details = new HashMap<String, BigDecimal>();
                    Map<String, String> nameAndDate = new HashMap<String, String>();
                    details.put("current_price", new BigDecimal(coin.getString("current_price")));
                    details.put("market_cap", new BigDecimal(coin.getString("market_cap")));
                    details.put("total_volume", new BigDecimal(coin.getString("total_volume")));
                    details.put("imageUrl", new BigDecimal(coin.getString("image")));
                    details.put("price_change_percentage_24h", new BigDecimal(coin.getString("price_change_percentage_24h")));
                    details.put("ath", new BigDecimal(coin.getString("ath")));
                    Coins.put(coin.getString("symbol"), details);
                    last_updated = coin.getString("last_updated");
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
