package com.company.damonday.Ranking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.damonday.CompanyInfo.FragmentTabs;
import com.company.damonday.R;

import java.util.ArrayList;
import java.util.List;

import function.GetPreviousObject;

/**
 * Created by LAM on 22/4/2015.
 */
public class Ranking extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new MyAdapter(this));
    }





    private class MyAdapter extends BaseAdapter
    {
        private List<Item> items = new ArrayList<Item>();
        private LayoutInflater inflater;
        private Context context;

        public MyAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);
            this.context = context;
            items.add(new Item("Image 1", R.drawable.nature1));
            items.add(new Item("Image 2", R.drawable.nature2));
            items.add(new Item("Image 3", R.drawable.tree1));
            items.add(new Item("Image 4", R.drawable.nature3));
            items.add(new Item("Image 5", R.drawable.tree2));

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup)
        {
            View v = view;
            ImageView picture;
            TextView name;

            if(v == null)
            {
                v = inflater.inflate(R.layout.charts_gridview, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }



            picture = (ImageView)v.getTag(R.id.picture);
            name = (TextView)v.getTag(R.id.text);

            Item item = (Item)getItem(i);

            picture.setImageResource(item.drawableId);
            name.setText(item.name);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You Clicked " + items.get(i).drawableId, Toast.LENGTH_LONG).show();

                    //pass the following object to next activity
                    GetPreviousObject passedObject = new GetPreviousObject(items.get(i).drawableId, "");
                    Intent i = new Intent(Ranking.this, FragmentTabs.class);
                    i.putExtra("sampleObject", passedObject);
                    startActivity(i);
                }
            });
            return v;
        }

        private class Item
        {
            final String name;
            final int drawableId;

            Item(String name, int drawableId)
            {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
    }
}
