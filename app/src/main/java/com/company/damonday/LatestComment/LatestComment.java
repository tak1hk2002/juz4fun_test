package com.company.damonday.LatestComment;

/**
 * Created by tom on 14/6/15.
 */


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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.CompanyInfo.Fragment.ViewCommentDetail.Fragment_ViewCommentDetail;
import com.company.damonday.Framework.CommentList.CommentList;
import com.company.damonday.Framework.CommentList.CommentList_CustomListAdapter;
import com.company.damonday.Framework.CommentList.EndlessScrollListener;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LatestComment extends Fragment {
    // Log tag
    private static final String TAG = LatestComment.class.getSimpleName();
    private ProgressImage pDialog;
    private List<CommentList> commentListList = new ArrayList<CommentList>();
    private ListView listView;
    private CommentList_CustomListAdapter adapter;
    private String api, lastFragmentTag;
    private StringRequest latestcommentReq;

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
        adapter = new CommentList_CustomListAdapter(getActivity(), commentListList);
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
                bundle.putString("comment_id", commentListList.get(position).getId());
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
        latestcommentReq = new StringRequest(api + Integer.toString(offset),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        hidePDialog();

                        try {
                            JSONObject jObject = new JSONObject(response);
                            // String aJsonString = jObject.getString("result");
                            //String bJsonString = jObject.getString("timestamp");
                            int status = jObject.getInt("status");
                            if(status == 1) {
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
                                        CommentList CommentList = new CommentList();
                                        CommentList.setId(oneObject.getString("id"));
                                        CommentList.setTitle(oneObject.getString("title"));
                                        CommentList.setProfilePic(oneObject.getString("profile_picture"));
                                        CommentList.setRating(oneObject.getString("average_score"));
                                        CommentList.setEntName("Bubble Soccer");
                                        CommentList.setCompanyName(oneObject.getString("company_name"));
                                        CommentList.setUsername(oneObject.getString("username"));
                                        CommentList.setPostedDate(oneObject.getString("days_before"));
                                        //latestcomment.setComment(oneObject.getString("comment"));
                                        // adding movie to movies array
                                        commentListList.add(CommentList);

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        adapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            else if (status == 0){
                                String errorMsg = jObject.getString("msg");

                                Toast.makeText(getActivity(),
                                        errorMsg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Log.e("log_tag", "Error parsing data " + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = getResources().getString(R.string.connection_fail_warning);
                } else if (error instanceof ServerError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof AuthFailureError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof ParseError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof NoConnectionError) {
                    message = getResources().getString(R.string.connection_fail_warning);
                } else if (error instanceof TimeoutError) {
                    message = getResources().getString(R.string.connection_server_warning);
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(latestcommentReq);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (latestcommentReq != null)  {
            latestcommentReq.cancel();
            Log.d("onStop", "Latest comment requests are all cancelled");
        }
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