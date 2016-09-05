package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment_Comment {

    private String title, info;
    private Boolean submitWarning;


    public Fragment_ViewWriteComment_Comment(String title, String info, Boolean submitWarning) {
        this.title = title;
        this.info = info;
        this.submitWarning = submitWarning;

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
}
