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

    //Detail url
    public String getUrlDetail(){
        return "http://damonday.tk/api/entertainment/details/?ent_id="+id;
    }
}
