package com.company.damonday.CompanyInfo.Fragment.ViewComment;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 23/7/15.
 */
public class Fragment_ViewComment_Comment {
    private String title, profilePic, postedDate, username, rating, id;


    public Fragment_ViewComment_Comment(){

    }

    public Fragment_ViewComment_Comment(String title, String profilePic, String postedDate, String username,
                 String rating, String id) {
        this.title = title;
        this.profilePic = profilePic;
        this.postedDate = postedDate;
        this.username = username;
        this.rating = rating;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
