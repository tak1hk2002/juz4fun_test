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
import android.widget.Toast;

import com.company.damonday.CompanyInfo.CompanySQLiteHandler;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;
import com.company.damonday.Search.CompanyObject;
import com.company.damonday.Search.search_adapter;
import com.company.damonday.TestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 21/6/15.
 */
public class MyFavourites extends Fragment {
    private CompanySQLiteHandler db;
    List<MyFavouritesObject> myFavouritesObjects = new ArrayList<>();

    private ListView listView;
    private MyFavourites_adapter adapter;
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
        myFavouritesObjects = db.getAll();
        if (myFavouritesObjects.size() == 0) {
            Toast.makeText(getActivity(), "你還沒有收藏，快點去收藏吧！", Toast.LENGTH_LONG).show();
        }

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new MyFavourites_adapter(getActivity(), myFavouritesObjects);
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
                bundle.putString("ent_id", myFavouritesObjects.get(position).getEntId());
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