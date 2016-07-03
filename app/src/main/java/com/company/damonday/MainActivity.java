package com.company.damonday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import com.company.damonday.Home.Home;
import com.company.damonday.Login.FragmentTabs;
import com.company.damonday.function.APIConfig;
import com.company.damonday.Ranking.Ranking;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
//import com.felipecsl.gifimageview.library.GifImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends FragmentActivity {
    String JsonText = null;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    private TextView info;
    private ImageView profileImgView, progress;
    private Button btn;
    private Button btn_map;
    private Button btn_login;
    private Button btn_testScorllView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn_map = (Button) findViewById(R.id.testmap);
        btn_login = (Button) findViewById(R.id.login);
        info = (TextView) findViewById(R.id.info);
        profileImgView = (ImageView) findViewById(R.id.profile_img);
        btn_testScorllView = (Button)findViewById(R.id.button2);
        progress = (ImageView)findViewById(R.id.imageView);
        //GifImageView gifImageView = (GifImageView) findViewById(R.id.imageView);

        final AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        final AlertDialog[] dialog = new AlertDialog[1];
        final EditText txtTitle = new EditText(this);


        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();

        //找到login button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        Log.v("context", getApplicationContext().toString());


        btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
               // Intent i = new Intent(MainActivity.this, Ranking.class);
                /*Intent i = new Intent(MainActivity.this, Home.class);  //tomc 13/10/2015 start
                startActivity(i);*/
                ab.setTitle(R.string.writeComment_dialog_title);

                ab.setView(txtTitle);

                ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        //Editable title = txtTitle.getText();
                        //OR
                    }
                });

                ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                //when alertview is launched, the keyboard show immediately
                dialog[0] = ab.create();
                dialog[0].getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                dialog[0].show();
                
            }
        });

        btn_map.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 啟動地圖元件用的Intent物件
                Intent intentMap = new Intent(MainActivity.this, MapsActivity.class);
                // 啟動地圖元件
                startActivityForResult(intentMap, 2);
            }
        });

        btn_login.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 啟動地圖元件用的Intent物件
                Intent i = new Intent(MainActivity.this, FragmentTabs.class);
                startActivity(i);
            }
        });

        btn_testScorllView.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 啟動地圖元件用的Intent物件
                Intent i = new Intent(MainActivity.this, TestActivity.class);
                startActivity(i);
            }
        });

        //幫loginButton增加callback function

        //這邊為了方便 直接寫成inner class

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //登入成功

            @Override
            public void onSuccess(LoginResult loginResult) {

                //get FB user profile
                Profile profile = Profile.getCurrentProfile();
                info.setText(message(profile));


                //get user ID and display his profile pic
                String userId = loginResult.getAccessToken().getUserId();
                String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";

                Glide.with(MainActivity.this)
                        .load(profileImgUrl)
                        .into(profileImgView);







                Log.d("FB", "access token got.");


                //accessToken之後或許還會用到 先存起來
                accessToken = loginResult.getAccessToken();

                Log.d("accessToken", accessToken.getToken());
                System.out.println(profile.getName());

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                Log.d("FB", "complete");
                                Log.d("FB", object.optString("name"));
                                Log.d("FB", object.optString("link"));
                                Log.d("FB", object.optString("id"));

                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();


                //profile pic url
                String ImgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";


                userFacebook(accessToken.getToken(), profile.getName(), accessToken.getUserId(), ImgUrl);


            }

            //登入取消

            @Override
            public void onCancel() {
                // App code

                Log.d("FB", "CANCEL");
            }

            //登入失敗

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.d("FB", exception.toString());
            }
        });



//        getJson abc = new getJson();
//        abc.execute("");
//        try {
//            JsonText = abc.get().toString();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        Log.d("alan1", JsonText);
        //parseJson();

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


    @Override
    protected void onResume() {
        super.onResume();



        // Logs 'install' and 'app activate' App Events.
        Profile profile = Profile.getCurrentProfile();
        info.setText(message(profile));
        try {
            String profileImgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";

            Glide.with(MainActivity.this)
                    .load(profileImgUrl)
                    .into(profileImgView);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String message(Profile profile) {
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("Welcome ").append(profile.getName());
        }
        return stringBuffer.toString();
    }

    private void userFacebook(final String accessToken, final String username,
                              final String id, final String profilePic) {
        // Tag used to cancel the request
        String tag_string_req = "req_facebook";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_FACEBOOK_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.equals("success")) {

                        Toast.makeText(MainActivity.this,
                                error, Toast.LENGTH_LONG).show();
                        Log.d("facebook", "success");

                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table
                        //db.addUser(email, username);
                        //Log.d("email", email);


                        //display message login successfully
                        /*AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                        ab.setTitle(R.string.register_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent in = new Intent(v.getContext(), FragmentTabs.class);
                                v.getContext().startActivity(in);
                            }
                        });
                        ab.create().show();*/

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        JSONObject data = jObj.getJSONObject("data");
                        String errorMsg = data.getString("msg");

                        Toast.makeText(MainActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(MainActivity.this,
                        R.string.connection_server_warning, Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("tag", "register");
                params.put("fb_id", id);
                params.put("fb_token", accessToken);
                params.put("fb_username", username);
                params.put("fb_profile_picture", profilePic);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }






}
