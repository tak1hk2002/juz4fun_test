package com.company.damonday.LatestComment;

/**
 * Created by tom on 14/6/15.
 */


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import com.company.damonday.LatestComment.latestcomment;

import java.util.List;

public class latestcomment_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<latestcomment> latestcommentItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public latestcomment_Adapter(Activity activity, List<latestcomment> latestcommentItems) {
        this.activity = activity;
        this.latestcommentItems = latestcommentItems;
    }

    @Override
    public int getCount() {
        return latestcommentItems.size();
    }

    @Override
    public Object getItem(int location) {
        return latestcommentItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.latestcomment_list_row, null);      //tomc

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.average_score);
        TextView comment = (TextView) convertView.findViewById(R.id.comment);
        TextView year = (TextView) convertView.findViewById(R.id.days_before);

        // getting movie data for the row
        latestcomment m = latestcommentItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText("Rating: " + m.getAveage_scrore());
        //rating.setText("tom");
        Log.d("1114", m.getAveage_scrore());
       // rating.setText("Rating: " );
        // genre
//        String genreStr = "";
//        for (String str : m.getGenre()) {
//            genreStr += str + ", ";
//        }
//        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
//                genreStr.length() - 2) : genreStr;
//        genre.setText(genreStr);

        comment.setText(m.getComment());


        // release year
       // year.setText(String.valueOf(m.getDay_before()));
        year.setText(m.getDay_before());
        return convertView;
    }

}
