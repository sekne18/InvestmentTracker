package com.example.investmenttracker.Database.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Database.util.CoinRoomDatabase;

import java.util.List;

public class CoinRepository {

    private CoinDao coinDao;
    private LiveData<List<Coin>> allCoins, favCoins;

    public CoinRepository(Application application) {
        CoinRoomDatabase db = CoinRoomDatabase.getDatabase(application);
        coinDao = db.coinDao();
        allCoins = coinDao.getAllCoins();
        favCoins = coinDao.getFavCoins();
    }

    public LiveData<List<Coin>> getAllData() { return allCoins; }

    public LiveData<List<Coin>> getFavCoinsData() { return favCoins; }

    public void insert(Coin coin) {
        CoinRoomDatabase.databaseWriteExecutor.execute( () -> {
            coinDao.insert(coin);
        });
    }

    public void favouriteState(String coinName, byte favouriteImage) {
        CoinRoomDatabase.databaseWriteExecutor.execute( () -> {
            coinDao.setFavouriteState(coinName, favouriteImage);
        });
    }

    public void deleteCoin(int coinId) {
        CoinRoomDatabase.databaseWriteExecutor.execute( () -> {
            coinDao.deleteCoin(coinId);
        });
    }





}
