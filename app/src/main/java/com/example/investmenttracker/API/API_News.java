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

public class API_News {

    public Map<String, Map<String, BigDecimal>> News = new HashMap<String, Map<String, BigDecimal>>();
    public String last_updated;

    public void RefreshDataFromAPI() {
        API_News.DownloadTask getCoins = new API_News.DownloadTask();

        try {
            getCoins.execute("https://min-api.cryptocompare.com/data/v2/news/?lang=EN");
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
                News.clear();
                JSONObject dataArray = new JSONObject(inline);
                JSONArray newsArray = dataArray.getJSONArray("Data");

                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject news = newsArray.getJSONObject(i);
                    String tmp = news.getString("imageurl");
                    Log.i("TMP", news.getString("imageurl"));

//                    Map<String, BigDecimal> details = new HashMap<String, BigDecimal>();
//                    Map<String, String> nameAndDate = new HashMap<String, String>();
//                    details.put("current_price", new BigDecimal(coin.getString("current_price")));
//                    details.put("market_cap", new BigDecimal(coin.getString("market_cap")));
//                    details.put("total_volume", new BigDecimal(coin.getString("total_volume")));
//                    details.put("price_change_percentage_24h", new BigDecimal(coin.getString("price_change_percentage_24h")));
//                    details.put("ath", new BigDecimal(coin.getString("ath")));
//                    News.put(coin.getString("symbol"), details);
//                    last_updated = coin.getString("last_updated");
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
