package com.company.damonday.CompanyInfo.Fragment;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 23/7/15.
 */
public class Fragment_ViewComment_Comment {
    private String title, profilePic;
    private String postedDate;
    private String content;
    private String username;

    public Fragment_ViewComment_Comment() {
    }

    public Fragment_ViewComment_Comment(String title, String profilePic, String postedDate, String content,
                 String username) {
        this.title = title;
        this.profilePic = profilePic;
        this.postedDate = postedDate;
        this.content = content;
        this.username = username;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
