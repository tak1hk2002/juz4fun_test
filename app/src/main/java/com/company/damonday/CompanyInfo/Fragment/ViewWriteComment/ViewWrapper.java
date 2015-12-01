package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.company.damonday.R;

/**
 * Created by lamtaklung on 29/11/2015.
 */
public class ViewWrapper {

    View base;
    RatingBar rate = null;
    TextView label = null;

    ViewWrapper(View base){
        this.base = base;
    }

    RatingBar getRatingBar(){
        if(rate == null)
            rate =(RatingBar) base.findViewById(R.id.dialog_ratingbar);
        return rate;
    }

    TextView getLabel(){
        if(label == null)
            label = (TextView)base.findViewById(R.id.title_ranking);
        return label;
    }
}
