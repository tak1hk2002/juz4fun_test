package com.company.damonday.CompanyInfo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.company.damonday.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LAM on 20/4/2015.
 */
public class Fragment_ViewCompany extends Fragment {

    private View v;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private int[] image = {
            R.drawable.map_icon,
            R.drawable.map_icon,
            R.drawable.map_icon,
            R.drawable.map_icon,
            R.drawable.map_icon
    };
    private String[] CompanyInfo = {
            "XXXXXXXXXXX",
            "22222222",
            "多人射擊",
            "$200 - $300",
            "星期一: 10:00 - 23:00"
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
        simpleAdapter = new SimpleAdapter(getActivity(),
                items, R.layout.view_companyinfo_simpleadapter, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        listView.setAdapter(simpleAdapter);
        return v;
    }
}

