package com.company.damonday;



import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.damonday.Home.Home;
import com.company.damonday.LatestComment.latestcommentvolley;
import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MyFavourites.MyFavourites;
import com.company.damonday.NewFoundCompany.NewFoundCompany;
import com.company.damonday.Ranking.NavDrawerListAdapter;
import com.company.damonday.Ranking.Ranking_try;
import com.company.damonday.Search.search;
import com.company.damonday.Setting.Setting;
import com.facebook.FacebookSdk;

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
    private LinearLayout mDrawerLinear;
    private Button btn_login,btn_register,btn_logout;
    private SessionManager session;         //tomc 10/4/2016        login
    private LoginSQLiteHandler db;           //tomc 10/4/2016       login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate");
        FacebookSdk.sdkInitialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        //tomc 10/4/2016   login
        session = new SessionManager(this);
        db = new LoginSQLiteHandler(this);          //tomc 10/4/2016       login


        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
         mDrawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);
        btn_logout =(Button) findViewById(R.id.btn_logout);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register= (Button)findViewById(R.id.btn_register);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home

        //Special handel, as （Juz4fun and 主頁） tomc 26/1/2016
        navDrawerItems.add(new NavDrawerItem("主頁", navMenuIcons.getResourceId(0, -1)));



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
       // getActionBar().setDisplayHomeAsUpEnabled(true);     //make back button

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_custom_layout);

        //sliding menu   tomc 23/4/2016
        getActionBar().getCustomView().findViewById(R.id.button_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(TestActivity.this, "hello", Toast.LENGTH_SHORT).show();
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {//tomc 31/8/2015
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }

            }
        });

        //back button tomc 23/4/2016
        getActionBar().getCustomView().findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(TestActivity.this , "back" , Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        });



        View v=getActionBar().getCustomView();

        TextView titleTxtView = (TextView) v.findViewById(R.id.actionbarTitle);



       // getActionBar().setDisplayShowTitleEnabled(false);
        //getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.btn_back, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //calling when Drawer is closed tomc 26/1/2016


               // getActionBar().setTitle(mTitle);
                System.out.println("onDrawerClosed");
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //calling when Drawer is opened tomc 26/1/2016

              //  getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };


        //mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        //tomc 10/4/2016
        Log.d("yyyyyyyy", Boolean.toString(session.isLoggedIn()));
        if(session.isLoggedIn()){
            btn_login.setVisibility(View.GONE);

                 btn_logout.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       session.setLogin(false);
                                                       db.deleteUsers();

                                                       //display message login successfully
                                                       AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                                                       ab.setTitle(R.string.logout_success);
                                                       ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                                                           @Override
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               Setting setting = new Setting();
                                                               FragmentManager fragmentManager = getSupportFragmentManager();
                                                               //clear all of the fragment at the stack
                                                               fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                               fragmentTransaction.replace(R.id.frame_container, setting, "setting").addToBackStack(null);

                                                               fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                               fragmentTransaction.commit();

                                                           }
                                                       });
                                                       ab.create().show();
                                                   }

        });
        }
        else {

            btn_logout.setVisibility(View.GONE);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("lastFragment", "setting");

                    Fragment_Login fragment_login = new Fragment_Login();
                    fragment_login.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment_login, "login").addToBackStack(null);

                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);

                }
            });

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment_Registration fragment_registration = new Fragment_Registration();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment_registration, "register").addToBackStack(null);

                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);

                }
            });
        }




    }

    /**
     * Slide menu item click listener
     */
    public void AdjustActiontitle(String title){                        //tomc 6/3/2016
        View v=getActionBar().getCustomView();

        TextView titleTxtView = (TextView) v.findViewById(R.id.actionbarTitle);
        titleTxtView.setText(title);
    }

    public void hideBackButton(){                        //tomc 6/3/2016


        View v=getActionBar().getCustomView().findViewById(R.id.button_back);
       v.setVisibility(View.INVISIBLE);
    }

    public void showBackButton(){                        //tomc 6/3/2016


        View v=getActionBar().getCustomView().findViewById(R.id.button_back);
        v.setVisibility(View.VISIBLE);
    }




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
       // getMenuInflater().inflate(R.menu.menu_main, menu);
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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
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
        //getActionBar().setDisplayHomeAsUpEnabled(true);     //make back button
        showBackButton();
        switch (position) {
            case 0:
                //主頁
                //getActionBar().setDisplayHomeAsUpEnabled(false);     //make back button
                     //make back button
                hideBackButton();
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
             System.out.println("testActivity_backstackEntryCount:="+fragmentManager.getBackStackEntryCount());
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
            System.out.println("ordertest1");
            mDrawerLayout.closeDrawer(mDrawerLinear);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
        AdjustActiontitle(getActionBar().getTitle().toString());

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


