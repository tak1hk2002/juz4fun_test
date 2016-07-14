package com.company.damonday.Ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import java.util.List;

/**
 * Created by lamtaklung on 27/6/15.
 */
public class MyAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    private Context context;
    private Boolean isHomePage;
    private List<CompanyInfo> companyInfoItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MyAdapter(Context context, List<CompanyInfo> companyInfoItems, Boolean isHomePage)
    {
        this.context = context;
        this.companyInfoItems = companyInfoItems;
        this.isHomePage = isHomePage;


    }


    //the follwing @Overrride is about attribute of BaseAdapter
    @Override
    public int getCount() {
        return companyInfoItems.size();
    }

    @Override
    public Object getItem(int location)
    {
        return companyInfoItems.get(location);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.ranking_charts_gridview, null);

        //set the height higher of first photo if home page
        if(isHomePage && position == 0){
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 800));
        }else{
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        }
        final TextView title = (TextView) convertView.findViewById(R.id.ent_name);
        final NetworkImageView companyImage = (NetworkImageView) convertView
                .findViewById(R.id.picture);
        ImageView ranking = (ImageView) convertView.findViewById(R.id.ranking);

        final CompanyInfo c = companyInfoItems.get(position);

        // company image


        if(c.getUrl().length()>0) {
            companyImage.setImageUrl(c.getUrl(), imageLoader);
            // title
            title.setText(c.getTitle());
        }
        companyImage.setDefaultImageResId(R.drawable.mascot_die_pic);
        companyImage.setErrorImageResId(R.drawable.mascot_die_pic);
        ranking.setImageResource(c.getMoscotId());




        return convertView;
    }
}
