package com.company.damonday.LatestComment;

/**
 * Created by tom on 14/6/15.
 */
import java.util.ArrayList;

public class latestcomment {
    private String title, profilePic, postedDate, username, rating, id, entName, companyName;


    public latestcomment() {
    }


    public latestcomment(String title, String profilePic, String postedDate, String username, String rating, String id, String entName, String companyName) {
        this.title = title;
        this.profilePic = profilePic;
        this.postedDate = postedDate;
        this.entName = entName;
        this.companyName = companyName;
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

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
