package com.example.investmenttracker.API;

import static com.example.investmenttracker.MainActivity.api_currencies;

import android.os.AsyncTask;
import android.util.Log;

import com.example.investmenttracker.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class API_CurrencyExchange {
    public Map<String, Double> Currency = new HashMap<String, Double>();
    public AsyncTask.Status currentStatus = AsyncTask.Status.RUNNING;

    public void RefreshDataFromAPI(String convertTo) {
        API_CurrencyExchange.DownloadTask getCurrencies = new API_CurrencyExchange.DownloadTask();

        try {
            if (convertTo.equals("€")) {
                getCurrencies.execute("https://api.exchangerate.host/convert?from=USD&to=EUR&amount=1");
                currentStatus = AsyncTask.Status.RUNNING;
            }
            else if (convertTo.equals("$")) {
                getCurrencies.execute("https://api.exchangerate.host/convert?from=EUR&to=USD&amount=1");
                currentStatus = AsyncTask.Status.RUNNING;
            }
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
                Currency.clear();
                JSONObject dataArray = new JSONObject(inline);
                Currency.put(Helper.currency, (Double) dataArray.get("result"));

                currentStatus = Status.FINISHED;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Helper.ConvertCoins();
        }
    }
}
