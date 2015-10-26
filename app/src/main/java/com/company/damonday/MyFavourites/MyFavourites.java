package com.company.damonday.MyFavourites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.damonday.CompanyInfo.CompanySQLiteHandler;
import com.company.damonday.R;

/**
 * Created by tom on 21/6/15.
 */
public class MyFavourites extends Fragment {
    private CompanySQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfavourites, container, false);

        return view;
    }

}