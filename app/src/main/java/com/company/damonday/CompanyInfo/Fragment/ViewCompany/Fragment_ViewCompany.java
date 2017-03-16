package com.company.damonday.CompanyInfo.Fragment.ViewCompany;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.Fragment.ViewCompany.FeeDetail.Fee_Detail;
import com.company.damonday.Framework.ScrollView.ScrollView;
import com.company.damonday.MapsActivity;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String entId, companyTel;
    private ArrayList<String> companyInfo = new ArrayList<String>();
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private APIConfig ranking;
    private double latitude, longitude;
    private List<Integer> showDetailIndicator = Arrays.asList();
    private JsonObjectRequest jsonObjReq;
    private ScrollView doubleScroll = new ScrollView();
    private ArrayList<String> info = new ArrayList<String>();
    private Fragment_ViewCompany_CustomListAdapter customAdapter;
    private int storeDesiredWidth = 0;
    private String[] companyInfoCat ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        try {
            //get id from previous page
            entId = getArguments().getString("ent_id");
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "No data loaded", Toast.LENGTH_LONG).show();
        }
        //new object for Api url
        ranking = new APIConfig(entId);
        Log.d("entId", entId);

        //get the company info categories
        companyInfoCat = getResources().getStringArray(R.array.company_info_cat);


        makeJsonArrayRequest();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /*//pass entId to fragment
        Bundle bundle = new Bundle();
        bundle.putString("ent_Id", entId);*/
        //makeJsonArrayRequest();
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.view_companyinfo, container, false);

        listView = (ListView)v.findViewById(R.id.listView_Company);
        /*simpleAdapter = new SimpleAdapter(getActivity(),
                items, R.layout.view_companyinfo_simpleadapter, new String[]{"title", "text", "indicator"},
                new int[]{R.id.title, R.id.text, R.id.indicator});*/

        customAdapter = new Fragment_ViewCompany_CustomListAdapter(getActivity(), info, companyInfoCat, showDetailIndicator);
        listView.setAdapter(customAdapter);
        doubleScroll.setListViewHeightBasedOnChildren(listView);
        //customAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Bundle args = new Bundle();
                args.putInt("position", position);

                switch (position) {
                    //address map
                    case 2:
                        //pass the object to MapsActivity
                        GetPreviousObject passedObject = new GetPreviousObject(-1, latitude, longitude, compayName
                        );
                        // 啟動地圖元件用的Intent物件
                        Intent intentMap = new Intent(getActivity(), MapsActivity.class);
                        intentMap.putExtra("LatLng", passedObject);
                        // 啟動地圖元件
                        startActivityForResult(intentMap, 2);
                        break;
                    //tel
                    case 3:
                        //trigger built in phone call function
                        //ACTION_DIAL => modify hardcoded number before making a call
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + companyTel));
                        try {
                            startActivity(phoneIntent);
                            //finish();
                            //Log.i("Finished making a call...", "");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(),
                                    "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //fee detail
                    case 5:
                        Bundle bundle = new Bundle();
                        bundle.putString("ent_id", entId);
                        Fee_Detail fee_detail = new Fee_Detail();
                        fee_detail.setArguments(bundle);

                /*getFragmentManager().beginTransaction()
                        .add(R.id.frame_container, fragment_ViewCommentDetail)
                        .commit();*/
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        System.out.println(fragmentManager.getBackStackEntryCount());
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("companyDetail"));
                        fragmentTransaction.add(R.id.frame_container, fee_detail, "feeDetail").addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
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
        //simpleAdapter.notifyDataSetChanged();
    }


    //Double scrollView
    public void setListViewHeightBasedOnChildren(ListView listView) {
        Log.d("setListView", "HIHI");
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        System.out.println(storeDesiredWidth);

        if(storeDesiredWidth == 0)
            storeDesiredWidth = desiredWidth;
        else if (storeDesiredWidth < desiredWidth)
            storeDesiredWidth = desiredWidth;
        else
            desiredWidth = storeDesiredWidth;
        System.out.println(desiredWidth);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    private void makeJsonArrayRequest() {
        //showpDialog();

        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ranking.getUrlDetail(), null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("json", response.toString());

                try {

                    // loop through each json object
                    int status = response.getInt("status");

                    if (status == 1) {
                        JSONObject companyInfo = response.getJSONObject("data");

                        latitude = companyInfo.getDouble("latitude");
                        longitude = companyInfo.getDouble("longitude");
                        compayName = companyInfo.getString("company_name");
                        companyTel = companyInfo.getString("contact_number");

                        info.add(companyInfo.getString("company_name"));
                        //cat
                        JSONArray catList = companyInfo.getJSONArray("cat");
                        String cat = "";
                        for (int i = 0; i < catList.length(); i++){
                            if(!cat.isEmpty())
                                cat += ", ";
                            cat += catList.get(i);
                        }
                        info.add(cat);
                        info.add(companyInfo.getString("address"));

                        info.add(companyTel);
                        info.add(companyInfo.getString("price_range"));
                        info.add(companyInfo.getString("business_hour"));
                        info.add(companyInfo.getString("preferred_transport"));

                        info.add(companyInfo.getString("company_email"));
                        info.add(companyInfo.getString("description"));

                    /*info.add(companyInfo.getString("ID"));
                    info.add(companyInfo.getString("name"));
                    info.add(companyInfo.getString("hit_rate"));*/


                        /*for (int i = 0; i < companyInfoCat.length; i++) {
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("title", companyInfoCat[i]);
                            item.put("text", info.get(i));
                            items.add(item);
                        }*/
                    }else if(status == 0){
                        String errorMsg = response.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_SHORT).show();
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
                doubleScroll.setListViewHeightBasedOnChildren(listView);
                customAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = getResources().getString(R.string.connection_fail_warning);
                } else if (error instanceof ServerError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof AuthFailureError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof ParseError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof NoConnectionError) {
                    message = getResources().getString(R.string.connection_fail_warning);
                } else if (error instanceof TimeoutError) {
                    message = getResources().getString(R.string.connection_server_warning);
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (jsonObjReq != null)  {
            jsonObjReq.cancel();
            Log.d("onStop", "Company Detail requests are all cancelled");
        }
    }

}

