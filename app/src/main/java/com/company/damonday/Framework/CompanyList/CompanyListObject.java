package com.company.damonday.Framework.CompanyList;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 25/10/2015.
 */
public class CompanyListObject {
    String entId;
    String picUrl;
    String title;
    String companyName;
    String price;
    String like;
    String fair;
    String dislike;
    String averageScore;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;
    ArrayList<String> cat = new ArrayList<>();

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyListObject() {
    }

    public CompanyListObject(String entId, String picUrl, String title, String companyName, String price, String like, String fair, String dislike, String averageScore, String cat) {
        this.entId = entId;
        this.picUrl = picUrl;
        this.title = title;
        this.companyName = companyName;
        this.price = price;
        this.like = like;
        this.fair = fair;
        this.dislike = dislike;
        this.averageScore = averageScore;

        this.category = cat;
    }

}
