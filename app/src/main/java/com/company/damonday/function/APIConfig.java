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
    public static String URL_LOGIN = "http://54.187.62.227/api/user/login/";

    // Server user register url
    public static String URL_REGISTER = "http://54.187.62.227/api/user/register/";

    //facebook user info url
    public static String URL_FACEBOOK_USER = "http://54.187.62.227/api/user/fb_login/";

    //Ranking url
    public static String URL_RANKING = "http://54.187.62.227/api/entertainment/rank/";

    //Home
    public static String URL_HOME = "http://54.187.62.227/api/entertainment/home";


    //Setting_about_us
    public static String URL_SETTING_ABOUT_US = "http://54.187.62.227/api/system/about_us";

    //Setting_feedback
    public static String URL_SETTING_feedback = "http://54.187.62.227/api/system/submit_opinion";

    //newfound
    public static String URL_newfound = "http://54.187.62.227/api/entertainment/new_entertainment";


    //advance search criteria
    public static String URL_Advance_Search_criteria="http://54.187.62.227/api/entertainment/search_criteria";

    //advance search
    public static String URL_Advance_Search="http://54.187.62.227/api/entertainment/advanced_search/";

    //fast search
    public static String URL_Fast_Search="http://54.187.62.227/api/entertainment/search/";

    //submit comment
    public static String URL_Write_Comment = "http://54.187.62.227/api/comment/submit_comment/";

    //latest comment

    public static String URL_Latest_Comment = "http://54.187.62.227/api/comment/latest_comments/?page=";


    //Detail url
    public String getUrlDetail(){
        return "http://54.187.62.227/api/entertainment/details/?ent_id="+id;
    }

    //ent comment
    public String getUrlComment(){
       return "http://54.187.62.227/api/comment/ent_comment/?ent_id="+id+"&page=";
    }


    public String getUrlCommentDetail(){
        return "http://54.187.62.227/api/comment/detail/?id="+id;
    }

}
