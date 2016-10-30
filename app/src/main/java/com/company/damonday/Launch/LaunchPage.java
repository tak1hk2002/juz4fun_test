package com.company.damonday.Launch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.Home.Home;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by lamtaklung on 23/6/2016.
 */
public class LaunchPage extends Fragment {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    public static final String PREFS_NAME = "MyPrefsFile";
    private JsonObjectRequest jsonObjReq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeJsonArrayRequest();
        //User has successfully logged in, save this information
        // We need an Editor object to make preference changes.
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode

        boolean hasDisplayedLaunchLogin = settings.getBoolean("hasDisplayedLaunchLogin", false);

        if(hasDisplayedLaunchLogin){
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
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
                        // fragmentTransaction.replace(R.id.frame_container, fragment, "launchsignup").addToBackStack("main");
                        fragmentTransaction.replace(R.id.frame_container, fragment, "home").addToBackStack("main");
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                    }

                }
            }, SPLASH_TIME_OUT);
        }
        else {

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    Fragment fragment = null;
                    fragment = new LaunchSignUp();
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
                        // fragmentTransaction.replace(R.id.frame_container, fragment, "launchsignup").addToBackStack("main");
                        fragmentTransaction.replace(R.id.frame_container, fragment, "launchsignup");
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                    }

                }
            }, SPLASH_TIME_OUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.launch_page, container, false);
        return view;
    }

    //從server 拿資料
    private void makeJsonArrayRequest() {


        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_Advance_Search_criteria, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("JSON", response.toString());

                try {

                    // loop through each json object
                    int status = response.getInt("status");



                    if (status == 1) {
                        System.out.print("success");
                        JSONObject criteria = response.getJSONObject("data");

                        JSONArray category = criteria.getJSONArray("category");
                        JSONObject price = criteria.getJSONObject("price_range");
                        JSONArray district = criteria.getJSONArray("district");
                        //JSONArray lagre_distinct = criteria.getJSONArray("category");

                        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = settings.edit();

                        //Set "hasLoggedIn" to true
                        editor.putString("category", category.toString());
                        editor.putString("price", price.toString());
                        editor.putString("district", district.toString());

                        // Commit the edits!
                        editor.commit();



                    } else if(status ==0) {
                        String errorMsg = response.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }




                } catch (JSONException e) {
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("json", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        R.string.connection_server_warning, Toast.LENGTH_SHORT).show();
                //hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (jsonObjReq != null)  {
            jsonObjReq.cancel();
            Log.d("onStop", "Search criteria requests are all cancelled");
        }
    }



}
