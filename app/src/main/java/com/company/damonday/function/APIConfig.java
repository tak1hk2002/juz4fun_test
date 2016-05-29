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
    public static String URL_LOGIN = "http://52.32.220.11252.32.220.112/api/user/login/";

    // Server user register url
    public static String URL_REGISTER = "http://52.32.220.112/api/user/register/";

    //facebook user info url
    public static String URL_FACEBOOK_USER = "http://52.32.220.112/api/user/fb_login/";

    //Ranking url
    public static String URL_RANKING = "http://52.32.220.112/api/entertainment/rank/";

    //Home
    public static String URL_HOME = "http://52.32.220.112/api/entertainment/home";


    //Setting_about_us
    public static String URL_SETTING_ABOUT_US = "http://52.32.220.112/api/system/about_us";

    //Setting_feedback
    public static String URL_SETTING_feedback = "http://52.32.220.112/api/system/submit_opinion";

    //newfound
    public static String URL_newfound = "http://52.32.220.112/api/entertainment/new_entertainment";


    //advance search criteria
    public static String URL_Advance_Search_criteria="http://52.32.220.112/api/entertainment/search_criteria";

    //advance search
    public static String URL_Advance_Search="http://52.32.220.112/api/entertainment/advanced_search/";

    //fast search
    public static String URL_Fast_Search="http://52.32.220.112/api/entertainment/search/";

    //submit comment
    public static String URL_Write_Comment = "http://52.32.220.112/api/comment/submit_comment/";

    //latest comment
    public static String URL_Latest_Comment = "http://52.32.220.112/api/comment/latest_comments/?page=";

    //My comment


    //Detail url
    public String getUrlDetail(){
        return "http://52.32.220.112/api/entertainment/details/?ent_id="+id;
    }

    //ent comment
    public String getUrlComment(){
       return "http://52.32.220.112/api/comment/ent_comment/?ent_id="+id+"&page=";
    }


    public String getUrlCommentDetail(){
        return "http://52.32.220.112/api/comment/detail/?id="+id;
    }

}
