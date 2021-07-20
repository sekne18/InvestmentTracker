package com.example.investmenttracker.Database.model;

public class News {
    private String url;
    private String imageurl;
    private String title;
    private String body;

    public News(String title, String body, String imageurl, String url) {
        this.url = url;
        this.body = body;
        this.title = title;
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageurl;
    }



}
