package com.example.investmenttracker.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Helper;

import java.math.BigDecimal;
import java.util.Map;

public class DataRefreshService extends Service implements API_CoinGecko.OnAsyncRequestComplete {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                () -> {
                    while(true) {
                        try {
                            Thread.sleep(300000); // 300000
                            Helper.getCoinsData(this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPostExecute(Map<String, Map<String, BigDecimal>> coins) {

    }
}

