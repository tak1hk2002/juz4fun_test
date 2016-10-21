package com.company.damonday.Framework.ImageList;

/**
 * Created by lamtaklung on 27/6/15.
 */

public class ImageInfo {
    private String title, url, company;
    private int entID, moscotID;


    public ImageInfo() {
    }


    public ImageInfo(String name, String company, String url, int entID, int moscotID) {
        this.title = name;
        this.url = url;
        this.entID = entID;
        this.moscotID = moscotID;
        this.company = company;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getEntID() {
        return entID;
    }

    public void setEntID(int entID) {
        this.entID = entID;
    }

    public int getMoscotID() {
        return moscotID;
    }

    public void setMoscotID(int moscotID) {
        this.moscotID = moscotID;
    }


}
