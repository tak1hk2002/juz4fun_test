package com.company.damonday.Ranking;

/**
 * Created by lamtaklung on 27/6/15.
 */

public class CompanyInfo {
    private String title, url;
    private int ent_id, moscotId;


    public CompanyInfo() {
    }



    public CompanyInfo(String name, String url, int ent_id, int moscotId) {
        this.title = name;
        this.url = url;
        this.ent_id = ent_id;
        this.moscotId = moscotId;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public int getMoscotId() {
        return moscotId;
    }

    public void setMoscotId(int moscotId) {
        this.moscotId = moscotId;
    }


}
