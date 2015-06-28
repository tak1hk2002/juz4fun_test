package com.company.damonday.Login;

/**
 * Created by lamtaklung on 31/5/15.
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;

//import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.R;

import com.company.damonday.function.TabManager;


public class FragmentTabs extends FragmentActivity {

    private TabHost mTabHost;
    private TabManager mTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment_tab);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);


        //initialise the tabhost
        mTabHost.setup();
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabManager.addTab(
                mTabHost.newTabSpec("Login").setIndicator("Login"),
                Fragment_Login.class, null);
        mTabManager.addTab(
                mTabHost.newTabSpec("Register").setIndicator("Register"),
                Fragment_Registration.class, null);




    }



}
