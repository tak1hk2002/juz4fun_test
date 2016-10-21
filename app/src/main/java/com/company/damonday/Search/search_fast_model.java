package com.company.damonday.Search;

/**
 * Created by tom on 25/10/15.
 */
public class search_fast_model {
    private String title;
    private String user_id;
    private String companyName;

    public search_fast_model() {
    }


    public search_fast_model(String user_id, String title, String companyName) {
        this.title = title;
        this.user_id = user_id;
        this.companyName = companyName;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

