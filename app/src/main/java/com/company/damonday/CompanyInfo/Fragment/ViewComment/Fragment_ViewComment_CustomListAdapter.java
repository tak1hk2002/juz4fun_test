package com.company.damonday.CompanyInfo.Fragment.ViewComment;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

/**
 * Created by lamtaklung on 23/7/15.
 */
public class Fragment_ViewComment_CustomListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Fragment_ViewComment_Comment> commentItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public Fragment_ViewComment_CustomListAdapter(Context context, List<Fragment_ViewComment_Comment> commentItems) {
        this.context = context;
        this.commentItems = commentItems;
    }

    @Override
    public int getCount() {
        return commentItems.size();
    }

    @Override
    public Object getItem(int location) {
        return commentItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_companycomment_list_row, parent, false);

        imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profile_pic);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView postedDate = (TextView) convertView.findViewById(R.id.posted_date);
        ImageView averagePic = (ImageView) convertView.findViewById(R.id.average_pic);
        TextView rating1 = (TextView) convertView.findViewById(R.id.rating1);

        // getting movie data for the row
        Fragment_ViewComment_Comment m = commentItems.get(position);

        // profilePic image
        profilePic.setImageUrl(m.getProfilePic(), imageLoader);
        profilePic.setDefaultImageResId(R.drawable.mascot_die_pic);
        profilePic.setErrorImageResId(R.drawable.mascot_die_pic);

        // title
        title.setText(m.getTitle());

        //username
        username.setText(m.getUsername());

        // posted date
        postedDate.setText(m.getPostedDate());

        Log.d("getRating", m.getRating());

        //like icon
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
