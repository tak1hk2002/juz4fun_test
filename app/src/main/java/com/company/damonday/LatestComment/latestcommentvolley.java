package com.company.damonday.LatestComment;

/**
 * Created by tom on 14/6/15.
 */


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.CompanyInfo.Fragment.ViewCommentDetail.Fragment_ViewCommentDetail;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.AppController;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class latestcommentvolley extends Fragment {
    // Log tag
    private static final String TAG = latestcommentvolley.class.getSimpleName();
    private ProgressImage pDialog;
    private List<latestcomment> latestcommentList = new ArrayList<latestcomment>();
    private ListView listView;
    private latestcomment_Adapter adapter;
    private String api, lastFragmentTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            api = getArguments().getString("api");
            lastFragmentTag = getArguments().getString("last_fragment_tag");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No API loaded", Toast.LENGTH_LONG).show();
        }

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latestcomment, container, false);
        //setContentView(R.layout.latestcomment);
        getActivity().setTitle(R.string.latestcomment);
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new latestcomment_Adapter(getActivity(), latestcommentList);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Log.d("page", Integer.toString(page));
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("comment_id", latestcommentList.get(position).getId());
                Fragment_ViewCommentDetail fragment_ViewCommentDetail = new Fragment_ViewCommentDetail();
                fragment_ViewCommentDetail.setArguments(bundle);
                /*getFragmentManager().beginTransaction()
                        .add(R.id.frame_container, fragment_ViewCommentDetail)
                        .commit();*/
                ((TestActivity) getActivity()).showBackButton();        //tomc 7/8/2016 actionbar button
                ((TestActivity) getActivity()).hideMenuButton();        //tomc 7/8/2016 actionbar button
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag(lastFragmentTag));
                fragmentTransaction.add(R.id.frame_container, fragment_ViewCommentDetail, "commentDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        });

        pDialog = new ProgressImage(getActivity());
        // Showing progress dialog before making http request
        pDialog.show();
        return view;
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        // Creating volley request obj
        Log.d("Link", api + offset);
        StringRequest latestcommentReq = new StringRequest(api + Integer.toString(offset),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        hidePDialog();

                        try {
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
                                    latestcomment.setId(oneObject.getString("ID"));
                                    latestcomment.setTitle(oneObject.getString("title"));
                                    latestcomment.setProfilePic(oneObject.getString("profile_picture"));
                                    latestcomment.setRating(oneObject.getString("average_score"));
                                    latestcomment.setEntName("Bubble Soccer");
                                    latestcomment.setCompanyName("Party Home");
                                    latestcomment.setUsername(oneObject.getString("username"));
                                    latestcomment.setPostedDate(oneObject.getString("days_before"));
                                    //latestcomment.setComment(oneObject.getString("comment"));
                                    // adding movie to movies array
                                    latestcommentList.add(latestcomment);

                                    // notifying list adapter about data changes
                                    // so that it renders the list view with updated data
                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            Log.e("log_tag", "Error parsing data " + e.toString());
                        }

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