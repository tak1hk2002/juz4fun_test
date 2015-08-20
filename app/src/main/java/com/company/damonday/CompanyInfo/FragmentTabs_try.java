package com.company.damonday.CompanyInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewComment;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewCompany;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewPhoto;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.TabManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 4/8/15.
 */
public class FragmentTabs_try extends Fragment {

    private FragmentTabHost mTabHost;
    private TabManager mTabManager;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<String> listOfItems;
    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private String urlJsonObj, videoUrl;
    private String entId;
    private ProgressDialog pDialog;
    private static String TAG = FragmentTabs_try.class.getSimpleName();
    private ArrayList<String> coverPage = new ArrayList<String>();
    private ArrayList<String> scoreInfo = new ArrayList<String>();
    private View rootView;
    private TextView tvLike, tvDislike, tvFair,  tvAverageScore;
    private String like, dislike, fair, averageScore;
    private APIConfig details;
    private MediaController mc;
    private VideoView videoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entId = getArguments().getString("ent_id");
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        details = new APIConfig(entId);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //pass entId to fragment
        Bundle bundle = new Bundle();
        bundle.putString("ent_Id", entId);




        rootView = inflater.inflate(R.layout.companyinfo_fragment_tab,container, false);
        tvLike = (TextView) rootView.findViewById(R.id.like);
        tvDislike = (TextView) rootView.findViewById(R.id.dislike);
        tvFair = (TextView) rootView.findViewById(R.id.fair);
        tvAverageScore = (TextView) rootView.findViewById(R.id.average_score);





        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabHost.addTab(
                mTabHost.newTabSpec("概要").setIndicator("概要"),
                Fragment_ViewCompany.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("玩評").setIndicator("玩評"),
                Fragment_ViewComment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("相片").setIndicator("相片"),
                Fragment_ViewPhoto.class, null);


        makeJsonArrayRequest();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void makeJsonArrayRequest() {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                details.getUrlDetail(), null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {


                    // loop through each json object
                    String status = response.getString("status");
                    JSONObject companyInfo = response.getJSONObject("data");
                    if (status.equals("success")){

                        JSONObject score = companyInfo.getJSONObject("score");
                        JSONArray promotion_images = companyInfo.getJSONArray("promotion_images");
                        videoUrl = companyInfo.getString("video");
                        Log.d("videoUrl", videoUrl);

                        for (int i = 0; i < promotion_images.length(); i++){
                            coverPage.add((String) promotion_images.get(i));
                        }

                        like = score.getString("like");
                        dislike = score.getString("dislike");
                        fair = score.getString("fair");
                        averageScore = score.getString("average_score");



                        initViews();
                        setViewPagerItemsWithAdapter();
                        setUiPageViewController();

                    }else{
                        String errorMsg = companyInfo.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                hidepDialog();


                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                //myViewPagerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    //initial of the pics
    private void initViews() {
        //set score of company
        tvLike.setText(like);
        tvDislike.setText(dislike);
        tvFair.setText(fair);
        tvAverageScore.setText(averageScore);



        listOfItems = new ArrayList<String>();
        if(videoUrl != null)
            listOfItems.add(videoUrl);
        for(int i = 0; i < coverPage.size(); i++) {
            listOfItems.add(coverPage.get(i));
        }
        listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
        //listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
        //listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");

    }



    private void setViewPagerItemsWithAdapter() {
        viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);  //call Class to create Object
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void setUiPageViewController() {
        dotsLayout = (LinearLayout)rootView.findViewById(R.id.viewPagerCountDots);
        dotsCount = myViewPagerAdapter.getCount();     //get number of pages(Pics) in ViewPager from the adapter
        Log.d("dotsCount", Integer.toString(dotsCount));
        dots = new TextView[dotsCount];     //Create a TextView array

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }

        dots[0].setTextColor(getResources().getColor(R.color.app_green));  //set the color of dots
    }


    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            Log.d("onSelected", Integer.toString(position));
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            dots[position].setTextColor(getResources().getColor(R.color.app_green));  //dots color
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<String> items;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public MyViewPagerAdapter(ArrayList<String> listOfItems) {
            this.items = listOfItems;
        }

        //display the pics
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view;
            Log.d("position", Integer.toString(position));
            if(position == 0 && videoUrl != null) {
                view = layoutInflater.inflate(R.layout.companyinfo_fragment_tab_video, container,false);
                videoView = (VideoView) view.findViewById(R.id.videoView);
                try{
                    //set media player
                    mc = new MediaController(getActivity());
                    videoView.setMediaController(mc);
                    videoView.setVideoURI(Uri.parse(listOfItems.get(position)));
                    videoView.seekTo(100);
                    Log.d("hi", "hi");
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                videoView.requestFocus();
                /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                        videoView.start();
                    }
                });*/
                //videoView.start();
            }
            else {

                videoView.stopPlayback();
                view = layoutInflater.inflate(R.layout.companyinfo_fragment_tab_pager_view, container, false);
                if (imageLoader == null)
                    imageLoader = AppController.getInstance().getImageLoader();

                NetworkImageView tView = (NetworkImageView) view
                        .findViewById(R.id.PageView);

                tView.setImageUrl(listOfItems.get(position), imageLoader);

            }




            container.addView(view);

            return view;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View)object;
            container.removeView(view);
        }
    }
}
