package com.company.damonday.Setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lamtaklung on 13/10/2016.
 */

public class MyComment extends Fragment {

    private static final String TAG = MyComment.class.getSimpleName();
    private ProgressImage pDialog;
    private ListView listView;
    private TextView noComment;
    private CommentList_CustomListAdapter adapter;
    private List<CommentList> commentListList = new ArrayList<CommentList>();
    private StringRequest myCommentReq;
    private SessionManager session;
    private LoginSQLiteHandler DB;
    private String token = null, lastFragmentTag;
    private APIConfig APIConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            lastFragmentTag = getArguments().getString("last_fragment_tag");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //get token
        session = new SessionManager(getActivity());
        if (AccessToken.getCurrentAccessToken() != null) {
            token = AccessToken.getCurrentAccessToken().toString();
        } else if (session.isLoggedIn()) {
            DB = new LoginSQLiteHandler(getActivity());
            HashMap<String, String> user = DB.getUserDetails();
            token = user.get("token");
        }
        System.out.println("Token: "+token);

        APIConfig = new APIConfig(token);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latestcomment, container, false);
        //setContentView(R.layout.latestcomment);
        getActivity().setTitle(R.string.my_comment);
        listView = (ListView) view.findViewById(R.id.list);
        noComment = (TextView) view.findViewById(R.id.no_comment);
        noComment.setText(R.string.comment_no_comment);

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
        Log.d("Link", APIConfig.getUrlMyComment() + offset);
        myCommentReq = new StringRequest(APIConfig.getUrlMyComment() + Integer.toString(offset),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        hidePDialog();

                        try {
                            JSONObject jObject = new JSONObject(response);
                            int status = jObject.getInt("status");
                            if(status == 1) {
                                JSONArray jArray = jObject.getJSONArray("data");

                                if(jArray.length() > 0) {
                                    noComment.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
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
                                            CommentList.setProfilePic("");
                                            CommentList.setRating("0");
                                            CommentList.setEntName("Bubble Soccer");
                                            CommentList.setCompanyName("ABC");
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
                                else{
                                    noComment.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
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
                Log.d(TAG, "Error: " + error.getMessage());
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
        AppController.getInstance().addToRequestQueue(myCommentReq);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (myCommentReq != null)  {
            myCommentReq.cancel();
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
}
