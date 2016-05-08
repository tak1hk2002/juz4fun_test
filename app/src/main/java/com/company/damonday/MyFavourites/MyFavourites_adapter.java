package com.company.damonday.MyFavourites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.CompanySQLiteHandler;
import com.company.damonday.R;
import com.company.damonday.Search.CompanyObject;
import com.company.damonday.function.AppController;

import java.util.List;

/**
 * Created by tom on 25/10/15.
 */
public class MyFavourites_adapter extends BaseAdapter {


        private Activity activity;
        private LayoutInflater inflater;
        private List<MyFavouritesObject> myFavouritesObjects;
        private CompanySQLiteHandler myfavouriteDB;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public MyFavourites_adapter(Activity activity, List<MyFavouritesObject> myFavouritesObjects) {
            this.activity = activity;
            this.myFavouritesObjects = myFavouritesObjects;
        }

        @Override
        public int getCount() {
            return myFavouritesObjects.size();
        }

        @Override
        public Object getItem(int location) {
            return myFavouritesObjects.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.myfavourites_list_row, null);      //tomc

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView rating = (TextView) convertView.findViewById(R.id.average_score);
            TextView price_range = (TextView) convertView.findViewById(R.id.price_range);
            TextView category = (TextView) convertView.findViewById(R.id.category);
            TextView like = (TextView) convertView.findViewById(R.id.like);
            TextView fair = (TextView) convertView.findViewById(R.id.fair);
            TextView dislike = (TextView) convertView.findViewById(R.id.dislike);
            ImageView bigLike = (ImageView) convertView.findViewById(R.id.big_like);
            final ImageView myFavourite = (ImageView) convertView.findViewById(R.id.my_favourite);


            // getting movie data for the row
            MyFavouritesObject m = myFavouritesObjects.get(position);

            // thumbnail image
            thumbNail.setImageUrl(m.getPicUrl(), imageLoader);

            // title
            title.setText(m.getTitle());

            // rating
            rating.setText(m.getAverageScore());

            /*if(Float.valueOf(m.getAverageScore()) < 1.7)
                bigLike.setImageResource(R.drawable.mascot_send_comment);
            else if(Float.valueOf(m.getAverageScore()) >= 1.7 && Float.valueOf(m.getAverageScore()) <= 3.3)
                bigLike.setImageResource(R.drawable.mascot_smile_comment);
            else if(Float.valueOf(m.getAverageScore()) > 3.3)
                bigLike.setImageResource(R.drawable.mascot_happy_comment);*/

            //price_range
            price_range.setText(m.getPrice());

           //category
            category.setText(m.getCategory());

            //like
            like.setText(m.getLike());

            //Dislike
            dislike.setText(m.getDislike());

            //Fair
            fair.setText(m.getFair());

            myFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK);
                    AlertDialog dialog;
                    ab.setTitle(R.string.my_favourite_list_delete_warning);
                    ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myfavouriteDB = new CompanySQLiteHandler(activity);
                            myfavouriteDB.deleteMyFavourite(Integer.parseInt(myFavouritesObjects.get(position).entId));
                            myFavouritesObjects.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // what ever you want to do with No option.
                            dialog.dismiss();
                        }
                    });
                    dialog = ab.create();
                    dialog.show();
                }
            });


            return convertView;
        }

    }

