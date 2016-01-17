package com.company.damonday;



import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.company.damonday.Home.Home;
import com.company.damonday.LatestComment.latestcommentvolley;
import com.company.damonday.MyFavourites.MyFavourites;
import com.company.damonday.NewFoundCompany.NewFoundCompany;
import com.company.damonday.Ranking.NavDrawerListAdapter;
import com.company.damonday.Ranking.Ranking_try;
import com.company.damonday.Search.search;
import com.company.damonday.Search.search_fast;
import com.company.damonday.Setting.Setting;

import java.util.ArrayList;

/**
 * Created by tomc on 2/8/15.
 * It is a fragment activity class for the sliding menu
 *
 *
 *
 *
 */
public class TestActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private CharSequence tempmtitle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // PhotosgetResourceId
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);     //make back button
        //getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.back, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                 //action bar 出現

        Log.d("onCreateOptionsMenu", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
        // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {               //action bar onclick
        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action bar actions click


        Log.d("onOptionsItemSelected", Integer.toString(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.btnMyMenu:

                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {//tomc 31/8/2015
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }

                return true;

//            case R.id.btnMyMenu:
//                Log.d("btn","btn");
//                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {//tomc 31/8/2015
//                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
//                } else {
//                    mDrawerLayout.openDrawer(Gravity.RIGHT);
//                }
//
//                return true;


            default:                        //tomc 13/10/2015 press back button in menu

//                mDrawerLayout.closeDrawer(Gravity.RIGHT);
//
//
//                //finish();
//                // onKeyDown(KeyEvent.KEYCODE_BACK, null);     //tomc 13/10/2015 call
//
                onBackPressed();            //tomc 13/10/2015 call back button activity
//                //Log.d("222222","rrrrrrr");
//
//                //setTitle("dddd");
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                System.out.println("tom");
//                System.out.println(fragmentManager.getBackStackEntryCount());
//
//                if (fragmentManager.getBackStackEntryCount() == 0) {
//                    //如果返主頁
//                    setTitle(navMenuTitles[0]);
//                    displayView(0);
//                } else {
//                    //如果不是返主頁
//                    setTitle(mTitle);
//                }
//
//                //mDrawerLayout.closeDrawer(Gravity.RIGHT);
                return super.onOptionsItemSelected(item);

        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("onPrepareOptionsMenu", "onPrepareOptionsMenu");

        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);

    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        String tag = null;
        getActionBar().setDisplayHomeAsUpEnabled(true);     //make back button

        switch (position) {
            case 0:
                //主頁
                getActionBar().setDisplayHomeAsUpEnabled(false);     //make back button
                     //make back button
                fragment = new Home();
                tag = "home";
                break;
            case 1:
                //進階搜尋
                fragment = new search();
                tag = "search";
                break;
            case 2:
                //排行榜
                fragment = new Ranking_try();
                tag = "ranking";
                break;
            case 3:
                //最新評論
                fragment = new latestcommentvolley();
                tag = "latestcommentvolley";
                break;
            case 4:
                //我的最愛
                fragment = new MyFavourites();
                tag = "myfavourites";
                break;
            case 5:
                //新發現
                fragment = new NewFoundCompany();
                tag = "newfound";
                break;

            case 6:
                //設定
                fragment = new Setting();
                tag = "setting";
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            //clear all of the fragment at the stack
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //System.out.println(fragmentManager.getBackStackEntryCount());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment, tag).addToBackStack("main");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);

            tempmtitle = mTitle;
            System.out.println("tempmtitle=" + tempmtitle);

            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.d("onPostCreate", "onPostCreate ");
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("onConfigurationChanged", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);


        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        System.out.println("onBackPressed");

        mDrawerLayout.closeDrawer(Gravity.RIGHT);

        FragmentManager fragmentManager = getSupportFragmentManager();

        System.out.println(fragmentManager.getBackStackEntryCount());

        if (fragmentManager.getBackStackEntryCount() == 0) {
            //如果返主頁
            setTitle(R.string.home);
            displayView(0);
            System.out.println("case1");
        } else {
            //如果不是返主頁
            setTitle(mTitle);
            System.out.println("case2");
        }


        //moveTaskToBack(true);
      //  super.onBackPressed(); // allows standard use of backbutton for page 1
    }
}


