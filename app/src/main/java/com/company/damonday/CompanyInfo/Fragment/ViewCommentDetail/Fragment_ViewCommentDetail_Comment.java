package com.company.damonday.CompanyInfo.Fragment.ViewCommentDetail;

/**
 * Created by lamtaklung on 18/9/15.
 */
public class Fragment_ViewCommentDetail_Comment {
    String profilePic, title, date, username, content;
    double averageScore, funScore, serviceScore, environmentScore, equipmentScore, priceScore;

    public Fragment_ViewCommentDetail_Comment(String profilePic, String title, String date, String username, String content, double averageScore, double funScore, double serviceScore, double environmentScore, double equipmentScore, double priceScore) {
        this.profilePic = profilePic;
        this.title = title;
        this.date = date;
        this.username = username;
        this.content = content;
        this.averageScore = averageScore;
        this.funScore = funScore;
        this.serviceScore = serviceScore;
        this.environmentScore = environmentScore;
        this.equipmentScore = equipmentScore;
        this.priceScore = priceScore;
    }

    public Fragment_ViewCommentDetail_Comment() {

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getFunScore() {
        return funScore;
    }

    public void setFunScore(double funScore) {
        this.funScore = funScore;
    }

    public double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public double getEnvironmentScore() {
        return environmentScore;
    }

    public void setEnvironmentScore(double environmentScore) {
        this.environmentScore = environmentScore;
    }

    public double getEquipmentScore() {
        return equipmentScore;
    }

    public void setEquipmentScore(double equipmentScore) {
        this.equipmentScore = equipmentScore;
    }

    public double getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(double priceScore) {
        this.priceScore = priceScore;
    }
}

