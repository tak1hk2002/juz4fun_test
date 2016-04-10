package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.company.damonday.R;

/**
 * Created by lamtaklung on 29/11/2015.
 */
public class ViewWrapper {

    View base;
    TextView label = null;
    RangeBar rangeBar = null;

    ViewWrapper(View base){
        this.base = base;
    }

    RangeBar getRangeBar(){
        if(rangeBar == null)
            rangeBar = (RangeBar) base.findViewById(R.id.dialog_rangebar);
        return rangeBar;
    }

    TextView getLabel(){
        if(label == null)
            label = (TextView)base.findViewById(R.id.title_ranking);
        return label;
    }
}
