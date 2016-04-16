package com.company.damonday.LatestComment;

/**
 * Created by tom on 14/6/15.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.R;
import com.company.damonday.LatestComment.latestcomment_Adapter;
import com.company.damonday.function.AppController;
import com.company.damonday.LatestComment.latestcomment;
import com.company.damonday.function.APIConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class latestcommentvolley extends Fragment {
    // Log tag
    private static final String TAG = latestcommentvolley.class.getSimpleName();

    // Movies json url
    //private static final String url = "http://damonday.tk/api/comment/latest_comments/?page=1";
    private ProgressDialog pDialog;
    private List<latestcomment> latestcommentList = new ArrayList<latestcomment>();
    private ListView listView;
    private latestcomment_Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latestcomment, container, false);
        //setContentView(R.layout.latestcomment);

        listView = (ListView)view.findViewById(R.id.list);
        adapter = new latestcomment_Adapter(getActivity(), latestcommentList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
    /*    getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));*/

        // Creating volley request obj
        StringRequest latestcommentReq = new StringRequest( APIConfig.URL_Latest_Comment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        hidePDialog();

                        try{
                            JSONObject jObject = new JSONObject(response);
                            // String aJsonString = jObject.getString("result");
                            //String bJsonString = jObject.getString("timestamp");
                            JSONArray jArray = jObject.getJSONArray("data");
                            // Parsing json
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    //JSONObject jObject = new JSONObject(response);
                                    // String aJsonString = jObject.getString("result");
                                    //String bJsonString = jObject.getString("timestamp");
                                    // JSONArray jArray = jObject.getJSONArray("data");

                                    // JSONObject obj = response.getJSONObject(i);
                                    latestcomment latestcomment = new latestcomment();
                                    latestcomment.setTitle(oneObject.getString("title"));
                                    latestcomment.setThumbnailUrl(oneObject.getString("profile_picture"));
                                    latestcomment.setAveage_scrore(oneObject.getString("average_score"));

                                    Log.d("1112", oneObject.getString("days_before"));
                                    latestcomment.setDay_before(oneObject.getString("days_before"));
                                    latestcomment.setComment(oneObject.getString("comment"));
                                    Log.d("1113", oneObject.getString("comment"));


                                    // adding movie to movies array
                                    latestcommentList.add(latestcomment);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        catch (JSONException e) {
                            Log.e("log_tag", "Error parsing data " + e.toString());
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(latestcommentReq);




        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//       // getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

}