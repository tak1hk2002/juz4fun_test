package com.company.damonday.CompanyInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.Fragment_ViewComment;
import com.company.damonday.CompanyInfo.Fragment.ViewCompany.Fragment_ViewCompany;
import com.company.damonday.CompanyInfo.Fragment.ViewCompany.Fragment_ViewPhoto;
import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_ViewWriteComment;
import com.company.damonday.CompanyInfo.Lib.VideoControllerView;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.TabManager;
import com.viewpagerindicator.UnderlinePageIndicator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by lamtaklung on 4/8/15.
 */
public class FragmentTabs_try extends Fragment implements
        SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl{

    private FragmentTabHost mTabHost;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<String> listOfItems = new ArrayList<String>();;
    private String videoUrl;
    private String entId;
    private ProgressDialog pDialog;
    private static String TAG = FragmentTabs_try.class.getSimpleName();
    private ArrayList<String> coverPage = new ArrayList<String>();
    private View rootView;
    private TextView tvLike, tvDislike, tvFair, tvCompanyTitle;
    private String like, dislike, fair, companyName;
    private APIConfig details;
    private VideoControllerView controller;
    private MediaPlayer player;
    private SurfaceView videoSurface;
    private ImageView ivMyFavourite, ivLike, ivFair, ivDislike;
    private UnderlinePageIndicator mIndicator;
    private View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            entId = getArguments().getString("ent_id");
        }catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No data loaded", Toast.LENGTH_LONG).show();
        }
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        details = new APIConfig(entId);

        //intialise the Video player
        player = new MediaPlayer();
        controller = new VideoControllerView(getActivity());


        makeJsonArrayRequest();

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
        ivMyFavourite = (ImageView) rootView.findViewById(R.id.my_favourite);
        tvCompanyTitle = (TextView) rootView.findViewById(R.id.company_title);
        ivLike = (ImageView) rootView.findViewById(R.id.like_image);
        ivFair = (ImageView) rootView.findViewById(R.id.fair_image);
        ivDislike = (ImageView) rootView.findViewById(R.id.dislike_image);



        //viewPage
        viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);  //call Class to create Object
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d("PageSelected", Integer.toString(i));
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("PageSelected", Integer.toString(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        //it will pre-load all video and images.
        viewPager.setOffscreenPageLimit(10);


        // ViewPager Indicator
        mIndicator = (UnderlinePageIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setFades(false);
        mIndicator.setViewPager(viewPager);






        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabHost.addTab(
                mTabHost.newTabSpec("概要").setIndicator("概要"),
                Fragment_ViewCompany.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("玩評").setIndicator("玩評"),
                Fragment_ViewComment.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("我要評論").setIndicator("我要評論"),
                Fragment_ViewWriteComment.class, bundle);




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


                    String status = response.getString("status");
                    JSONObject companyInfo = response.getJSONObject("data");
                    if (status.equals("success")){

                        JSONObject score = companyInfo.getJSONObject("score");
                        JSONArray promotion_images = companyInfo.getJSONArray("promotion_images");
                        videoUrl = companyInfo.getString("video");
                        companyName = companyInfo.getString("name");
                        Log.d("videoUrl", videoUrl);

                        for (int i = 0; i < promotion_images.length(); i++){
                            coverPage.add((String) promotion_images.get(i));
                        }

                        like = score.getString("like");
                        dislike = score.getString("dislike");
                        fair = score.getString("fair");
                        initViews();


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
                myViewPagerAdapter.notifyDataSetChanged();
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
        tvCompanyTitle.setText(companyName);


        ivLike.setImageResource(R.drawable.like);
        ivFair.setImageResource(R.drawable.fair);
        ivDislike.setImageResource(R.drawable.dislike);
        ivMyFavourite.setImageResource(R.drawable.my_favourite);




        if(videoUrl != null)
            listOfItems.add(videoUrl);
        for(int i = 0; i < coverPage.size(); i++) {
            listOfItems.add(coverPage.get(i));
        }
        listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
        listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
        listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");

    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback
    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);

        //player.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;

    }

    @Override
    public void toggleFullScreen() {

    }
    // End VideoMediaController.MediaPlayerControl



    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<String> items;

        private NetworkImageView tView;
        private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public MyViewPagerAdapter(ArrayList<String> listOfItems) {
            this.items = listOfItems;
        }

        //display the pics
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //show the first page which is the video player
            if(position == 0 && videoUrl != null) {
                view = layoutInflater.inflate(R.layout.companyinfo_fragment_tab_video, container,false);
                controller.setAnchorView((FrameLayout) view.findViewById(R.id.videoSurfaceContainer));



                videoSurface = (SurfaceView) view.findViewById(R.id.videoSurface);
                SurfaceHolder videoHolder = videoSurface.getHolder();
                videoHolder.addCallback(FragmentTabs_try.this);
                try{
                    //set media player
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(getActivity(), Uri.parse(listOfItems.get(position)));
                    player.setOnPreparedListener(FragmentTabs_try.this);
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            controller.show();
                            Log.d("position", Integer.toString(position));
                        }

                        return false;
                    }
                });


            }
            //show company images except for first page
            else {

                view = layoutInflater.inflate(R.layout.companyinfo_fragment_tab_pager_view, container, false);
                if (imageLoader == null)
                    imageLoader = AppController.getInstance().getImageLoader();

                tView = (NetworkImageView) view
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
