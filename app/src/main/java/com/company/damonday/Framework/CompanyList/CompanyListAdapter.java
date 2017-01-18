package com.company.damonday.Framework.CompanyList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.company.damonday.CompanyInfo.CompanySQLiteHandler;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import java.util.List;

/**
 * Created by tom on 25/10/15.
 */
public class CompanyListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<CompanyListObject> companyListObjects;
    private CompanySQLiteHandler myfavouriteDB;
    private boolean disableRight;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CompanyListAdapter(Activity activity, List<CompanyListObject> companyListObjects, boolean disableRight) {
        this.activity = activity;
        this.companyListObjects = companyListObjects;
        this.disableRight = disableRight;
    }

    @Override
    public int getCount() {
        return companyListObjects.size();
    }

    @Override
    public Object getItem(int location) {
        return companyListObjects.get(location);
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
        NetworkImageView userIcon = (NetworkImageView) convertView
                .findViewById(R.id.user_icon);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView companyName = (TextView) convertView.findViewById(R.id.company_name);
        TextView rating = (TextView) convertView.findViewById(R.id.average_score);
        TextView rating2 = (TextView) convertView.findViewById(R.id.average_score2);
        TextView price_range = (TextView) convertView.findViewById(R.id.price_range);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView like = (TextView) convertView.findViewById(R.id.like);
        TextView fair = (TextView) convertView.findViewById(R.id.fair);
        TextView dislike = (TextView) convertView.findViewById(R.id.dislike);
        ImageView scoreIcon = (ImageView) convertView.findViewById(R.id.score_icon);
        RelativeLayout right = (RelativeLayout) convertView.findViewById(R.id.right);
        //final ImageView myFavourite = (ImageView) convertView.findViewById(R.id.my_favourite);

        // disappear right hand my favourite icon
        /*if(disableRight)
            right.setVisibility(View.GONE);*/


        // getting movie data for the row
        CompanyListObject m = companyListObjects.get(position);

        // thumbnail image
        userIcon.setImageUrl(m.getPicUrl(), imageLoader);
        userIcon.setDefaultImageResId(R.drawable.pro_pic_default);
        userIcon.setErrorImageResId(R.drawable.mascot_die_pic);

        // title
        title.setText(m.getTitle());

        // company name
        companyName.setText(m.getCompanyName());

        // rating
        rating.setText(m.getAverageScore());

        Log.d("m.getAverageScore()", m.getAverageScore());

        if (Float.valueOf(m.getAverageScore()) == 0.0) {
            scoreIcon.setImageResource(R.drawable.mascot_send_comment);
            rating.setText(R.string.not_has_score_yet);
            rating2.setVisibility(View.GONE);
        } else if (Float.valueOf(m.getAverageScore()) < 2.1)
            scoreIcon.setImageResource(R.drawable.mascot_send_comment);
        else if (Float.valueOf(m.getAverageScore()) >= 2.1 && Float.valueOf(m.getAverageScore()) <= 3.5)
            scoreIcon.setImageResource(R.drawable.mascot_smile_comment);
        else if (Float.valueOf(m.getAverageScore()) > 3.5)
            scoreIcon.setImageResource(R.drawable.mascot_happy_comment);

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

        /*myFavourite.setOnClickListener(new View.OnClickListener() {
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
        });*/


        return convertView;
    }

}

