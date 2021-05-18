package com.example.investmenttracker.Database.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.R;

import java.util.List;

@Dao // Dao - Data Access Object (provides an API for reading and writing data)
public interface CoinDao {

    //CRUD - Create, Read, Update, Delete
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Coin coin);

    @Query("DELETE FROM coin_table")
    void deleteAll();

    @Query("DELETE FROM coin_table WHERE id=:coinId")
    void deleteCoin(int coinId);

    @Query("SELECT * FROM coin_table ORDER BY name ASC")
    LiveData<List<Coin>> getAllCoins();

    @Query("SELECT * FROM coin_table WHERE favouriteImage=2131165310 GROUP BY name ORDER BY name ASC") // 700008 is value of R.drawable.heart_border_full
    LiveData<List<Coin>> getFavCoins();

    @Query("UPDATE coin_table SET favouriteImage=:favouriteImage WHERE name=:coinName")
    void setFavouriteState(String coinName, int favouriteImage);


}
