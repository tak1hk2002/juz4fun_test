package com.company.damonday.Framework.CommentList;

/**
 * Created by tom on 14/6/15.
 */


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import java.util.List;

public class CommentList_CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<CommentList> commentListItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CommentList_CustomListAdapter(Activity activity, List<CommentList> commentListItems) {
        this.activity = activity;
        this.commentListItems = commentListItems;
    }

    @Override
    public int getCount() {
        return commentListItems.size();
    }

    @Override
    public Object getItem(int location) {
        return commentListItems.get(location);
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
        CommentList m = commentListItems.get(position);

        // profilePic image
        profilePic.setImageUrl(m.getProfilePic(), imageLoader);
        profilePic.setDefaultImageResId(R.drawable.pro_pic_default);
        profilePic.setErrorImageResId(R.drawable.mascot_die_pic);

        // title
        title.setText(m.getTitle());

        //Ent name and Company Name
        if(m.getEntName() != null && m.getCompanyName() != null)
            entNameAndCompanyName.setText(m.getEntName() + "@" + m.getCompanyName());
        else
            entNameAndCompanyName.setText("");

        //username
        username.setText(m.getUsername());

        // posted date
        postedDate.setText(m.getPostedDate());

        //like icon
        if (m.getRating().equals("null")) {
            averagePic.setImageResource(R.drawable.mascot_send_comment);
            rating2.setVisibility(View.GONE);
        }
        if (Float.parseFloat(m.getRating()) < 2.1)
            averagePic.setImageResource(R.drawable.mascot_send_comment);
        else if (Float.parseFloat(m.getRating()) >= 2.1 && Float.parseFloat(m.getRating()) <= 3.5)
            averagePic.setImageResource(R.drawable.mascot_smile_comment);
        else if (Float.parseFloat(m.getRating()) > 3.5)
            averagePic.setImageResource(R.drawable.mascot_happy_comment);

        //rating
        rating1.setText(m.getRating());
        return convertView;
    }

}
