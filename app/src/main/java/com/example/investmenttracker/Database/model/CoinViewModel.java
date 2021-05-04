package com.example.investmenttracker.Database.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.investmenttracker.Database.data.CoinRepository;

import java.util.List;

public class CoinViewModel extends AndroidViewModel {

    public static CoinRepository repository;
    public final LiveData<List<Coin>> allCoins, favCoins;

    public CoinViewModel(@NonNull Application application) {
        super(application);
        repository = new CoinRepository(application);
        allCoins = repository.getAllData();
        favCoins = repository.getFavCoinsData();
    }

    public LiveData<List<Coin>> getAllCoins() { return allCoins; }
    public static void insert(Coin coin) { repository.insert(coin); }
    public static void deleteCoin(int coinId) { repository.deleteCoin(coinId); }
    public static void favouriteImage(String coinName, int favourite) { repository.favouriteState(coinName, favourite); }
    public LiveData<List<Coin>> getFavouriteCoins() { return favCoins; }





}
