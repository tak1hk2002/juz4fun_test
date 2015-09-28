package com.company.damonday.CompanyInfo;

import android.support.v4.app.FragmentActivity;

/**
 * Created by LAM on 20/4/2015.
 */
public class FragmentTabs extends FragmentActivity {

    /*private FragmentTabHost mTabHost;
    private TabManager mTabManager;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<String> listOfItems;
    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private String urlJsonObj;
    private int entId;
    private ProgressDialog pDialog;
    private static String TAG = FragmentTabs.class.getSimpleName();
    private String coverPage;

    public FragmentTabs() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyinfo_fragment_tab);
        ArrayList<String> list=new ArrayList<String>();

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        //get the passed object from last activity
        Intent i = getIntent();
        entId  = i.getIntExtra("Ent_id", -1);
        urlJsonObj = "http://damonday.tk/api/entertainment/details/?ent_id="+entId;

        //pass entId to fragment
        Bundle bundle = new Bundle();
        bundle.putInt("entId", entId);


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //initialise the tabhost
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

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


    }




    private void makeJsonArrayRequest() {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {


                    // loop through each json object
                    String status = response.getString("status");
                    JSONObject companyInfo = response.getJSONObject("data");

                    coverPage = companyInfo.getString("cover_image");

                } catch (JSONException e) {
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                hidepDialog();

                initViews();
                setViewPagerItemsWithAdapter();
                setUiPageViewController();

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                myViewPagerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
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


        //get the passed object from last activity
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        listOfItems = new ArrayList<String>();

            listOfItems.add(coverPage);
            listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
            listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");
            //listOfItems.add("http://cdn.inside.com.tw/wp-content/uploads/2012/05/Chrome.jpg");

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
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public MyViewPagerAdapter(ArrayList<String> listOfItems) {
            this.items = listOfItems;
        }

        //display the pics
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view;
            if(position == 0) {

                view = layoutInflater.inflate(R.layout.companyinfo_fragment_tab_video, container,false);
                final VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
                try{
                    MediaController mc = new MediaController(FragmentTabs.this);
                    videoView.setMediaController(mc);
                    videoView.setVideoURI(Uri.parse("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp"));
                    videoView.seekTo(100);
                }catch (Exception e){
                    Toast.makeText(FragmentTabs.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                videoView.requestFocus();
                *//*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                        videoView.start();
                    }
                });*//*
                //videoView.start();
            }
            else {
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
    }*/






}
