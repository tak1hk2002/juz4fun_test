package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment_Comment {

    private String title, info;
    private Integer openHour, openMin, endHour, endMin;
    private Boolean submitWarning;


    public Fragment_ViewWriteComment_Comment(String title, String info, Boolean submitWarning) {
        this.title = title;
        this.info = info;
        this.submitWarning = submitWarning;

    }


    public Fragment_ViewWriteComment_Comment(String title, String info, Boolean submitWarning, Integer openHour, Integer openMin, Integer endHour, Integer endMin){
        this.title = title;
        this.info = info;
        this.submitWarning = submitWarning;
        this.openHour = openHour;
        this.openMin = openMin;
        this.endHour = endHour;
        this.endMin = endMin;

    }

    public Fragment_ViewWriteComment_Comment() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getSubmitWarning() {
        return submitWarning;
    }

    public void setSubmitWarning(Boolean submitWarning) {
        this.submitWarning = submitWarning;
    }

    public Integer getOpenHour() {
        return openHour;
    }

    public void setOpenHour(Integer openHour) {
        this.openHour = openHour;
    }

    public Integer getOpenMin() {
        return openMin;
    }

    public void setOpenMin(Integer openMin) {
        this.openMin = openMin;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMin() {
        return endMin;
    }

    public void setEndMin(Integer endMin) {
        this.endMin = endMin;
    }
}
