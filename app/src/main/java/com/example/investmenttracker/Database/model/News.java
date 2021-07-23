package com.example.investmenttracker.Database.model;

public class News {
    private String url;
    private String imageurl;
    private String title;
    private String body;
    private String source;

    public News(String title, String body, String imageurl, String url, String source) {
        this.url = url;
        this.body = body;
        this.title = title;
        this.imageurl = imageurl;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public String getImageUrl() {
        return imageurl;
    }



}
