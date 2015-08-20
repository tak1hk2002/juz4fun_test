package com.company.damonday.Ranking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.FragmentTabs;
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
        ProgressBar mProgress=(ProgressBar) convertView.findViewById(R.id.pb);


        final TextView title = (TextView) convertView.findViewById(R.id.text);
        final NetworkImageView companyImage = (NetworkImageView) convertView
                .findViewById(R.id.picture);

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


        /*imageLoader.get(c.getUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response != null) {
                    Bitmap bitmap = response.getBitmap();
                    Log.d("response", response.getRequestUrl().toString());
                    if (bitmap != null) {
                        pb.setVisibility(View.GONE);
                        companyImage.setVisibility(View.VISIBLE);
                        companyImage.setImageBitmap(bitmap);
                        // title
                        title.setText(c.getTitle());
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);
                companyImage.setVisibility(View.VISIBLE);
                companyImage.setImageResource(R.drawable.ic_launcher);
            }


        });*/








        /*convertView.setOnItemClickListener(new View.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(context, "You Clicked " + companyInfoItems.get(position).getUrl(), Toast.LENGTH_LONG).show();

                //pass the following object to next activity
                GetPreviousObject passedObject = new GetPreviousObject(companyInfoItems.get(position).getEnt_id(), -1, -1);
                Intent i = new Intent(context, FragmentTabs.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("sampleObject", passedObject);
                context.startActivity(i);
            }
        });*/
        return convertView;
    }
}
