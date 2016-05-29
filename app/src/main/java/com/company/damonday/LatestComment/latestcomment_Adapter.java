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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.Fragment.ViewComment.Fragment_ViewComment_Comment;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import com.company.damonday.LatestComment.latestcomment;

import org.w3c.dom.Text;

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

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.latestcomment_list_row, parent, false);

        imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profile_pic);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView postedDate = (TextView) convertView.findViewById(R.id.posted_date);
        ImageView averagePic = (ImageView) convertView.findViewById(R.id.average_pic);
        TextView rating1 = (TextView) convertView.findViewById(R.id.rating1);
        TextView rating2 = (TextView) convertView.findViewById(R.id.rating2);
        TextView entNameAndCompanyName = (TextView) convertView.findViewById(R.id.ent_name_company_name);

        // getting movie data for the row
        latestcomment m = latestcommentItems.get(position);

        // profilePic image
        profilePic.setImageUrl(m.getProfilePic(), imageLoader);
        profilePic.setDefaultImageResId(R.drawable.mascot_die_pic);
        profilePic.setErrorImageResId(R.drawable.mascot_die_pic);

        // title
        title.setText(m.getTitle());

        //Ent name and Company Name
        entNameAndCompanyName.setText(m.getEntName()+"@"+m.getCompanyName());

        //username
        username.setText(m.getUsername());

        // posted date
        postedDate.setText(m.getPostedDate());

        //like icon
        if(m.getRating().equals("null")) {
            averagePic.setImageResource(R.drawable.mascot_send_comment);
            rating2.setVisibility(View.GONE);
        }
        if(Float.parseFloat(m.getRating()) < 1.7)
            averagePic.setImageResource(R.drawable.mascot_send_comment);
        else if (Float.parseFloat(m.getRating()) >= 1.7 && Float.parseFloat(m.getRating()) <= 3.3)
            averagePic.setImageResource(R.drawable.mascot_smile_comment);
        else if (Float.parseFloat(m.getRating()) > 3.3)
            averagePic.setImageResource(R.drawable.mascot_happy_comment);

        //rating
        rating1.setText(m.getRating());
        return convertView;
    }

}
