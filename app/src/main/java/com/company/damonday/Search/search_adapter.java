package com.company.damonday.Search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import java.util.List;

/**
 * Created by tom on 25/10/15.
 */
public class search_adapter extends BaseAdapter {


    private Activity activity;
    private LayoutInflater inflater;
    private List<CompanyObject> companyObjects;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public search_adapter(Activity activity, List<CompanyObject> companyObjects) {
        this.activity = activity;
        this.companyObjects = companyObjects;
    }

    @Override
    public int getCount() {
        return companyObjects.size();
    }

    @Override
    public Object getItem(int location) {
        return companyObjects.get(location);
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
            convertView = inflater.inflate(R.layout.myfavourites_list_row, null);      //tomc

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.average_score);
        TextView price_range = (TextView) convertView.findViewById(R.id.price_range);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView like = (TextView) convertView.findViewById(R.id.like);
        TextView fair = (TextView) convertView.findViewById(R.id.fair);
        TextView dislike = (TextView) convertView.findViewById(R.id.dislike);


        // getting movie data for the row
        CompanyObject m = companyObjects.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText(m.getAveage_scrore() + "/5");

        //price_range
        price_range.setText(m.getPrice_range());

        //category
        category.setText(m.getCategory());

        //like
        like.setText(m.getLike());

        //Dislike
        dislike.setText(m.getDislike());

        //Fair
        fair.setText(m.getFair());


        return convertView;
    }

}

