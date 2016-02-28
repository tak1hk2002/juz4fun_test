package com.company.damonday.Login.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Setting.Setting;
import com.company.damonday.function.APIConfig;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MainActivity;
import com.company.damonday.R;
import com.company.damonday.function.AppController;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lamtaklung on 31/5/15.
 */
public class Fragment_Login extends Fragment {
    private static final String TAG = Fragment_Registration.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private LoginSQLiteHandler loginSQLiteHandler;
    private View v;
    private String entId, lastFragment;
    CallbackManager callbackManager;
    private AccessToken accessToken;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);
        try {
            entId = getArguments().getString("ent_id");
            //get last fragment name
            lastFragment = getArguments().getString("lastFragment");
        }catch(Exception e) {
            e.printStackTrace();
        }

        // SQLite database handler
        loginSQLiteHandler = new LoginSQLiteHandler(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.login_login_tab, container, false);
        inputUsername = (EditText)v.findViewById(R.id.username);
        inputPassword = (EditText)v.findViewById(R.id.password);
        btnLogin = (Button) v.findViewById(R.id.btnLogin);
        TextView txtRegister = (TextView) v.findViewById(R.id.register);
        TextView txtForgotPassword = (TextView) v.findViewById(R.id.forgot_password);
        //找到login button
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();


        // Progress dialog
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());




        btnLogin.setOnClickListener(new AdapterView.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                // Check for empty data in the form
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(username, password);
                } else {
                    //Prompt user to enter credentials
                    Toast.makeText(getActivity(),
                            "請輸入使用者名稱和密碼", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Registration fragment_registration = new Fragment_Registration();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment_registration, "register").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

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


                //get user ID and display his profile pic
                String userId = loginResult.getAccessToken().getUserId();
                String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";



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





        return v;

    }

    //redirect to the page before login
    private void redirectToDetail(){
        try {
            //if the user login via Detail
            if (entId != null) {
                Bundle bundle = new Bundle();
                FragmentTabs_try fragmentTabs_try = new FragmentTabs_try();
                bundle.putString("ent_id", entId);
                fragmentTabs_try.setArguments(bundle);


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //delete last fragment
                fragmentManager.popBackStack();
                //delete last fragment
                fragmentManager.popBackStack();

                //hide the fragment which is to jump to company detail page
                String hideFragment = "";
                if(getActivity().getSupportFragmentManager().findFragmentByTag("home") != null)
                    hideFragment = "home";
                else if (getActivity().getSupportFragmentManager().findFragmentByTag("ranking") != null)
                    hideFragment = "ranking";
                else if (getActivity().getSupportFragmentManager().findFragmentByTag("search_result") != null)
                    hideFragment = "search_result";

                fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag(hideFragment));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);

                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            //if the user login through setting
            } else if (getActivity().getSupportFragmentManager().findFragmentByTag("setting") != null){
                Setting setting = new Setting();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //clear all of the fragment at the stack
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, setting, "setting").addToBackStack(null);

                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
            else {
                /*Intent in = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(in);*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("提交中 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.equals("success")) {
                        String token = data.getString("token");
                        String username = data.getString("username");
                        // Create login session
                        session.setLogin(true);
                        //add user info to sqlite
                        Log.d("toke", token);
                        Log.d("username", username);
                        loginSQLiteHandler.addUser(token, "Wait for Ryo to add email field", username);


                        //display message login successfully
                        AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                        ab.setTitle(R.string.login_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    //last fragment is from detail
                                    if (entId != null) {
                                        Bundle bundle = new Bundle();
                                        FragmentTabs_try fragmentTabs_try = new FragmentTabs_try();
                                        bundle.putString("ent_id", entId);
                                        fragmentTabs_try.setArguments(bundle);


                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        System.out.println(fragmentManager.getBackStackEntryCount());
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        //delete last fragment
                                        fragmentManager.popBackStack();
                                        //delete last fragment
                                        fragmentManager.popBackStack();
                                        //delete loginfragment
                                    /*Fragment loginFragment = getActivity().getSupportFragmentManager().findFragmentByTag("login");
                                    Fragment companyDetailFragment = getActivity().getSupportFragmentManager().findFragmentByTag("companyDetail");
                                    if(loginFragment != null)
                                        fragmentTransaction.remove(loginFragment);
                                    if(companyDetailFragment != null)
                                        fragmentTransaction.remove(companyDetailFragment);*/


                                        //hide the fragment which is to jump to company detail page
                                        String hideFragment = "";
                                        if(getActivity().getSupportFragmentManager().findFragmentByTag("home") != null)
                                            hideFragment = "home";
                                        else if (getActivity().getSupportFragmentManager().findFragmentByTag("ranking") != null)
                                            hideFragment = "ranking";
                                        else if (getActivity().getSupportFragmentManager().findFragmentByTag("search_result") != null)
                                            hideFragment = "search_result";

                                        fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag(hideFragment));
                                        fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);

                                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        fragmentTransaction.commit();


                                    }
                                    //last fragment is from setting
                                    else if (lastFragment != null){
                                        switch (lastFragment){
                                            case "setting":
                                                Setting setting = new Setting();
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                //clear all of the fragment at the stack
                                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.frame_container, setting, "setting").addToBackStack(null)
                                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                        .commit();

                                                break;
                                        }
                                    }
                                    else {
                                        Intent in = new Intent(v.getContext(), MainActivity.class);
                                        v.getContext().startActivity(in);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        ab.create().show();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = data.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }





    @Override
    public void onResume() {
        super.onResume();



        // Logs 'install' and 'app activate' App Events.
        /*Profile profile = Profile.getCurrentProfile();
        //info.setText(message(profile));
        try {
            String profileImgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";

            Glide.with(MainActivity.this)
                    .load(profileImgUrl)
                    .into(profileImgView);
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
        AppEventsLogger.activateApp(getActivity().getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(getActivity().getApplicationContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Log.d("URL_FACEBOOK_USER", APIConfig.URL_FACEBOOK_USER.toString());
                Log.d("Response", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.equals("success")) {
                        Toast.makeText(getActivity(),
                                error, Toast.LENGTH_LONG).show();
                        Log.d("facebook", "success");

                        redirectToDetail();

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

                        Toast.makeText(getActivity(),
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
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}