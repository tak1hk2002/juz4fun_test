package com.company.damonday.NewFoundCompany;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.damonday.R;

/**
 * Created by lamtaklung on 28/9/15.
 */
public class NewFoundCompany extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);
        //hi



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newfound, container, false);

        return view;
    }

}
