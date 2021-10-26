package com.example.investmenttracker.Database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "coin_table")
public class Coin {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageUrl;
    private String name;
    private Float price_curr;
    private Float owned;
    private String currency;
    private byte isFavourite;


    public Coin(String imageUrl, String name, Float price_curr, Float owned, String currency, byte isFavourite) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price_curr = price_curr;
        this.owned = owned;
        this.currency = currency;
        this.isFavourite = isFavourite;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCurrency() { return currency; }

    public void setCurrency(String curr) { this.currency = curr; }

    public String getImageUrl() {
        return imageUrl;
    }

    public byte isFavourite() {
        return isFavourite;
    }

    public void isFavourite(byte favouriteImage) {
        this.isFavourite = favouriteImage;
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


}
