package com.company.damonday.CompanyInfo.Fragment.ViewComment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.Fragment.ViewCommentDetail.Fragment_ViewCommentDetail;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAM on 20/4/2015.
 */
public class Fragment_ViewComment extends Fragment {

    private View v;
    public  ListView commentListView;
    private TextView txtNoComment;
    private Fragment_ViewComment_CustomListAdapter customListAdapter;
    public  List<Fragment_ViewComment_Comment> commentList = new ArrayList<Fragment_ViewComment_Comment>();
    private String entId;
    private APIConfig ranking;
    private Boolean APILoading = false;
    private JsonObjectRequest jsonObjReq;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        try {
            //get id from previous page
            entId = getArguments().getString("ent_id");
            Log.d("ent_id", entId);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "No data loaded", Toast.LENGTH_LONG).show();
        }
        //new object for Api url
        ranking = new APIConfig(entId);

        //call api
        //APICall apiCall = new APICall(entId);
        //apiCall.customLoadMoreDataFromApi(1);
        customLoadMoreDataFromApi(1);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        v = inflater.inflate(R.layout.view_companycomment, container, false);

        commentListView = (ListView) v.findViewById(R.id.list);
        txtNoComment = (TextView) v.findViewById(R.id.no_comment);
        customListAdapter = new Fragment_ViewComment_CustomListAdapter(getActivity(), commentList);
        commentListView.setAdapter(customListAdapter);
        //if no comment, show warning to user
        commentListView.setEmptyView(txtNoComment);
        //customListAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(commentListView);
        commentListView.deferNotifyDataSetChanged();
        customListAdapter.notifyDataSetChanged();

        /*// Attach the listener to the AdapterView onCreate
        commentListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });*/
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("comment_id", commentList.get(position).getId());
                Fragment_ViewCommentDetail fragment_ViewCommentDetail = new Fragment_ViewCommentDetail();
                fragment_ViewCommentDetail.setArguments(bundle);
                Log.d("getLayoutParams", Integer.toString(commentListView.getLayoutParams().height));

                /*getFragmentManager().beginTransaction()
                        .add(R.id.frame_container, fragment_ViewCommentDetail)
                        .commit();*/
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("companyDetail"));
                fragmentTransaction.add(R.id.frame_container, fragment_ViewCommentDetail, "commentDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });



        return v;
    }

    //doulbe scrollView
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        /*int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);*/
        //listView.requestLayout();

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        Log.d("offset", Integer.toString(offset));
        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ranking.getUrlComment().concat(Integer.toString(offset)), null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {


                try {
                    String status = response.getString("status");
                    if(status.equals("success")){
                        JSONArray data = response.getJSONArray("data");
                        txtNoComment.setVisibility(View.GONE);
                        commentListView.setVisibility(View.VISIBLE);
                        txtNoComment.setText(R.string.comment_no_comment);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject commentContent = data.getJSONObject(i);
                            Fragment_ViewComment_Comment comment = new Fragment_ViewComment_Comment();
                            comment.setProfilePic("http://www.suggymoto.com/Blog/wp-content/uploads/2013/03/Android-Icon-50x50.png");
                            comment.setTitle(commentContent.getString("title"));
                            comment.setPostedDate(commentContent.getString("time"));
                            comment.setUsername("Alan Lam");
                            comment.setRating(commentContent.getString("worth"));
                            comment.setId(commentContent.getString("ID"));
                            commentList.add(comment);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*commentList.add(comment1);
                commentList.add(comment2);
                commentList.add(comment3);
                commentList.add(comment4);
                commentList.add(comment5);
                */
                //hidepDialog();
                setListViewHeightBasedOnChildren(commentListView);
                commentListView.deferNotifyDataSetChanged();
                customListAdapter.notifyDataSetChanged();

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                //setListViewHeightBasedOnChildren(listView);
                //simpleAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        R.string.connection_server_warning, Toast.LENGTH_SHORT).show();
                //hidepDialog();
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
            Log.d("onStop", "Comment requests are all cancelled");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

