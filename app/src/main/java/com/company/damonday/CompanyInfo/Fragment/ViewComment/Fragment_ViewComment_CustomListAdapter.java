package com.company.damonday.CompanyInfo.Fragment.ViewComment;

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

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.view_companycomment_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profile_pic);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView postedDate = (TextView) convertView.findViewById(R.id.posted_date);
        ImageView like = (ImageView) convertView.findViewById(R.id.like);
        TextView rating1 = (TextView) convertView.findViewById(R.id.rating1);
        TextView rating2 = (TextView) convertView.findViewById(R.id.rating2);

        // getting movie data for the row
        Fragment_ViewComment_Comment m = commentItems.get(position);

        // profilePic image
        profilePic.setImageUrl(m.getProfilePic(), imageLoader);

        // title
        title.setText(m.getTitle());

        //username
        username.setText(m.getUsername());

        // posted date
        postedDate.setText(m.getPostedDate());

        //like icon
        if(Float.valueOf(m.getRating()) < 1.7)
            like.setImageResource(R.drawable.mascot_send_comment);
        else if (Float.valueOf(m.getRating()) >= 1.7 && Float.valueOf(m.getRating()) <= 3.3)
            like.setImageResource(R.drawable.mascot_smile_comment);
        else if (Float.valueOf(m.getRating()) > 3.3)
            like.setImageResource(R.drawable.mascot_happy_comment);

        //rating
        rating1.setText(m.getRating());
        rating2.setText(R.string.commentDetail_max_score);


        return convertView;
    }

}
