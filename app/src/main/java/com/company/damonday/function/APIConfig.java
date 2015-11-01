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
    public static String URL_LOGIN = "http://damonday.tk/api/user/login/";

    // Server user register url
    public static String URL_REGISTER = "http://damonday.tk/api/user/register/";

    //facebook user info url
    public static String URL_FACEBOOK_USER = "http://damonday.tk/api/user/fb_login/";

    //Ranking url
    public static String URL_RANKING = "http://damonday.tk/api/entertainment/rank/";

    //Home
    public static String URL_HOME = "http://damonday.tk/api/entertainment/home/";


    //Setting_about_us
    public static String URL_SETTING_ABOUT_US = "http://damonday.tk/api/system/about_us";

    //Setting_feedback
    public static String URL_SETTING_feedback = "http://damonday.tk/api/system/submit_opinion";

    //newfound
    public static String URL_newfound = "http://damonday.tk/api/entertainment/new_entertainment";


    //advance search criteria
    public static String URL_Advance_Search_criteria="http://damonday.tk/api/entertainment/search_criteria";

    //advance search
    public static String URL_Advance_Search="http://damonday.tk/api/entertainment/advanced_search/";

    //fast search
    public static String URL_Fast_Search="http://damonday.tk/api/entertainment/search/";

    //Detail url
    public String getUrlDetail(){
        return "http://damonday.tk/api/entertainment/details/?ent_id="+id;
    }

    //ent comment
    public String getUrlComment(){
       return "http://damonday.tk/api/comment/ent_comment/?ent_id="+id+"&page=";
    }




    public String getUrlCommentDetail(){
        return "http://damonday.tk/api/comment/detail/?id="+id;
    }

}
