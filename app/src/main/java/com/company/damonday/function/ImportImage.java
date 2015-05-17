package com.company.damonday.function;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by LAM on 13/4/2015.
 */
public class ImportImage extends ImageView
{

    public ImportImage(Context context){
        super(context);
    }


    public ImportImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public ImportImage(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}