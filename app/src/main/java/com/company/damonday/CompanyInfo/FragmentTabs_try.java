package com.company.damonday.CompanyInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.company.damonday.CompanyInfo.CompanyImages.FullScreenViewActivity;
import com.company.damonday.CompanyInfo.CompanyVideo.VideoPlayerActivity;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.CustomScrollView;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.Fragment_ViewComment;
import com.company.damonday.CompanyInfo.Fragment.ViewCompany.Fragment_ViewCompany;
import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_ViewWriteComment;
import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_login_register;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MyFavourites.MyFavouritesObject;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.viewpagerindicator.CirclePageIndicator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by lamtaklung on 4/8/15.
 */
public class FragmentTabs_try extends Fragment{

    private FragmentTabHost mTabHost;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<String> listOfItems = new ArrayList<String>();;
    private ProgressImage pDialog;
    private static String TAG = FragmentTabs_try.class.getSimpleName();
    private ArrayList<String> coverPage = new ArrayList<String>();
    private TextView tvLike, tvDislike, tvFair, tvCompanyTitle;
    private String like, dislike, fair, entName, companyName,averageScore, categories, price, entId, videoUrl;
    private APIConfig details;
    private ImageView ivMyFavourite, imgLike, imgOk, imgDislike;
    private CompanySQLiteHandler myfavouriteDB;
    private SessionManager session;
    private MyFavouritesObject myFavouritesObject;
    private int SCROLL_Y=0;
    private CustomScrollView scrollView;
    private int pageNum = 1;
    private JsonObjectRequest jsonObjReq;


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


        pDialog = new ProgressImage(getActivity());
        pDialog.show();
        details = new APIConfig(entId);

        //intialise the Video player


        makeJsonArrayRequest();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initial view
        View rootView = inflater.inflate(R.layout.companyinfo_fragment_tab, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        tvLike = (TextView) rootView.findViewById(R.id.like);
        tvDislike = (TextView) rootView.findViewById(R.id.dislike);
        tvFair = (TextView) rootView.findViewById(R.id.ok);
        ivMyFavourite = (ImageView) rootView.findViewById(R.id.my_favourite);
        imgLike = (ImageView) rootView.findViewById(R.id.like_image);
        imgOk = (ImageView) rootView.findViewById(R.id.ok_image);
        imgDislike = (ImageView) rootView.findViewById(R.id.dislike_image);
        //tvCompanyTitle = (TextView) rootView.findViewById(R.id.company_title);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        scrollView = (CustomScrollView) rootView.findViewById(R.id.parent_scroll);

        //get the scroll Y position
        scrollView.setScrollChangedListener(mScrollChangedListener);

        myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);  //call Class to create Object
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);

        // SQLite database handler
        myfavouriteDB = new CompanySQLiteHandler(getActivity());

        //get row count
        int myFavouriteCount = myfavouriteDB.getRowCount(Integer.parseInt(entId));

        final boolean[] heartUnclicked = {true};


        if(myFavouriteCount > 0) {
            heartUnclicked[0] = false;
            ivMyFavourite.setImageResource(R.drawable.btn_favourite_selected);
        }
        else {
            heartUnclicked[0] = true;
            ivMyFavourite.setImageResource(R.drawable.btn_favourite_unselect);
        }

        ivMyFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heartUnclicked[0]){
                    heartUnclicked[0] = false;
                    ivMyFavourite.setImageResource(R.drawable.btn_favourite_selected);
                    myfavouriteDB.addMyFavourite(myFavouritesObject);
                    Toast.makeText(getActivity(), R.string.heart_clicked, Toast.LENGTH_SHORT).show();

                }
                else{
                    heartUnclicked[0] = true;
                    ivMyFavourite.setImageResource(R.drawable.btn_favourite_unselect);
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
        CirclePageIndicator mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);

        //pass entId to fragment
        Bundle bundle = new Bundle();
        bundle.putString("ent_id", entId);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabHost.addTab(
                mTabHost.newTabSpec("概要").setIndicator(createTabView(getActivity(), "概要")),
                Fragment_ViewCompany.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("玩評").setIndicator(createTabView(getActivity(), "玩評")),
                Fragment_ViewComment.class, bundle);


        //check login
        //no login, go to login or register option screen

        //no login record
        if (AccessToken.getCurrentAccessToken() != null || session.isLoggedIn()) {
            mTabHost.addTab(
                    mTabHost.newTabSpec("我要評論").setIndicator(createTabView(getActivity(), "我要評論")),
                    Fragment_ViewWriteComment.class, bundle);
        }else{ //detect the login record
            mTabHost.addTab(
                    mTabHost.newTabSpec("我要評論").setIndicator(createTabView(getActivity(), "我要評論")),
                    Fragment_login_register.class, bundle);
        }
        return rootView;
    }

    private static View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.companyinfo_frgment_tab_tabs, null, false);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
        tv.setText(tabText);
        return view;
    }
    boolean mEnableFlag = true;
    //Calculate the height of the ScrollView after combining outer and inner listView
    private CustomScrollView.ScrollChangedListener mScrollChangedListener = new CustomScrollView.ScrollChangedListener() {
        @Override
        public void onScrollChanged(int y) {
            Log.d("findFragemntByTag", Integer.toString(mTabHost.getCurrentTab()));

            int height=scrollView.getHeight();
            int scrollViewMeasuredHeight=scrollView.getChildAt(0).getMeasuredHeight();
            System.out.println(">>>>>>>>>>>> "+"scrollY="+y+",height="+height+",scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
            SCROLL_Y=y;
            if((y+height)>=scrollViewMeasuredHeight*0.9 && mTabHost.getCurrentTab() == 1) {

                if(mEnableFlag)
                {
                    mEnableFlag=false;
                    // GO TO Load More!!!
                    Log.d("Load more", "Load more");

                    FragmentManager fragmentManager = getChildFragmentManager();
                    Fragment_ViewComment fragment_viewComment = (Fragment_ViewComment)fragmentManager.findFragmentByTag("玩評");
                    pageNum++;
                    fragment_viewComment.customLoadMoreDataFromApi(pageNum);
                }
            }
            else{
                mEnableFlag = true;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment 1", "onActivityCreated");

    }

    @Override
    public void onResume() {
        super.onResume();
        scrollView.smoothScrollTo(0, SCROLL_Y);
        Log.d("Fragment 1", "onResume");
    }


    private void makeJsonArrayRequest() {
        showpDialog();

        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                details.getUrlDetail(), null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    int status = response.getInt("status");

                    if (status == 1){
                        JSONObject companyInfo = response.getJSONObject("data");
                        JSONObject score = companyInfo.getJSONObject("score");
                        JSONArray promotion_images = companyInfo.getJSONArray("promotion_images");
                        videoUrl = companyInfo.getString("video");
                        Log.d("VideoUrl", videoUrl);
                        //videoUrl = "http://www.sample-videos.com/video/mp4/360/big_buck_bunny_360p_2mb.mp4";
                        entName = companyInfo.getString("cht_name");
                        companyName = companyInfo.getString("company_name");

                        Log.d("videoUrl", videoUrl);

                        for (int i = 0; i < promotion_images.length(); i++){
                            coverPage.add((String) promotion_images.get(i));
                        }

                        like = score.getString("like");
                        dislike = score.getString("dislike");
                        fair = score.getString("fair");

                        //other info
                        JSONArray cat = companyInfo.getJSONArray("cat");
                        categories = "";
                        for (int i = 0; i < cat.length(); i++){
                            String catString = cat.getString(i);
                            if (categories != "")
                                categories += ", ";
                            categories += catString;
                        }
                        System.out.println("categories: "+ categories);
                        price = companyInfo.getString("price");
                        averageScore = score.getString("average_score").equals("null") ? getResources().getString(R.string.not_has_score_yet) : score.getString("average_score");


                        initViews();

                    }else if (status == 0){
                        String errorMsg = response.getString("msg");
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
                        R.string.connection_server_warning, Toast.LENGTH_SHORT).show();
                hidepDialog();
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
            Log.d("onStop", "Company Detail requests are all cancelled");
        }
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
        //tvCompanyTitle.setText(companyName);

        if(!entName.equals(null));   //tomc 28/10/2015
            getActivity().setTitle(entName);


        String coverPageUrl;
        if(!coverPage.isEmpty()) {
            coverPageUrl = coverPage.get(0);
        }else {
            coverPageUrl = "";
        }
        myFavouritesObject = new MyFavouritesObject(entId, coverPageUrl, entName, companyName, price, like, fair, dislike, averageScore, categories);

        if(videoUrl != null)
            listOfItems.add(videoUrl);
        for(int i = 0; i < coverPage.size(); i++) {
            listOfItems.add(coverPage.get(i));
        }
        //listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
        //listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
        //listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");

    }

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
            View view;
            if(position == 0 && videoUrl != null) {
                view = layoutInflater.inflate(R.layout.companyinfo_fragment_tab_video, container,false);
                ImageView MediaPreview = (ImageView) view.findViewById(R.id.MediaPreview);
                ImageView VideoPreviewPlayButton = (ImageView) view.findViewById(R.id.VideoPreviewPlayButton);

                MediaPreview.setImageResource(R.color.black);
                VideoPreviewPlayButton.setImageResource(R.drawable.btn_play_large);

                VideoPreviewPlayButton.setVisibility(view.VISIBLE);


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), VideoPlayerActivity.class);
                        i.putExtra("videoUrl", videoUrl);
                        startActivity(i);


                        /*//startActivity(new Intent(getActivity(), VideoPlayerActivity.class));
                        Bundle bundle = new Bundle();
                        bundle.putString("videoUrl", videoUrl);


                        VideoPlayer videoPlayerActivity = new VideoPlayer();
                        videoPlayerActivity.setArguments(bundle);

                        FragmentManager fragmentManager = getFragmentManager();
                        //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);   //important to clear repeat fragment in the stack //Alan 9/2/2016
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(getFragmentManager().findFragmentByTag("companyDetail"));
                        fragmentTransaction.add(R.id.frame_container, videoPlayerActivity, "video").addToBackStack("main");  //Alan 9/2/2016
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();*/

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

                tView.setDefaultImageResId(R.drawable.mascot_nopic);
                tView.setErrorImageResId(R.drawable.mascot_die_pic);
                tView.setImageUrl(listOfItems.get(position), imageLoader);



                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
                        i.putExtra("position", position);
                        i.putExtra("listOfItems", listOfItems);
                        startActivity(i);
                    }
                });


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
