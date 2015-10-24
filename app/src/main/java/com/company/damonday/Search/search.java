package com.company.damonday.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.LatestComment.latestcommentvolley;
import com.company.damonday.Search.search;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by tom on 21/6/15.
 */
public class search extends Fragment {

    private Spinner spinnerPrice,spinner_lagre_district,spinner_district,spinner_category;
    private ArrayAdapter adapterPrice,adapter_lagre_district,adapter_district,adapter_category;
    private ArrayList<String> arrayPrice=new ArrayList<String>();
    private ArrayList<String> array_lage_district=new ArrayList<String>();
    private ArrayList<String> array_district=new ArrayList<String>();
    private ArrayList<String> array_area_all=new ArrayList<String>();
    private ArrayList<String> array_HK_Island=new ArrayList<String>();
    private ArrayList<String> array_Kowloon=new ArrayList<String>();
    private ArrayList<String> array_Islands_District=new ArrayList<String>();
    private ArrayList<String> array_New_Territories=new ArrayList<String>();
    private ArrayList<String> array_category=new ArrayList<String>();
    private HashMap<String, String> hashPrice = new HashMap<String, String>();

    private Button button_search;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        spinnerPrice = (Spinner) view.findViewById(R.id.spinner_price);
        spinner_lagre_district= (Spinner) view.findViewById(R.id.spinner_lagre_district);
        spinner_district= (Spinner) view.findViewById(R.id.spinner_district);
        spinner_category= (Spinner) view.findViewById(R.id.spinner_category);

        button_search =(Button)view.findViewById(R.id.button_search);
        makeJsonArrayRequest();


        adapterPrice = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,arrayPrice);
        adapter_category = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,array_category);
        adapter_district = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,array_district);
        adapter_lagre_district = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,array_lage_district);
        //設定下拉選單的樣式

        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_lagre_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //spinners ser adapter
        spinnerPrice.setAdapter(adapterPrice);
        spinner_category.setAdapter(adapter_category);
        spinner_district.setAdapter(adapter_district);
        spinner_lagre_district.setAdapter(adapter_lagre_district);

        //選中時
        spinner_lagre_district.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                                       public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                                                           Toast.makeText(getActivity(), "您選擇" + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                                                           Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());
                                                           if(adapterView.getSelectedItem().toString().equals("香港島"))
                                                           {array_district=array_HK_Island;
                                                               Log.d("tom","HK_island");
                                                               //adapter_district = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,array_district);
                                                               //spinner_district.setAdapter(adapter_district);
                                                           }

                                                           if(adapterView.getSelectedItem().toString().equals("九龍"))
                                                           {array_district=array_Kowloon;
                                                               Log.d("tom","kowloon");}

                                                           if(adapterView.getSelectedItem().toString().equals("新界"))
                                                           {array_district=array_New_Territories;
                                                               Log.d("tom","NT");}
                                                           if(adapterView.getSelectedItem().toString().equals("離島"))
                                                           {array_district=array_Islands_District;
                                                               Log.d("tom","island");}
                                                           for (String member:array_district
                                                                ) {
                                                               Log.d("tom_membername:",member);
                                                           }

                                                           adapter_district.notifyDataSetChanged();
                                                           Log.d("test:", "test");

                                                       }
                                                       public void onNothingSelected(AdapterView arg0) {
                                                           Toast.makeText(getActivity(), "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                                                       }
                                                   });



        /*adapterPrice = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,arrayPrice);
        //設定下拉選單的樣式
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(adapterPrice);*/
        //設定項目被選取之後的動作
/*        spinnerPrice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(getActivity(), "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(getActivity(), "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });*/

        button_search.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {

                Bundle bundle = new Bundle();
//                bundle.putString("ent_id", Integer.toString(companyInfoItems.get(position).getEnt_id()));
                //              fragmentTabs_try = new FragmentTabs_try();
//                fragmentTabs_try.setArguments(bundle);
                latestcommentvolley latestcommentvolley_fragment = new latestcommentvolley();

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





    private void makeJsonArrayRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_Search_criteria, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("JSON", response.toString());

                try {

                    // loop through each json object
                    String status = response.getString("status");


                    JSONObject criteria = response.getJSONObject("data");

                    if(status.equals("success")) {
                        System.out.print("success");


                        JSONArray category = criteria.getJSONArray("category");
                        JSONObject price = criteria.getJSONObject("price_range");
                        JSONObject district = criteria.getJSONObject("district");
                        //JSONArray lagre_distinct = criteria.getJSONArray("category");
                        for (int i = 0; i < category.length(); i++) {

                            JSONObject categoryDetail = (JSONObject)category.get(i);
                            String name = categoryDetail.getString("name");
                            String ID = categoryDetail.getString("ID");
                          //  hashPrice.put(name, ID);
                            //arrayPrice = new String[category.length()];
                            array_category.add(name);
                        }
                        for (int i = 1; i <= price.length(); i++) {
                            String name = price.getString(String.valueOf(i));
                            arrayPrice.add(name);
                        }

                        for (int i = 1; i <= district.length(); i++) {

                            JSONObject districtDetail = district.getJSONObject(String.valueOf(i));
                            //get distrist
                            String district_name = districtDetail.getString("district_name");
                            array_lage_district.add(district_name);
                            //get area array
try {
    JSONArray area = districtDetail.getJSONArray("area");
    for (int j=0; j < area.length(); j++) {
        JSONObject areaDetail = (JSONObject) area.get(j);
        String area_name = areaDetail.getString("area_name");
       // Log.d("tom", area_name);
        array_district.add(area_name);
        array_area_all.add(area_name);
        if(district_name.equals("香港島"))
        {array_HK_Island.add(area_name);}
        if(district_name.equals("九龍"))
        {array_Kowloon.add(area_name);}
        if(district_name.equals("新界"))
        {array_New_Territories.add(area_name);}
        if(district_name.equals("離島"))
        {array_Islands_District.add(area_name);}
    }
}
catch (JSONException e){
    Log.d("tom","the first term全選");
}

                        }



                    }else{
                        String errorMsg = response.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }



//                    for(int j=0;j<4;j++){
//                        Log.d("JSON", "tomtomt" + arrayPrice.get(j));
//                    }
                    adapterPrice.notifyDataSetChanged();
                    adapter_district.notifyDataSetChanged();
                    adapter_lagre_district.notifyDataSetChanged();
                    adapter_category.notifyDataSetChanged();
          // adapterPrice = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,new String[]{"紅茶","奶茶","綠茶"});



                } catch (JSONException e) {
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                //hidePDialog();
                // notifying list adapter about data changes
                // so that it renders the list view with updated data

                //  adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("json", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
