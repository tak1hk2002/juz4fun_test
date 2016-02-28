package com.company.damonday.Ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private List<CompanyInfo> companyInfoItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MyAdapter(Context context, List<CompanyInfo> companyInfoItems)
    {
        this.context = context;
        this.companyInfoItems = companyInfoItems;


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
        ProgressBar mProgress=(ProgressBar) convertView.findViewById(R.id.pb);


        final TextView title = (TextView) convertView.findViewById(R.id.text);
        final NetworkImageView companyImage = (NetworkImageView) convertView
                .findViewById(R.id.picture);
        ImageView ranking = (ImageView) convertView.findViewById(R.id.ranking);

        final CompanyInfo c = companyInfoItems.get(position);

        // company image


        if(c.getUrl().length()>0) {
            companyImage.setImageUrl(c.getUrl(), imageLoader);
            // title
            title.setText(c.getTitle());
            mProgress.setVisibility(View.INVISIBLE);
        }
        companyImage.setDefaultImageResId(R.drawable.ic_launcher);
        companyImage.setErrorImageResId(R.drawable.ic_launcher);
        ranking.setImageResource(c.getMoscotId());




        return convertView;
    }
}
