package com.company.damonday.CompanyInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.CustomScrollView;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.Fragment_ViewComment;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.Fragment_ViewComment_Comment;
import com.company.damonday.CompanyInfo.Fragment.ViewCompany.Fragment_ViewCompany;
import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_ViewWriteComment;
import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_login_register;
import com.company.damonday.CompanyInfo.Lib.VideoControllerView;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MyFavourites.MyFavouritesObject;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
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
    private ArrayList<String> categories = new ArrayList<String>();
    private View rootView;
    private TextView tvLike, tvDislike, tvFair, tvCompanyTitle;
    private String like, dislike, fair, companyName, averageScore, cat, price;
    private APIConfig details;
    private VideoControllerView controller;
    private MediaPlayer player;
    private SurfaceView videoSurface;
    private ImageView ivMyFavourite, ivLike, ivFair, ivDislike;
    private UnderlinePageIndicator mIndicator;
    private View view;
    private CompanySQLiteHandler myfavouriteDB;
    private SessionManager session;
    private MyFavouritesObject myFavouritesObject;
    private boolean mEnableFlag=true;
    private int SCROLL_Y=0;
    private CustomScrollView scrollView;
    public int pageNum = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

/*        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }

        };

        accessTokenTracker.startTracking();

        updateWithToken(AccessToken.getCurrentAccessToken());*/

        // SqLite database handler
        //loginDB = new SQLiteHandler(activity);

        // session manager
        //session = new SessionManager(activity);

        /*accessToken = AccessToken.getCurrentAccessToken();
        Log.d("accessToken", accessToken.toString());*/
    }

    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);

        try {
            entId = getArguments().getString("ent_id");
        }catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No data loaded", Toast.LENGTH_LONG).show();
        }

        session = new SessionManager(getActivity());


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


        //initial view
        rootView = inflater.inflate(R.layout.companyinfo_fragment_tab,container, false);
        tvLike = (TextView) rootView.findViewById(R.id.like);
        tvDislike = (TextView) rootView.findViewById(R.id.dislike);
        tvFair = (TextView) rootView.findViewById(R.id.fair);
        ivMyFavourite = (ImageView) rootView.findViewById(R.id.my_favourite);
        tvCompanyTitle = (TextView) rootView.findViewById(R.id.company_title);
        ivLike = (ImageView) rootView.findViewById(R.id.like_image);
        ivFair = (ImageView) rootView.findViewById(R.id.fair_image);
        ivDislike = (ImageView) rootView.findViewById(R.id.dislike_image);
        viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        scrollView = (CustomScrollView) rootView.findViewById(R.id.parent_scroll);

        //get the scroll Y position
        scrollView.setScrollChangedListener(mScrollChangedListener);

        //config the view
        ivLike.setImageResource(R.drawable.like);
        ivFair.setImageResource(R.drawable.fair);
        ivDislike.setImageResource(R.drawable.dislike);

        myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);  //call Class to create Object
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);

        // SQLite database handler
        myfavouriteDB = new CompanySQLiteHandler(getActivity());

        //get row count
        int myFavouriteCount = myfavouriteDB.getRowCount(Integer.parseInt(entId));
        Log.d("myFavouriteCount", Integer.toString(myFavouriteCount));

        final boolean[] heartUnclicked = {true};
        if(myFavouriteCount > 0) {
            heartUnclicked[0] = false;
            ivMyFavourite.setImageResource(R.drawable.heart_clicked);
        }
        else {
            heartUnclicked[0] = true;
            ivMyFavourite.setImageResource(R.drawable.heart_unclicked);
        }

        ivMyFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heartUnclicked[0]){
                    heartUnclicked[0] = false;
                    ivMyFavourite.setImageResource(R.drawable.heart_clicked);
                    myfavouriteDB.addMyFavourite(myFavouritesObject);
                    Toast.makeText(getActivity(), R.string.heart_clicked, Toast.LENGTH_SHORT).show();

                }
                else{
                    heartUnclicked[0] = true;
                    ivMyFavourite.setImageResource(R.drawable.heart_unclicked);
                    myfavouriteDB.deleteMyFavourite(Integer.parseInt(entId));
                    Toast.makeText(getActivity(), R.string.heart_unclicked, Toast.LENGTH_SHORT).show();
                }

            }
        });



        //viewPage
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




        //pass entId to fragment
        Bundle bundle = new Bundle();
        bundle.putString("ent_id", entId);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabHost.addTab(
                mTabHost.newTabSpec("概要").setIndicator("概要"),
                Fragment_ViewCompany.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("玩評").setIndicator("玩評"),
                Fragment_ViewComment.class, bundle);

        //check login
        //no login, go to login or register option screen

        //no login record
        if (AccessToken.getCurrentAccessToken() != null || session.isLoggedIn()) {
            mTabHost.addTab(
                    mTabHost.newTabSpec("我要評論").setIndicator("我要評論"),
                    Fragment_ViewWriteComment.class, bundle);
        }else{ //detect the login record
            mTabHost.addTab(
                    mTabHost.newTabSpec("我要評論").setIndicator("我要評論"),
                    Fragment_login_register.class, bundle);
        }






        return rootView;
    }


    //Calculate the height of the ScrollView after combining outer and inner listView
    private CustomScrollView.ScrollChangedListener mScrollChangedListener = new CustomScrollView.ScrollChangedListener() {
        @Override
        public void onScrollChanged(int y) {
            int height=scrollView.getHeight();
            int scrollViewMeasuredHeight=scrollView.getChildAt(0).getMeasuredHeight();
            System.out.println(">>>>>>>>>>>> "+"scrollY="+y+",height="+height+",scrollViewMeasuredHeight="+scrollViewMeasuredHeight*0.9);
            SCROLL_Y=y;
            if((y+height)>=scrollViewMeasuredHeight*0.9) {
                if(mEnableFlag)
                {
                    //mEnableFlag=false;
                    // GO TO Load More!!!
                    Log.d("Load more", "Load more");
                    //Fragment_ViewComment.
                    //Fragment_ViewComment.setListViewHeightBasedOnChildren(Fragment_ViewComment.commentListView);
                    FragmentManager fragmentManager = getChildFragmentManager();
                    Fragment_ViewComment fragment_viewComment = (Fragment_ViewComment)fragmentManager.findFragmentByTag("玩評");
                    pageNum++;
                    fragment_viewComment.customLoadMoreDataFromApi(pageNum);

                }
            }
        }
    };



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        scrollView.smoothScrollTo(0, SCROLL_Y);
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
                        //videoUrl = companyInfo.getString("video");
                        videoUrl = "https://r3---sn-a8au-vgqe.googlevideo.com/videoplayback?initcwndbps=972500&source=youtube&sparams=dur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&pl=20&upn=aGLaFhwh8L8&mm=31&ipbits=0&fexp=9408937%2C9413138%2C9416126%2C9420452%2C9422141%2C9422596%2C9423662%2C9424753%2C9425945%2C9426382%2C9426482%2C9426624%2C9426674%2C9426722%2C9427126%2C9427292&mime=video%2Fmp4&requiressl=yes&ratebypass=yes&mv=m&mt=1453024336&ms=au&sver=3&dur=106.626&mn=sn-a8au-vgqe&signature=7E08703E4AB390EF876DF94FA89C3E4DC1F0B64C.767B3EA799C1D2160B83D0340AFE8D97056A64B4&key=yt6&itag=18&expire=1453046081&id=o-ABz2GeyAUt7Sey9KgBeFrwe41M1Jv_53uyJT1qU3q_mB&lmt=1393055536888759&ip=173.234.194.20&pcm2cms=yes&title=WWW.DOWNVIDS.NET-Hahaha...So%20Funny.mp4";
                        companyName = companyInfo.getString("name");
                        Log.d("videoUrl", videoUrl);

                        for (int i = 0; i < promotion_images.length(); i++){
                            coverPage.add((String) promotion_images.get(i));
                        }

                        like = score.getString("like");
                        dislike = score.getString("dislike");
                        fair = score.getString("fair");

                        //other info
                        JSONArray cat = companyInfo.getJSONArray("cat");
                        for (int i = 0; i < cat.length(); i++){
                            categories.add((String) cat.get(i));
                        }
                        price = companyInfo.getString("price");
                        averageScore = score.getString("average_score");



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


    //initial after getting jason
    private void initViews() {
        //set score of company
        tvLike.setText(like);
        tvDislike.setText(dislike);
        tvFair.setText(fair);
        tvCompanyTitle.setText(companyName);

        if(!companyName.equals(null));   //tomc 28/10/2015
        getActivity().getActionBar().setTitle(companyName);


        String coverPageUrl;
        if(!coverPage.isEmpty()) {
            coverPageUrl = coverPage.get(0);
        }else {
            coverPageUrl = "";
        }
        myFavouritesObject = new MyFavouritesObject(entId, coverPageUrl, companyName, price, like, fair, dislike, averageScore, categories);




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
