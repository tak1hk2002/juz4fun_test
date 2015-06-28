package com.company.damonday.CompanyInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.company.damonday.R;

import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewComment;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewCompany;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewPhoto;
import com.company.damonday.function.TabManager;
import com.company.damonday.function.getJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import function.GetPreviousObject;

/**
 * Created by LAM on 20/4/2015.
 */
public class FragmentTabs extends FragmentActivity {

    private TabHost mTabHost;
    private ImageView mImageView;
    private TabManager mTabManager;
    private TextView mTitleTextView;
    private TextView mLikeTextView;
    private TextView mFairTextView;
    private TextView mDislikeTextView;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<String> listOfItems;
    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private String JsonText = null;
    private JSONArray promotion_images;
    private String like;
    private String fair;
    private String dislike;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyinfo_fragment_tab);

        //mImageView = (ImageView) findViewById(R.id.CompanyView);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mLikeTextView = (TextView) findViewById(R.id.like);
        mFairTextView = (TextView) findViewById(R.id.fair);
        mDislikeTextView = (TextView) findViewById(R.id.dislike);

        //get the passed object from last activity
        //Intent i = getIntent();
        //GetPreviousObject PreviousObject = (GetPreviousObject)i.getSerializableExtra("sampleObject");


        //display company profile from last clicked action
        //mImageView.setImageResource(PreviousObject.getId());

        //initialise the tabhost
        mTabHost.setup();
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabManager.addTab(
                mTabHost.newTabSpec("Fragment1").setIndicator("Fragment1"),
                Fragment_ViewCompany.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec("Fragment2").setIndicator("Fragment2"),
                Fragment_ViewComment.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec("Fragment3").setIndicator("Fragment3"),
                Fragment_ViewPhoto.class, null);

//        getJson abc = new getJson();
//        abc.execute("http://www.damonday.tk/api/entertainment/get_entertainment_details/?ent_id=9");
//        try {
//            JsonText = abc.get().toString();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
       // parseJson();

        initViews();
        contentString();
        setViewPagerItemsWithAdapter();
        setUiPageViewController();

    }

    public void parseJson() {
        try {
            JSONObject obj = new JSONObject(JsonText);
            JSONObject data = new JSONObject(obj.getString("data"));
            JSONObject rating = new JSONObject(data.getString("rating"));
            String average_score = rating.getString("average_score");
            like = rating.getString("like");
            fair = rating.getString("fair");
            dislike = rating.getString("dislike");

            title = data.getString("title");
            promotion_images = data.getJSONArray("promotion_images");
            String d = data.getString("title");



        }
        catch (JSONException e) {e.printStackTrace();}

    }



    private void contentString(){
        mTitleTextView.setText(title);
        mLikeTextView.setText(like);
        mFairTextView.setText(fair);
        mDislikeTextView.setText(dislike);
    }

    //initial of the pics
    private void initViews() {

        //getActionBar().hide();

        //get the passed object from last activity
        Intent i = getIntent();
        GetPreviousObject PreviousObject = (GetPreviousObject)i.getSerializableExtra("sampleObject");
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        listOfItems = new ArrayList<String>();

            listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
            listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
            listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
            listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");

    }



    private void setViewPagerItemsWithAdapter() {
        myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);  //call Class to create Object
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void setUiPageViewController() {
        dotsLayout = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        dotsCount = myViewPagerAdapter.getCount();     //get number of pages(Pics) in ViewPager from the adapter
        dots = new TextView[dotsCount];     //Create a TextView array

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
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

        public MyViewPagerAdapter(ArrayList<String> listOfItems) {
            this.items = listOfItems;
        }

        //display the pics
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.pager_view, container,false);

            final ImageView tView = (ImageView)view.findViewById(R.id.PageView);

            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);

            float density  = getResources().getDisplayMetrics().density;
            float dpHeight = outMetrics.heightPixels / density;
            final float dpWidth  = outMetrics.widthPixels / density;

/*
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    HttpImageConnection ImageUrl = new HttpImageConnection(items.get(position), Math.round(dpWidth));
                    final Bitmap bitmap = ImageUrl.Connection();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tView.setImageBitmap(bitmap);
                        }
                    });
                }
            });
*/

            //thread.start();

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
