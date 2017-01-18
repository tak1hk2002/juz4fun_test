package com.company.damonday.MyFavourites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.company.damonday.CompanyInfo.CompanySQLiteHandler;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.Framework.CompanyList.CompanyListObject;
import com.company.damonday.Framework.CompanyList.CompanyListAdapter;
import com.company.damonday.R;
import com.company.damonday.TestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 21/6/15.
 */
public class MyFavourites extends Fragment {
    private CompanySQLiteHandler db;
    List<CompanyListObject> companyListObjects = new ArrayList<>();

    private ListView listView;
    private CompanyListAdapter adapter;
    private FragmentTabs_try fragmentTabs_try;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfavourites, container, false);
        //getActivity().getActionBar().setTitle(R.string.myfavourite);
        getActivity().setTitle(R.string.myfavourite);

        db = new CompanySQLiteHandler(getActivity());
        companyListObjects = db.getAll();


        listView = (ListView) view.findViewById(R.id.list);
        TextView noFavourite = (TextView) view.findViewById(R.id.no_favourite);
        if (companyListObjects.size() > 0) {
            noFavourite.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else {
            listView.setVisibility(View.GONE);
            noFavourite.setVisibility(View.VISIBLE);
        }

        noFavourite.setText(R.string.my_favourite_list_no_favourite);
        adapter = new CompanyListAdapter(getActivity(), companyListObjects, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //pass the following object to next activity
                /*Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);*/

                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", companyListObjects.get(position).getEntId());
                fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);

                ((TestActivity) getActivity()).showBackButton();        //tomc 7/8/2016 actionbar button
                ((TestActivity) getActivity()).hideMenuButton();        //tomc 7/8/2016 actionbar button
                FragmentManager fragmentManager = getFragmentManager();
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("myfavourites"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });


        return view;
    }

}