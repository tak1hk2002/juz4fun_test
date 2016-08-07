package com.company.damonday.Search;

/**
 * Created by tom on 25/10/15.
 */
public class search_fast_model {
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

    private String title;
    private String user_id;

    public search_fast_model() {
    }


    public search_fast_model(String user_id, String title
    ) {
        this.title = title;
        this.user_id = user_id;

    }


}

