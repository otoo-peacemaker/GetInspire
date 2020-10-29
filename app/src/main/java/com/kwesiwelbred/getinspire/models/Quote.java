package com.kwesiwelbred.getinspire.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;


public class Quote implements Serializable {

    @Exclude
    public int id;

    public String title, desc, url;

    @Exclude
    public String category;

    @Exclude
    public boolean isFavourite = false;

    public Quote() {
    }

    public Quote(int id, String title, String desc, String url, String category) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
