package com.company.damonday.Framework.ImageList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import java.util.List;

/**
 * Created by lamtaklung on 27/6/15.
 */
public class ImageList_CustomListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private Boolean isHomePage;
    private List<ImageInfo> imageInfoItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ImageList_CustomListAdapter(Context context, List<ImageInfo> imageInfoItems, Boolean isHomePage) {
        this.context = context;
        this.imageInfoItems = imageInfoItems;
        this.isHomePage = isHomePage;


    }


    //the follwing @Overrride is about attribute of BaseAdapter
    @Override
    public int getCount() {
        return imageInfoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return imageInfoItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.ranking_charts_gridview, null);

        //set the height higher of first photo if home page
        if (isHomePage && position == 0) {
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 700));
        } else {
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 500));
            //convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        }
        final TextView title = (TextView) convertView.findViewById(R.id.ent_name);
        final TextView companyName = (TextView) convertView.findViewById(R.id.company_name);
        final NetworkImageView companyImage = (NetworkImageView) convertView
                .findViewById(R.id.picture);
        ImageView ranking = (ImageView) convertView.findViewById(R.id.ranking);

        final ImageInfo c = imageInfoItems.get(position);

        // company image


        if (c.getUrl().length() > 0) {
            companyImage.setImageUrl(c.getUrl(), imageLoader);
            companyImage.setDefaultImageResId(R.drawable.mascot_nopic);
            companyImage.setErrorImageResId(R.drawable.mascot_die_pic);
            // title
            title.setText(c.getTitle());
            companyName.setText("@"+c.getCompany()+"//");
        }
        companyImage.setDefaultImageResId(R.drawable.mascot_die_pic);
        companyImage.setErrorImageResId(R.drawable.mascot_die_pic);
        ranking.setImageResource(c.getMoscotID());


        return convertView;
    }
}
