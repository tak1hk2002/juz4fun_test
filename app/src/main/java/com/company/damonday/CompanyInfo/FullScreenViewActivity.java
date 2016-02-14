package com.company.damonday.CompanyInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.company.damonday.R;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 12/2/2016.
 */
public class FullScreenViewActivity extends Activity {

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyinfo_fragment_tab_fullscreen_view_pager);

        viewPager = (ViewPager) findViewById(R.id.pager);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        ArrayList<String> imageList = i.getStringArrayListExtra("listOfItems");
        System.out.println(imageList);
        System.out.println(position);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                imageList);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
