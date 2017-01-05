package com.company.damonday.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.company.damonday.R;

import java.util.ArrayList;

/**
 * Created by lamtaklung on 18/12/2016.
 */

public class FastSearchAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    private ArrayList<FastSearchModel> productDetails = new ArrayList<FastSearchModel>();
    int count;
    // Typeface type;
    Context context;

    //constructor method
    public FastSearchAdapter(Context context, ArrayList<FastSearchModel> product_details) {

        layoutInflater = LayoutInflater.from(context);

        this.productDetails = product_details;
        this.count = product_details.size();
        this.context = context;


    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int arg0) {
        return productDetails.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.search_fast_list_row, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.entertainment_name);
        TextView companyName = (TextView) convertView.findViewById(R.id.company_name);

        if (productDetails.size() > 0) {
            FastSearchModel m = productDetails.get(position);

            title.setText(m.getTitle());

            companyName.setText("@" + m.getCompanyName());
        }

        return convertView;
    }
}
