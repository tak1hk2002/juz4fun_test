package com.company.damonday.CompanyInfo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.company.damonday.MapsActivity;
import com.company.damonday.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import function.GetPreviousObject;

/**
 * Created by LAM on 20/4/2015.
 */
public class Fragment_ViewCompany extends Fragment {

    private View v;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private int[] image = {
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1

    };
    private String[] CompanyInfo = {
            "九龍油塘高翔苑高安閣",
            "18503",
            "多人射擊",
            "$200 - $300",
            "星期一: 10:00 - 23:00",
            "XXXXXXX",
            "XXXXXXX",
            "XXXXXXX",
            "XXXXXXX",
            "XXXXXXX"

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.view_companyinfo, container, false);
        listView = (ListView)v.findViewById(R.id.listView_Company);
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", image[i]);
            item.put("text", CompanyInfo[i]);
            items.add(item);
        }

        Log.v("getContext2",v.getContext().toString());

        simpleAdapter = new SimpleAdapter(getActivity(),
                items, R.layout.view_companyinfo_simpleadapter, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Bundle args = new Bundle();
                args.putInt("position", position);

                switch (position) {
                    case 0:
                        //pass the object to MapsActivity
                        GetPreviousObject passedObject = new GetPreviousObject(-1, -1, -1);
                        // 啟動地圖元件用的Intent物件
                        Intent intentMap = new Intent(v.getContext(), MapsActivity.class);
                        intentMap.putExtra("LatLng", passedObject);
                        // 啟動地圖元件
                        startActivityForResult(intentMap, 2);
                        break;
                    case 1:

                        //trigger built in phone call function
                        //ACTION_DIAL => modify hardcoded number before making a call
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:29525478"));
                        try {
                            startActivity(phoneIntent);
                            //finish();
                            //Log.i("Finished making a call...", "");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(v.getContext(),
                                    "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                Log.d("position", Integer.toString(position));
                Fragment newFragment = new Fragment_ViewCompany();

                FragmentTransaction ft =
                        getActivity().getSupportFragmentManager().beginTransaction();
                newFragment.setArguments(args);

                ft.replace(R.id.realtabcontent, newFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                ft.addToBackStack(null);
                ft.commit();
            }

        });
        return v;
    }
}

