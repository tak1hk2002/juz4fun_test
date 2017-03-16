package com.company.damonday.Framework;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lamtaklung on 20/2/2017.
 */

public class FontTypeText extends TextView {

    public FontTypeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontTypeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontTypeText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/12.ttf");
            setTypeface(tf);
        }
    }

}
