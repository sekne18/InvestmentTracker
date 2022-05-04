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

public class API_CoinGecko extends AsyncTask<String, Void, Map<String, Map<String, BigDecimal>>> {
    private final API_CoinGecko.OnAsyncRequestComplete caller;
    public Map<String, Map<String, BigDecimal>> Coins = new HashMap<String, Map<String, BigDecimal>>();
    public Map<String, String> coin_Images = new HashMap<String, String>();
    public String last_updated;
    public AsyncTask.Status currentStatus = AsyncTask.Status.PENDING;

    public API_CoinGecko(API_CoinGecko.OnAsyncRequestComplete caller) {
        this.caller = caller;
    }

    @Override
    protected Map<String, Map<String, BigDecimal>> doInBackground(String... strings) {
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
                details.put("current_price", new BigDecimal(coin.getString("current_price")));details.put("market_cap", new BigDecimal(coin.getString("market_cap")));
                details.put("total_volume", new BigDecimal(coin.getString("total_volume")));
                details.put("price_change_percentage_24h", new BigDecimal(coin.getString("price_change_percentage_24h")));
                details.put("ath", new BigDecimal(coin.getString("ath")));
                Coins.put(coin.getString("symbol"), details);
                coin_Images.put(coin.getString("symbol"), coin.getString("image"));
                last_updated = coin.getString("last_updated");
            }
            currentStatus = Status.FINISHED;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return Coins;
    }

    @Override
    protected void onPostExecute(Map<String, Map<String, BigDecimal>> coins) {
        super.onPostExecute(coins);
        caller.onPostExecute(coins);
    }

    public interface OnAsyncRequestComplete {
        void onPostExecute(Map<String, Map<String, BigDecimal>> coins);
    }
}
