package com.company.damonday.CompanyInfo.Fragment.ViewCompany;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.company.damonday.Framework.CommentList.CommentList;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import java.util.List;

/**
 * Created by lamtaklung on 2/10/2016.
 */

public class Fragment_ViewCompany_CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<String> InfoListItems;
    private String[] titleListItmes;
    private List<Integer> showDetailIndicator;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public Fragment_ViewCompany_CustomListAdapter(Activity activity, List<String> InfoListItems, String[] titleListItmes, List<Integer> showDetailIndicator) {
        this.activity = activity;
        this.InfoListItems = InfoListItems;
        this.titleListItmes = titleListItmes;
        this.showDetailIndicator = showDetailIndicator;
    }


    @Override
    public int getCount() {
        return InfoListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return InfoListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_companyinfo_simpleadapter, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView text = (TextView) convertView.findViewById(R.id.text);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);

        title.setText(titleListItmes[position]);
        text.setText(InfoListItems.get(position));
        if(showDetailIndicator.contains(position))
            indicator.setVisibility(View.VISIBLE);
        else
            indicator.setVisibility(View.GONE);


        return convertView;
    }
}
