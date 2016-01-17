package com.company.damonday.CompanyInfo.Fragment.ViewComment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by lamtaklung on 12/1/2016.
 *
 * To get the Scroll Y position
 */
public class CustomScrollView extends ScrollView {
    private ScrollChangedListener mScrollChangedListener;

    public interface ScrollChangedListener
    {
        void onScrollChanged(int y);
    }

    public void setScrollChangedListener(ScrollChangedListener l)
    {
        mScrollChangedListener = l;
    }

    public CustomScrollView(Context context) {
        super(context, null);
        setFadingEdgeLength(0);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(mScrollChangedListener != null)
            mScrollChangedListener.onScrollChanged(y);
    }

}
