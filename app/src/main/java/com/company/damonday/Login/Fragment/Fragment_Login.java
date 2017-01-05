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
import com.company.damonday.Home.Home;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Setting.Setting;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MainActivity;
import com.company.damonday.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lamtaklung on 31/5/15.
 */
public class Fragment_Login extends Fragment {
    private static final String TAG = Fragment_Login.class.getSimpleName();
    private Button btnLogin, btnCancel;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressImage pDialog;
    private SessionManager session;
    private LoginSQLiteHandler loginSQLiteHandler;
    private View v;
    private String entId, lastFragment;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    private StringRequest strReq, facebookStrReq;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);
        try {
            entId = getArguments().getString("ent_id");
            //get last fragment name
            lastFragment = getArguments().getString("lastFragment");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // SQLite database handler
        loginSQLiteHandler = new LoginSQLiteHandler(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.login);

        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.login_login_tab, container, false);
        inputUsername = (EditText) v.findViewById(R.id.username);
        inputPassword = (EditText) v.findViewById(R.id.password);
        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel);
        TextView txtRegister = (TextView) v.findViewById(R.id.register);
        TextView txtForgotPassword = (TextView) v.findViewById(R.id.forgot_password);
        //找到login button
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.btn_fb_login);
        loginButton.setFragment(this);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();


        // Progress dialog
        pDialog = new ProgressImage(getActivity());

        // Session manager
        session = new SessionManager(getActivity());


        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Forget_Password fragmentForgetPassword = new Fragment_Forget_Password();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //clear all of the fragment at the stack
                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragmentForgetPassword, "forgetPassword").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
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
                            R.string.login_warning_enter_info, Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                ((TestActivity) getActivity()).tempTitle.remove(((TestActivity) getActivity()).tempTitle.size() - 1);
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


        //get permission of facebook
        loginButton.setReadPermissions(Arrays.asList(
                "email", "user_birthday"));

        //幫loginButton增加callback function

        //這邊為了方便 直接寫成inner class
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //登入成功

            @Override
            public void onSuccess(LoginResult loginResult) {

                //get FB user profile
                Profile profile = Profile.getCurrentProfile();



                Log.d("FB", "access token got.");
                //accessToken之後或許還會用到 先存起來
                accessToken = loginResult.getAccessToken();

                Log.d("accessToken", accessToken.getToken());

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                String email = object.optString("email");
                                String fbID = object.optString("id");
                                String fbUsername = object.optString("name");

                                Log.d("FB", "complete");
                                Log.d("FB", fbUsername);
                                Log.d("FB", object.optString("link"));
                                Log.d("FB", fbID);
                                Log.d("FB", email);

                                //profile pic url
                                String fbProfilePic = "https://graph.facebook.com/" + fbID + "/picture?type=large";
                                Log.d("FB", fbProfilePic);
                                userFacebook(fbID, fbProfilePic, email, accessToken.getToken(), fbUsername);


                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email");
                request.setParameters(parameters);
                request.executeAsync();


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
    private void redirectToDetail() {
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
                ((TestActivity) getActivity()).tempTitle.remove(((TestActivity) getActivity()).tempTitle.size() - 1);
                fragmentManager.popBackStackImmediate();
                //delete last fragment
                ((TestActivity) getActivity()).tempTitle.remove(((TestActivity) getActivity()).tempTitle.size() - 1);
                fragmentManager.popBackStackImmediate();
                //hide the fragment which is to jump to company detail page
                String hideFragment = "";
                if (getActivity().getSupportFragmentManager().findFragmentByTag("home") != null)
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
            } else if (getActivity().getSupportFragmentManager().findFragmentByTag("setting") != null) {
                Setting setting = new Setting();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //clear all of the fragment at the stack
                ((TestActivity) getActivity()).hideBackButton();        //tomc 7/8/2016 actionbar button
                ((TestActivity) getActivity()).showMenuButton();        //tomc 7/8/2016 actionbar button
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, setting, "setting").addToBackStack(null);

                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            } else {
                Fragment fragment = null;
                fragment = new Home();
                getActivity().getActionBar().show();
                // This method will be executed once the timer is over
                // Start your app main activity
                                /*Intent i = new Intent(SplashActivity.this, TestActivity.class);
                                startActivity(i);*/
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //clear all of the fragment at the stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //System.out.println(fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment, "home").addToBackStack("main");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        showDialog();

        strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("status");

                    // Check for error node in json
                    if (error == 1) {
                        JSONObject data = jObj.getJSONObject("data");
                        String token = data.getString("auth_key");
                        String username = data.getString("username");
                        String email = data.getString("email");
                        // Create login session
                        session.setLogin(true);
                        //add user info to sqlite
                        Log.d("toke", token);
                        Log.d("username", username);
                        //add the user info into SQlite
                        loginSQLiteHandler.addUser(token, email, username);

                        try {
                            redirectToDetail();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (error == 0) {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_SHORT).show();
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
                        R.string.connection_server_warning, Toast.LENGTH_LONG).show();
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


    private void userFacebook(final String fbID, final String fbProfilePic,
                              final String email, final String fbToken, final String fbUsername) {
        // Tag used to cancel the request
        String tag_string_req = "req_facebook";

        //pDialog.setMessage("Registering ...");
        showDialog();

        facebookStrReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_FACEBOOK_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                Log.d("URL_FACEBOOK_USER", APIConfig.URL_FACEBOOK_USER.toString());
                Log.d("Response", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int status = jObj.getInt("status");
                    if (status == 1) {

                        //add facebook info into SQLite
                        loginSQLiteHandler.addUser(fbToken, email, fbUsername);
                        Log.d("facebook", "success");

                        redirectToDetail();

                    } else if (status == 0){

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("msg");

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
                        R.string.connection_server_warning, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("tag", "register");
                params.put("fbId", fbID);
                params.put("fbProfile", fbProfilePic);
                params.put("email", email);
                params.put("fbToken", fbToken);
                params.put("fbUsername", fbUsername);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(facebookStrReq, tag_string_req);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (strReq != null)  {
            strReq.cancel();
            Log.d("onStop", "System Login requests are all cancelled");
        }
        if(facebookStrReq != null){
            facebookStrReq.cancel();
            Log.d("onStop", "Facebook Login requests are all cancelled");
        }
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