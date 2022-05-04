package com.example.investmenttracker.API;

import android.os.AsyncTask;
import android.widget.AdapterView;

import com.example.investmenttracker.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

    public class API_CurrencyExchange extends AsyncTask<String, Void, Map<String, Double>> {

        private final OnAsyncRequestComplete caller;
        public Map<String, Double> Currency = new HashMap<String, Double>();
        public AsyncTask.Status currentStatus = AsyncTask.Status.RUNNING;

        public API_CurrencyExchange(OnAsyncRequestComplete caller) {
            this.caller = caller;
        }

        @Override
        protected Map<String, Double> doInBackground(String... strings) {
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
            return Currency;
        }

        @Override
        protected void onPostExecute(Map<String, Double> currencies) {
            super.onPostExecute(currencies);
            caller.onPostExecute(currencies);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            caller.onPreExecute();
        }

        public interface OnAsyncRequestComplete {
            void onPostExecute(Map<String, Double> currencies);
            void onPreExecute();
        }

    }

