package com.company.damonday.Ranking;

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

import java.util.List;

import function.GetPreviousObject;

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
            convertView = inflater.inflate(R.layout.charts_gridview, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        TextView title = (TextView) convertView.findViewById(R.id.text);
        NetworkImageView companyImage = (NetworkImageView) convertView
                .findViewById(R.id.picture);

        CompanyInfo c = companyInfoItems.get(position);
        Log.d("position", Integer.toString(position));
        Log.d("getUrl", c.getUrl());

        // company image
        companyImage.setImageUrl(c.getUrl(), imageLoader);

        // title
        title.setText(c.getTitle());




        /*convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked " + companyInfoItems.get(position).getUrl(), Toast.LENGTH_LONG).show();

                //pass the following object to next activity
                GetPreviousObject passedObject = new GetPreviousObject(companyInfoItems.get(position).getEnt_id(),-1,-1);
                Intent i = new Intent(this, FragmentTabs.class);
                i.putExtra("sampleObject", passedObject);
                startActivity(i);
            }
        });*/
        return convertView;
    }
}
