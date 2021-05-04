package com.example.investmenttracker.Database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.investmenttracker.R;

@Entity(tableName = "coin_table")
public class Coin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private Float price_curr;
    private Float owned;
    private int favouriteImage;
    private int coinImage;
//    private Float price_1h;
//    private Float price_4h;
//    private Float price_1d;

    public Coin(@NonNull int coinImage, String name, Float price_curr, Float owned, int favouriteImage) {  // , Float price_1h, Float price_4h, Float price_1d
        this.coinImage = coinImage;
        this.name = name;
        this.price_curr = price_curr;
        this.owned = owned;
        this.favouriteImage = favouriteImage; // 1 is true, 0 is false
//        this.price_1h = price_1h;
//        this.price_4h = price_4h;
//        this.price_1d = price_1d;
    }

    public int getCoinImage() {
        return coinImage;
    }

    public void setCoinImage(int coinImage) {
        this.coinImage = coinImage;
    }

    public int getFavouriteImage() {
        return favouriteImage;
    }

    public void setFavouriteImage(int favouriteImage) {
        this.favouriteImage = favouriteImage;
    }

    public Float getOwned() {
        return owned;
    }

    public void setOwned(Float owned) {
        this.owned = owned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice_curr() {
        return price_curr;
    }

    public void setPrice_curr(Float price_curr) {
        this.price_curr = price_curr;
    }

    public String getName() {
        return name;
    }

//    public Float getPrice_1h() {
//        return price_1h;
//    }
//
//    public void setPrice_1h(Float price_1h) {
//        this.price_1h = price_1h;
//    }
//
//    public Float getPrice_4h() {
//        return price_4h;
//    }
//
//    public void setPrice_4h(Float price_4h) {
//        this.price_4h = price_4h;
//    }
//
//    public Float getPrice_1d() {
//        return price_1d;
//    }
//
//    public void setPrice_1d(Float price_1d) {
//        this.price_1d = price_1d;
//    }

}
