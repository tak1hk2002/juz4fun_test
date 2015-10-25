package com.company.damonday.MyFavourites;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 25/10/2015.
 */
public class MyFavouritesObject {
    String entId, picUrl, title, price, like, fair, dislike, averageScore;
    ArrayList<String> cat = new ArrayList<>();

    public MyFavouritesObject(String entId, String picUrl, String title, String price, String like, String fair, String dislike, String averageScore, ArrayList<String> cat) {
        this.entId = entId;
        this.picUrl = picUrl;
        this.title = title;
        this.price = price;
        this.like = like;
        this.fair = fair;
        this.dislike = dislike;
        this.averageScore = averageScore;
        this.cat = cat;
    }

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getFair() {
        return fair;
    }

    public void setFair(String fair) {
        this.fair = fair;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }

    public ArrayList<String> getCat() {
        return cat;
    }

    public void setCat(ArrayList<String> cat) {
        this.cat = cat;
    }
}
