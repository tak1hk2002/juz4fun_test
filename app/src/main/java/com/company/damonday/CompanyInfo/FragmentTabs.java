package com.company.damonday.CompanyInfo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TabHost;

import com.company.damonday.R;

import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewComment;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewCompany;
import com.company.damonday.CompanyInfo.Fragment.Fragment_ViewPhoto;

import function.GetPreviousObject;

/**
 * Created by LAM on 20/4/2015.
 */
public class FragmentTabs extends FragmentActivity {

    private TabHost mTabHost;
    private ImageView mImageView;
    private TabManager mTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyinfo_fragment_tab);

        mImageView = (ImageView) findViewById(R.id.CompanyView);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);

        //get the passed object from last activity
        Intent i = getIntent();
        GetPreviousObject PreviousObject = (GetPreviousObject)i.getSerializableExtra("sampleObject");

        //display company profile from last clicked action
        mImageView.setImageResource(PreviousObject.getId());

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

    }




}
