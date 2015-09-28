package com.company.damonday.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.company.damonday.LatestComment.latestcommentvolley;
import com.company.damonday.Search.search;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;


/**
 * Created by tom on 21/6/15.
 */
public class search extends Fragment {

    private Button button_search;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        button_search =(Button)view.findViewById(R.id.button_search);
        button_search.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {

                Bundle bundle = new Bundle();
//                bundle.putString("ent_id", Integer.toString(companyInfoItems.get(position).getEnt_id()));
 //              fragmentTabs_try = new FragmentTabs_try();
//                fragmentTabs_try.setArguments(bundle);
                latestcommentvolley   latestcommentvolley_fragment=new latestcommentvolley();

                FragmentManager fragmentManager = getFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search"));
                fragmentTransaction.add(R.id.frame_container, latestcommentvolley_fragment, "search_result").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

        });


        return view;
    }
}
