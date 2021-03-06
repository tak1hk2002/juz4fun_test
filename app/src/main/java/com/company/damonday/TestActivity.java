package com.company.damonday;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.damonday.AccountVerified.AccountVerified;
import com.company.damonday.Launch.LaunchPage;
import com.company.damonday.Home.Home;
import com.company.damonday.LatestComment.LatestComment;
import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MyFavourites.MyFavourites;
import com.company.damonday.NewFoundCompany.NewFoundCompany;
import com.company.damonday.Ranking.Ranking;
import com.company.damonday.ResetPassword.ResetPassword;
import com.company.damonday.Search.AdvancedSearch;
import com.company.damonday.Setting.Setting;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.DeepLink.DeepLinkSwitch;
import com.company.damonday.function.Firebase.CreateLogEvent;
import com.company.damonday.function.NetworkChecking.ConnectivityReceiver;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

/**
 * Created by tomc on 2/8/15.
 * It is a fragment activity class for the sliding menu
 */
public class TestActivity extends FragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
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
    //private CharSequence tempmtitle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public ArrayList<String> tempTitle;
    private NavDrawerListAdapter adapter;
    private LinearLayout mDrawerLinear;
    private Button btn_login, btn_register, btn_logout;
    private LinearLayout linear_logout, linear_login, linear_register;
    private SessionManager session;         //tomc 10/4/2016        login
    private LoginSQLiteHandler db;           //tomc 10/4/2016       login
    private Boolean isDeepLink = false;
    private AccessTokenTracker accessTokenTracker;
    private String tag = "";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private CreateLogEvent createLogEvent;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        System.out.println("Advance_search:" + mFirebaseRemoteConfig.getValue("advanced_search"));
        //set Create log event
        createLogEvent = new CreateLogEvent(mFirebaseAnalytics);


        //catch all the errors that cannot be detected
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                FirebaseCrash.report(ex);
            }
        });*/

        //get deep link info
        data = this.getIntent().getData();
        System.out.println("datadatadatadatadatadata: "+data);
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            System.out.println("Parma1: " + data.getQueryParameter("param1"));
            System.out.println("Parma2: " + data.getQueryParameter("param2"));
            System.out.println(data.getHost());
            Log.i("MyApp", "Deep link clicked " + uri);
        }



        Log.d("onCreate", "onCreate");
        FacebookSdk.sdkInitialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        //set soft input mode
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //check if the network is connected      Alan 14/5/2016
        checkConnection();

        //tomc 10/4/2016   login
        session = new SessionManager(this);
        db = new LoginSQLiteHandler(this);          //tomc 10/4/2016       login
        mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);
        linear_logout = (LinearLayout) findViewById(R.id.linear_logout);
        linear_login = (LinearLayout) findViewById(R.id.linear_login);
        linear_register = (LinearLayout) findViewById(R.id.linear_register);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        tempTitle = new ArrayList<String>();

        // adding nav drawer items to array
        // Home

        //Special handel, as （Juz4fun and 主頁） tomc 26/1/2016
        for(int i = 0; i < navMenuIcons.length(); i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }

        /*navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));*/


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
                    checkLogin();
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


        View v = getActionBar().getCustomView();

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
                // System.out.println("onDrawerClosed");
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

        if (savedInstanceState == null && data == null) {
            // on first time display view for first nav item
            displayView(7);
        }
        //go to deep link process
        else if (data != null){
            System.out.println("aaaaaaaaaaaaaaaa");
            displayView(8);
        }


        /*//keep checking whether the accessToken is null or not
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out

                }
            }
        };

        if(!accessTokenTracker.isTracking())
            accessTokenTracker.startTracking();*/

    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        if (isConnected) {
            //Toast.makeText(this, R.string.connection_success_warning, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.connection_fail_warning, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    /**
     * Slide menu item click listener
     */
    public void AdjustActiontitle(String title) {                        //tomc 6/3/2016
        View v = getActionBar().getCustomView();

        TextView titleTxtView = (TextView) v.findViewById(R.id.actionbarTitle);
        titleTxtView.setText(title);
    }

    public void hideBackButton() {                        //tomc 6/3/2016
//use by the fragment
        View v = getActionBar().getCustomView().findViewById(R.id.button_back);
        v.setVisibility(View.INVISIBLE);
    }

    public void showBackButton() {                        //tomc 6/3/2016
//use by the fragment
        View v = getActionBar().getCustomView().findViewById(R.id.button_back);
        v.setVisibility(View.VISIBLE);
    }

    public void showMenuButton() {                        //tomc 6/8/2016
//use by the fragment
        View v = getActionBar().getCustomView().findViewById(R.id.button_setting);
        v.setVisibility(View.VISIBLE);
    }

    public void hideMenuButton() {                        //tomc 6/8/2016
//use by the fragment
        View v = getActionBar().getCustomView().findViewById(R.id.button_setting);
        v.setVisibility(View.INVISIBLE);
    }

    public void checkLogin() {
        if (session.isLoggedIn() || AccessToken.getCurrentAccessToken() != null) {
            linear_login.setVisibility(View.GONE);
            linear_register.setVisibility(View.GONE);
            linear_logout.setVisibility(View.VISIBLE);
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (session.isLoggedIn()) {
                        session.setLogin(false);
                    }
                    else if(AccessToken.getCurrentAccessToken() != null){
                        LoginManager.getInstance().logOut();
                    }
                    db.deleteUsers();
                    displayView(0);
                    //mDrawerLayout.closeDrawer(Gravity.RIGHT);

                }

            });


        } else {
            linear_logout.setVisibility(View.GONE);
            linear_login.setVisibility(View.VISIBLE);
            linear_register.setVisibility(View.VISIBLE);

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
                    //checkLogin();           //tomc 8/5/2016   check Login
                }

                return true;


            default:                        //tomc 13/10/2015 press back button in menu

//                mDrawerLayout.closeDrawer(Gravity.RIGHT);

                onBackPressed();            //tomc 13/10/2015 call back button activity
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
    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        String api = null;
        //getActionBar().setDisplayHomeAsUpEnabled(true);     //make back button
        hideBackButton();
        showMenuButton();
        //showBackButton();                                     //6/8/2016  tomc ,follow ios back button logic
        Log.d("POSITION", Integer.toString(position));
        switch (position) {
            case 0:
                //主頁
                //getActionBar().setDisplayHomeAsUpEnabled(false);     //make back button
                //make back button
                //hideBackButton();
                fragment = new Home();
                tag = "home";
                getActionBar().show();
                break;
            case 1:
                //showBackButton();
                //進階搜尋
                fragment = new AdvancedSearch();
                tag = "search";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("1");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                break;
            case 2:
                //排行榜
                fragment = new Ranking();
                tag = "ranking";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("2");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                //Sets whether analytics collection is enabled for this app on this device.
                mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                //Sets the user ID property.
                mFirebaseAnalytics.setUserId(String.valueOf("64286830"));

                //Sets a user property to a given value.
                mFirebaseAnalytics.setUserProperty("HIHI", "Peter");
                break;
            case 3:
                //最新評論
                fragment = new LatestComment();
                api = APIConfig.URL_Latest_Comment;
                tag = "latestcomment";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("3");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                break;
            case 4:
                //我的最愛
                fragment = new MyFavourites();
                tag = "myfavourites";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("4");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                break;
            case 5:
                //新發現
                fragment = new NewFoundCompany();
                tag = "newfound";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("5");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                break;

            case 6:
                //設定
                fragment = new Setting();
                tag = "setting";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("6");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                api = "";
                break;
            case 7:
                //launch page
                fragment = new LaunchPage();
                tag = "launchpage";
                createLogEvent.setContentType(tag);
                createLogEvent.setItemID("7");
                createLogEvent.setItemName(tag);
                createLogEvent.send();
                getActionBar().hide();
                isDeepLink = false;
                break;
            case 8:
                //Deep link
                fragment = new AccountVerified();
                //DeepLinkSwitch deepLinkSwitch = new DeepLinkSwitch(data);
                //fragment = deepLinkSwitch.goToFragment();
                hideMenuButton();
                isDeepLink = true;
                tag= "";
                System.out.println("fragment: "+fragment);
                break;

            default:
                break;
        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("api", api);
            bundle.putString("last_fragment_tag", tag);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment homeFragment = fragmentManager.findFragmentByTag("home");
            System.out.println("testActivity_backstackEntryCount:=" + fragmentManager.getBackStackEntryCount());
            //clear all of the fragment at the stack
            //only the "main" stack, "empty" stack remains
            //fragmentManager.popBackStack("main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //System.out.println(fragmentManager.getBackStackEntryCount());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //add the home fragment to empty stack
            System.out.println("tag=" + tag);
            fragmentManager.popBackStack("emply", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStack("main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);   //important to clear repeat fragment in the stack //Alan 9/2/2016
//            if(tag.equals("home")) {
//                //System.out.println("homecase1");
//                //if (homeFragment == null) {
//                    System.out.println("homecase2");
//                    fragmentTransaction.replace(R.id.frame_container, fragment, tag).addToBackStack("empty");
//               // }
//            }
//            else {
//                System.out.println("homecase3");
//                fragmentManager.popBackStack("main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(R.id.frame_container, fragment, tag).addToBackStack("main");
//                fragmentTransaction.hide(fragmentManager.findFragmentByTag("home"));
//                fragmentTransaction.add(R.id.frame_container, fragment, "tag").addToBackStack("main");

            //     }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

            if (position != 7) {
                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                // System.out.println("tempmtitle=" + tempmtitle);
                // setTitle(navMenuTitles[position]);
            }
            mDrawerLayout.closeDrawer(mDrawerLinear);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {

        getActionBar().setTitle(title);
        AdjustActiontitle(getActionBar().getTitle().toString());

        if (getActionBar().getTitle() == null || getActionBar().getTitle() == "") {
            tempTitle.add("no title");
        }
        else
            tempTitle.add(title.toString());
        System.out.println("temptitle0:"+tempTitle);

    }

    public void setTitleBackButton(CharSequence title) {

        getActionBar().setTitle(title);
        AdjustActiontitle(title.toString());
        //System.out.println("temptitle4:"+tempTitle);
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
        boolean clickLoginOrRegister;
//when BackbuttonPressed
        System.out.println("onBackPressed");
        System.out.println(tag);
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d("EntryCount", Integer.toString(fragmentManager.getBackStackEntryCount()));

        String lastBarTitle = tempTitle.get(tempTitle.size()-1);
        //indicate whether the last page is login or register or not
        if(lastBarTitle.equals("登入") || lastBarTitle.equals("註冊"))
            clickLoginOrRegister = true;
        else
            clickLoginOrRegister = false;

        //deep link handling
        //go back to launch page
        if (isDeepLink){
            displayView(7);
        }
        /*
        * 因為launch page會有登入和註冊，click then back button會問係咪要離開，所以要加exception如果係launchpage and (登入or註冊)，
        * 可以back page
        * */
        else if (fragmentManager.getBackStackEntryCount() > 1 ||
                (tag.equals("launchpage") && clickLoginOrRegister)) {
            //如果不是返主頁
            if (!tempTitle.isEmpty()) {
                System.out.println(tempTitle);
                tempTitle.remove(tempTitle.size() - 1);
                System.out.println("temptitle3:" + tempTitle);
                setTitleBackButton(tempTitle.get(tempTitle.size()-1));
            }

            //System.out.println("fragmentManager.getBackStackEntryCount()tom=" + fragmentManager.getBackStackEntryCount());
            // fragmentManager.popBackStack();
            fragmentManager.popBackStackImmediate();
            if (fragmentManager.getBackStackEntryCount() == 1) {
                hideBackButton();
                showMenuButton();
                System.out.println("case1");
            }


        }
        else{
            //quit the app dialog
            new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.main_quit_prompt)
                    .setMessage(R.string.main_quit_message)
                    .setPositiveButton(R.string.main_quit_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.main_quit_negative, null)
                    .show();
        }
        //moveTaskToBack(true);
        //  super.onBackPressed(); // allows standard use of backbutton for page 1
    }
}


