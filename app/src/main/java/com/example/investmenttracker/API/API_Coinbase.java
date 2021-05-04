package com.example.investmenttracker.API;

import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.example.investmenttracker.Database.model.CoinViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class API_Coinbase {

    public static float BTC_price = 0f;
    public static float ETH_price = 0f;
    public static float ADA_price = 0f;
    public static float DOT_price = 0f;

    public static void startToPullDataFromAPI() {
        DownloadTask getBTC = new DownloadTask();
        DownloadTask getETH = new DownloadTask();
        DownloadTask getADA = new DownloadTask();
        DownloadTask getDOT = new DownloadTask();

        try {
            getBTC.execute("https://api.coinbase.com/v2/exchange-rates?currency=BTC");
            getETH.execute("https://api.coinbase.com/v2/exchange-rates?currency=ETH");
            getADA.execute("https://api.coinbase.com/v2/exchange-rates?currency=ADA");
            getDOT.execute("https://api.coinbase.com/v2/exchange-rates?currency=DOT");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;

                    data = reader.read();
                }
                Log.i("URLContent", result);


                String[] coinName = result.split(":");
                String name = coinName[2].split(",")[0].replace("\"","");
                Log.i("URLContent", name);

                String[] valueUSD = result.split("USD");
                String value = valueUSD[1].split(":")[1].split(",")[0].replace("\"","");
                Log.i("URLContent", value);

                // TODO Vnos v bazo
                switch (name.toUpperCase()) {
                    case "BTC":
                        BTC_price = Float.parseFloat(value);
                        break;
                    case "ETH":
                        ETH_price = Float.parseFloat(value);
                        break;
                    case "ADA":
                        ADA_price = Float.parseFloat(value);
                        break;
                    case "DOT":
                        DOT_price = Float.parseFloat(value);
                        break;
                }

            } catch (IOException e) {
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
