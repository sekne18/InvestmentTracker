package com.example.investmenttracker.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

    public class API_News extends AsyncTask<String, Void, Map<Integer, Map<String, String>>> {

        private final OnAsyncRequestComplete caller;
        public Map<Integer, Map<String, String>> News = new HashMap<Integer, Map<String, String>>();
        public String last_updated;
        public AsyncTask.Status currentStatus = AsyncTask.Status.RUNNING;

        public API_News(OnAsyncRequestComplete caller) {
            this.caller = caller;
        }

        @Override
        protected Map<Integer, Map<String, String>> doInBackground(String... strings) {
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
                    JSONObject newsJson = newsArray.getJSONObject(i);
                    Map<String, String> details = new HashMap<String, String>();
                    details.put("imageurl", newsJson.getString("imageurl"));
                    details.put("url", newsJson.getString("url"));
                    details.put("title", newsJson.getString("title"));
                    details.put("body", newsJson.getString("body")
                            .replace("&#8217;","'")
                            .replace("&#8220;","\"")
                            .replace("&#8220;","\"")
                            .replace("&#160;","")
                            .replace("[&#8216;]","'")
                            .replace("[&#8217;]","'")
                            .replace("[&#8230;]","..."));
                    details.put("source", newsJson.getString("source"));
                    News.put(i, details);
                }
                // Get current date
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                last_updated = format.format(date);
                currentStatus = Status.FINISHED;;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return News;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            caller.onPreExecute();
        }

        @Override
        protected void onPostExecute(Map<Integer, Map<String, String>> news) {
            super.onPostExecute(news);
            caller.onPostExecute(news);
        }

        public interface OnAsyncRequestComplete {
            void onPostExecute(Map<Integer, Map<String, String>> news);
            void onPreExecute();
        }
    }
//}
