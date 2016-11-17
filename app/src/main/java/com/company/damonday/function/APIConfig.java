package com.company.damonday.function;

/**
 * Created by lamtaklung on 31/5/15.
 */
public class APIConfig {

    public String id;
    public APIConfig(String id) {
        this.id = id;

    }

    // Server user login url
    public static String URL_LOGIN = "http://52.32.220.112/juz4fun/api/user/login/";

    // Server user register url
    public static String URL_REGISTER = "http://52.32.220.112/juz4fun/api/user/register/";

    //facebook user info url
    public static String URL_FACEBOOK_USER = "http://52.32.220.112/juz4fun/api/user/fb-login/";

    //Ranking url
    public static String URL_RANKING = "http://52.32.220.112/juz4fun/api/entertainment/rank/";

    //Home
    public static String URL_HOME = "http://52.32.220.112/juz4fun/api/entertainment/home/";


    //Setting_about_us
    public static String URL_SETTING_ABOUT_US = "http://52.32.220.112/juz4fun/api/system/about-us/";

    //Setting_feedback
    public static String URL_SETTING_feedback = "http://52.32.220.112/juz4fun/api/system/submit-opinion/";

    //newfound
    public static String URL_newfound = "http://52.32.220.112/juz4fun/api/entertainment/new/";


    //advance search criteria
    public static String URL_Advance_Search_criteria="http://52.32.220.112/juz4fun/api/entertainment/search-criteria";

    //advance search
    public static String URL_Advance_Search="http://52.32.220.112/juz4fun/api/entertainment/advanced-search/";


    //submit comment
    public static String URL_Write_Comment = "http://52.32.220.112/juz4fun/api/comment/submit/";

    //latest comment
    public static String URL_Latest_Comment = "http://52.32.220.112/juz4fun/api/comment/latest/?page=";

    //My comment
    public String getUrlMyComment(){
        return "http://52.32.220.112/juz4fun/api/comment/my-list/?token="+id+"&page=";
    }

    //Detail url
    public String getUrlDetail(){
        return "http://52.32.220.112/juz4fun/api/entertainment/details/?id="+id;
    }

    //ent comment
    public String getUrlComment(){
       return "http://52.32.220.112/juz4fun/api/comment/ent-comment/?ent_id="+id+"&page=";
    }

    //Comment detail
    public String getUrlCommentDetail(){
        return "http://52.32.220.112/juz4fun/api/comment/details/?id="+id;
    }

    //Fast search
    public String getUrlFastSearch(){
        return "http://52.32.220.112/juz4fun/api/entertainment/search/?keyword="+id+"&page=";
    }

}
