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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.MapsActivity;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String urlJsonObj, compayName;
    private String entId;
    private ArrayList<String> companyInfo = new ArrayList<String>();
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private APIConfig ranking;
    private int latitude, longitude;


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
            R.drawable.tree1,
            R.drawable.tree1,
            R.drawable.tree1

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //get id from previous page
        entId = getArguments().getString("ent_Id");
        Log.d("entId3", entId);
        //new object for Api url
        ranking = new APIConfig(entId);

        makeJsonArrayRequest();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //makeJsonArrayRequest();
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.view_companyinfo, container, false);

        listView = (ListView)v.findViewById(R.id.listView_Company);
        simpleAdapter = new SimpleAdapter(getActivity(),
                items, R.layout.view_companyinfo_simpleadapter, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        listView.setAdapter(simpleAdapter);
        setListViewHeightBasedOnChildren(listView);
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
                        GetPreviousObject passedObject = new GetPreviousObject(-1, 22.2855200, 114.1576900, compayName
                        );
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
                /*Fragment newFragment = new Fragment_ViewCompany();

                FragmentTransaction ft =
                        getActivity().getSupportFragmentManager().beginTransaction();
                newFragment.setArguments(args);

                ft.replace(R.id.realtabcontent, newFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                ft.addToBackStack(null);
                ft.commit();*/
            }

        });


        return v;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        simpleAdapter.notifyDataSetChanged();
    }


    //doulbe scrollView
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        SimpleAdapter listAdapter = (SimpleAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    private void makeJsonArrayRequest() {
        //showpDialog();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ranking.getUrlDetail(), null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("json", response.toString());

                try {

                    ArrayList<String> info = new ArrayList<String>();

                    // loop through each json object
                    String status = response.getString("status");
                    JSONObject companyInfo = response.getJSONObject("data");

                    if (status.equals("success")) {
                        compayName = companyInfo.getString("name");
                        info.add(companyInfo.getString("description"));
                        info.add(companyInfo.getString("address"));
                        info.add(companyInfo.getString("contact_number"));
                        info.add(companyInfo.getString("price"));
                        info.add(companyInfo.getString("preferred_transport"));
                        info.add(companyInfo.getString("business_hour"));
                        info.add("Dummy text");
                        info.add("Dummy text");
                        info.add("Dummy text");
                        info.add("Dummy text");
                        info.add("Dummy text");
                        info.add("Dummy text");




                    /*info.add(companyInfo.getString("ID"));
                    info.add(companyInfo.getString("name"));
                    info.add(companyInfo.getString("hit_rate"));*/


                        for (int i = 0; i < image.length; i++) {
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("image", image[i]);
                            item.put("text", info.get(i));
                            items.add(item);
                        }
                    }else{
                        String errorMsg = companyInfo.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }





                } catch (JSONException e) {
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                //hidepDialog();


                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                setListViewHeightBasedOnChildren(listView);
                simpleAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

