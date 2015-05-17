package com.company.damonday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.company.damonday.Ranking.Ranking;
import com.company.damonday.function.getJson;
import com.company.damonday.function.parsonJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity {
    String JsonText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);



        btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Ranking.class);
                startActivity(i);
            }
        });




        getJson abc = new getJson();
        abc.execute("http://www.damonday.tk/api/entertainment/get_entertainment_details/?ent_id=9");
        try {
            JsonText = abc.get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("alan1", JsonText);
        parseJson();

    }

    public void parseJson() {
        //由圖片可以知道，字串一開始是{}也就是物件，因此宣告一個JSON物件；
        //反之若一開始是[]陣列則宣告JSON陣列。
        JSONObject obj;
        try {
            obj = new JSONObject(JsonText);
            //宣告字串data來存放剛剛撈到的字串，剛剛的物件叫obj因此對他下obj.getString(“data”)，
            //而裡面的data則是因為在上圖中最外層的物件裡包的JSON陣列叫做data。
           // String data = obj.getString("data");
           // JSONArray data = obj.getJSONArray("data");
            JSONObject data = new JSONObject(obj.getString("data"));


            JSONObject rating = new JSONObject(data.getString("rating"));
            String average_score = rating.getString("average_score");
            String like = rating.getString("like");
            String fair = rating.getString("fair");
            String dislike = rating.getString("dislike");

            String title = data.getString("title");
            JSONArray promotion_images = data.getJSONArray("promotion_images");
            String d = data.getString("title");

           // String abc =obj.getString("result");
            //這裡是宣告我們要繼續拆解的JSON陣列，所以要把剛剛撈到的字串轉換為JSON陣列
           // JSONArray dataArray = new JSONArray(data);
            //先宣告name跟id的字串陣列來存放等等要拆解的資料
            //String [] rating = new String [dataArray.length()];
           // String [] title = new String [data.length()];
            /*String [] start_date = new String [dataArray.length()];
            String [] end_date = new String [dataArray.length()];
            String [] on_promotion = new String [dataArray.length()];
            String [] price = new String [dataArray.length()];
            String [] description = new String [dataArray.length()];
            String [] preferred_transport = new String [dataArray.length()];
            String [] address = new String [dataArray.length()];
            String [] business_hour = new String [dataArray.length()];
            String [] phone = new String [dataArray.length()];
            String [] video = new String [dataArray.length()];
            String [] promotion_images = new String [dataArray.length()];*/




            //因為data陣列裡面有好多個JSON物件，因此利用for迴圈來將資料抓取出來
            //因為不知道data陣列裡有多少物件，因此我們用.length()這個方法來取得物件的數量
           // for (int i = 0; i < data.length(); i++) {
                //接下來這兩行在做同一件事情，就是把剛剛JSON陣列裡的物件抓取出來
                //並取得裡面的字串資料
                //dataArray.getJSONObject(i)這段是在講抓取data陣列裡的第i個JSON物件
                //抓取到JSON物件之後再利用.getString(“欄位名稱”)來取得該項value
                //這裡的欄位名稱就是剛剛前面所提到的name:value的name
                //rating[i] = dataArray.getJSONObject(i).getString("rating");
              //  title[i] = data.getJSONObject(i).getString("title");
                /*start_date[i] = dataArray.getJSONObject(i).getString("start_date");
                end_date[i] = dataArray.getJSONObject(i).getString("end_date");
                on_promotion[i] = dataArray.getJSONObject(i).getString("on_promotion");
                price[i] = dataArray.getJSONObject(i).getString("price");
                description[i] = dataArray.getJSONObject(i).getString("description");
                preferred_transport[i] = dataArray.getJSONObject(i).getString("preferred_transport");
                address[i] = dataArray.getJSONObject(i).getString("address");
                business_hour[i] = dataArray.getJSONObject(i).getString("business_hour");
                phone[i] = dataArray.getJSONObject(i).getString("phone");
                video[i] = dataArray.getJSONObject(i).getString("video");
                promotion_images[i] = dataArray.getJSONObject(i).getString("promotion_images");*/
           // }

            Log.d("rating", promotion_images.getString(1));
        }
        catch (JSONException e) {e.printStackTrace();}




    }









}
