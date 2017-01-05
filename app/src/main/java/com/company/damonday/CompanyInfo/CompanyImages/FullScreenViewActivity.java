package com.company.damonday.CompanyInfo.CompanyImages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.company.damonday.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 12/2/2016.
 */
public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private int mCurrentPosition;
    private int mScrollState;

    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyinfo_fragment_tab_fullscreen_view_pager);

        viewPager = (ViewPager) findViewById(R.id.pager);
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        final ArrayList<String> imageList = i.getStringArrayListExtra("listOfItems");
        System.out.println(imageList);
        System.out.println(position);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                imageList);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                if(mCurrentPosition == 0) {
                    viewPager.setCurrentItem(imageList.size() - 1, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                handleScrollState(state);
                mScrollState = state;
            }

            private void handleScrollState(final int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    setNextItemIfNeeded();
                }
            }


            private void setNextItemIfNeeded() {
                if (!isScrollStateSettling()) {
                    handleSetNextItem();
                }
            }

            private boolean isScrollStateSettling() {
                return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
            }

            private void handleSetNextItem() {
                System.out.println("HIHI");
                final int lastPosition = imageList.size() - 1;
                if(mCurrentPosition == lastPosition) {
                    viewPager.setCurrentItem(1, false);
                }
            }
        });



        // ViewPager Indicator

        mIndicator.setViewPager(viewPager);
    }


}
