package com.example.investmenttracker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import static com.example.investmenttracker.MainActivity.api_coin;
import static com.example.investmenttracker.MainActivity.api_news;
import static com.example.investmenttracker.MainActivity.canRefresh;

public class Helper {

    public static boolean connected;

    public static class InternetCheck extends AsyncTask<Void,Void,Boolean> {

        private Consumer mConsumer;
        public interface Consumer { void accept(Boolean internet); }

        public  InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

        @Override protected Boolean doInBackground(Void... voids) { try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; } }

        @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
        builder.setTitle("No internet. Please check your connection status!");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connected = CheckConnection(context);
                new Helper.InternetCheck(internet -> { connected = internet; });

                if (!connected) {
                    openDialogForNetworkConnection(context);
                }
                else if (canRefresh){
                    api_news.RefreshDataFromAPI();
                    api_coin.RefreshDataFromAPI();
                }
            }
        });
        builder.show();
    }

}