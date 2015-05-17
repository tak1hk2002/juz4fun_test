package com.company.damonday.function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lamtaklung on 3/5/15.
 */
public class parsonJson {

    String [] name;
    String [] id;

    public void parseJson(String json) {
        //由圖片可以知道，字串一開始是{}也就是物件，因此宣告一個JSON物件；
        //反之若一開始是[]陣列則宣告JSON陣列。
        JSONObject obj;
        try {
            obj = new JSONObject(json);
//宣告字串data來存放剛剛撈到的字串，剛剛的物件叫obj因此對他下obj.getString(“data”)，
//而裡面的data則是因為在上圖中最外層的物件裡包的JSON陣列叫做data。
            String data = obj.getString("data");
//這裡是宣告我們要繼續拆解的JSON陣列，所以要把剛剛撈到的字串轉換為JSON陣列
            JSONArray dataArray = new JSONArray(data);
//先宣告name跟id的字串陣列來存放等等要拆解的資料
            name = new String [dataArray.length()];
            id = new String [dataArray.length()];

//因為data陣列裡面有好多個JSON物件，因此利用for迴圈來將資料抓取出來
//因為不知道data陣列裡有多少物件，因此我們用.length()這個方法來取得物件的數量
            for (int i = 0; i < dataArray.length(); i++) {
                //接下來這兩行在做同一件事情，就是把剛剛JSON陣列裡的物件抓取出來
                //並取得裡面的字串資料
                //dataArray.getJSONObject(i)這段是在講抓取data陣列裡的第i個JSON物件
                //抓取到JSON物件之後再利用.getString(“欄位名稱”)來取得該項value
                //這裡的欄位名稱就是剛剛前面所提到的name:value的name
                name[i] = dataArray.getJSONObject(i).getString("name");
                id[i] = dataArray.getJSONObject(i).getString("id");
            }
        }
        catch (JSONException e) {e.printStackTrace();}


    }
    public String [] getName(){
        return name;
    }

    public String [] getId(){
        return id;
    }

}
