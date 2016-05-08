package com.company.damonday.CompanyInfo.Fragment.ViewCompany;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.Fragment.ViewCompany.FeeDetail.Fee_Detail;
import com.company.damonday.MapsActivity;
import com.company.damonday.R;
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
    private String entId;
    private ArrayList<String> companyInfo = new ArrayList<String>();
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private APIConfig ranking;
    private double latitude, longitude;
    private List<Integer> showDetailIndicator = Arrays.asList(2, 3, 5);

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
        simpleAdapter = new SimpleAdapter(getActivity(),
                items, R.layout.view_companyinfo_simpleadapter, new String[]{"title", "text", "indicator"},
                new int[]{R.id.title, R.id.text, R.id.indicator});
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
                    case 2:
                        //pass the object to MapsActivity
                        GetPreviousObject passedObject = new GetPreviousObject(-1, latitude, longitude, compayName
                        );
                        // 啟動地圖元件用的Intent物件
                        Intent intentMap = new Intent(v.getContext(), MapsActivity.class);
                        intentMap.putExtra("LatLng", passedObject);
                        // 啟動地圖元件
                        startActivityForResult(intentMap, 2);
                        break;
                    case 3:

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


    //doulbe scrollView
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        SimpleAdapter listAdapter = (SimpleAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < (listAdapter.getCount()); i++)
        {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        /*int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);*/
    }


    private void makeJsonArrayRequest() {
        //showpDialog();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ranking.getUrlDetail(), null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("json", response.toString());

                try {

                    ArrayList<Spanned> info = new ArrayList<Spanned>();

                    // loop through each json object
                    String status = response.getString("status");
                    JSONObject companyInfo = response.getJSONObject("data");

                    if (status.equals("success")) {
                        compayName = companyInfo.getString("name");
                        info.add((Html.fromHtml(companyInfo.getString("description"))));
                        info.add(Html.fromHtml(companyInfo.getString("address")));
                        info.add(Html.fromHtml(companyInfo.getString("contact_number")));
                        //cat
                        JSONArray catList = companyInfo.getJSONArray("cat");
                        String cat = "";
                        for (int i = 0; i < catList.length(); i++){
                            if(!cat.isEmpty())
                                cat += ", ";
                            cat += catList.get(i);
                        }
                        info.add(Html.fromHtml(cat));
                        info.add(Html.fromHtml(companyInfo.getString("price")));
                        info.add(Html.fromHtml(companyInfo.getString("preferred_transport")));
                        info.add(Html.fromHtml(companyInfo.getString("business_hour")));
                        info.add(Html.fromHtml("Dummy text"));
                        info.add(Html.fromHtml("Dummy text"));
                        info.add(Html.fromHtml("Dummy text"));
                        //info.add(Html.fromHtml("Dummy text"));
                        //info.add(Html.fromHtml("Dummy text"));
                        //info.add(Html.fromHtml("Dummy text"));

                        latitude = companyInfo.getDouble("latitude");
                        longitude = companyInfo.getDouble("longitude");




                    /*info.add(companyInfo.getString("ID"));
                    info.add(companyInfo.getString("name"));
                    info.add(companyInfo.getString("hit_rate"));*/


                        for (int i = 0; i < companyInfoCat.length; i++) {
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("title", companyInfoCat[i]);
                            item.put("text", info.get(i));
                            if(showDetailIndicator.contains(i))
                                item.put("indicator", R.drawable.icon_forward_arrow);
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
                //simpleAdapter.notifyDataSetChanged();

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

