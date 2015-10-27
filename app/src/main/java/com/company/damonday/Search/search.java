package com.company.damonday.Search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Home.Home;
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
import java.util.Map;


/**
 * Created by tom on 21/6/15.
 */
public class search extends Fragment {

    private Spinner spinnerPrice, spinner_lagre_district, spinner_district, spinner_category;
    private ArrayAdapter adapterPrice, adapter_lagre_district, adapter_district, adapter_category;
    private ArrayList<String> arrayPrice = new ArrayList<String>();
    private ArrayList<String> array_lage_district = new ArrayList<String>();
    private ArrayList<String> array_district = new ArrayList<String>();
    private ArrayList<String> array_area_all = new ArrayList<String>();
    private ArrayList<String> array_HK_Island = new ArrayList<String>();
    private ArrayList<String> array_Kowloon = new ArrayList<String>();
    private ArrayList<String> array_Islands_District = new ArrayList<String>();
    private ArrayList<String> array_New_Territories = new ArrayList<String>();
    private ArrayList<String> array_category = new ArrayList<String>();
    private HashMap<String, String> hash_category = new HashMap<String, String>();
    private HashMap<String, String> hash_Price = new HashMap<String, String>();
    private HashMap<String, String> hash_large_district = new HashMap<String, String>();
    //private HashMap<String, String> hash_district = new HashMap<String, String>();
    private HashMap<String, String> hash_area_all = new HashMap<String, String>();
    private String price_id;
    private String district_id;
    private String large_district_id;
    private String category_id;

    private Button button_search;
    private ProgressDialog pDialog;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);
        getActivity().getActionBar().setTitle(R.string.advance_search);
        spinnerPrice = (Spinner) view.findViewById(R.id.spinner_price);
        spinner_lagre_district = (Spinner) view.findViewById(R.id.spinner_lagre_district);
        spinner_district = (Spinner) view.findViewById(R.id.spinner_district);
        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);

        button_search = (Button) view.findViewById(R.id.button_search);
        makeJsonArrayRequest();


        adapterPrice = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayPrice);
        adapter_category = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, array_category);
        adapter_district = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, array_district);
        adapter_lagre_district = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, array_lage_district);
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

        arrayPrice.add("全選");
        array_category.add("全選");
        array_area_all.add("全選");
        array_Islands_District.add("全選");
        array_New_Territories.add("全選");
        array_Kowloon.add("全選");
        array_HK_Island.add("全選");
        //選中時
        spinner_lagre_district.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(), "您選擇" + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());
                if (adapterView.getSelectedItem().toString().equals("全選")) {
                    array_district = array_area_all;
                    Log.d("tom", "all");
                }
                if (adapterView.getSelectedItem().toString().equals("香港島")) {
                    array_district = array_HK_Island;
                    Log.d("tom", "HK_island");
                }

                if (adapterView.getSelectedItem().toString().equals("九龍")) {
                    array_district = array_Kowloon;
                    Log.d("tom", "kowloon");
                }

                if (adapterView.getSelectedItem().toString().equals("新界")) {
                    array_district = array_New_Territories;
                    Log.d("tom", "NT");
                }
                if (adapterView.getSelectedItem().toString().equals("離島")) {
                    array_district = array_Islands_District;
                    Log.d("tom", "island");
                }


                if (!adapterView.getSelectedItem().toString().equals("全選")) {
                    large_district_id = hash_large_district.get(adapterView.getSelectedItem().toString());
                } else {
                    large_district_id = "";
                }
                Log.d("large_district_id:", large_district_id);
                adapter_district = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, array_district);
                adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_district.setAdapter(adapter_district);
            }

            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(getActivity(), "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });


//價錢
        spinnerPrice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(), "您選擇" + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());
                //拎番個id
                if (!adapterView.getSelectedItem().toString().equals("全選")) {
                    price_id = hash_Price.get(adapterView.getSelectedItem().toString());
                } else {
                    price_id = "";
                }
                Log.d("_price_id:", price_id);
            }

            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(getActivity(), "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });

        spinner_district.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(), "您選擇" + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());
                //拎番個id
                if (!adapterView.getSelectedItem().toString().equals("全選")) {
                    district_id = hash_area_all.get(adapterView.getSelectedItem().toString());
                } else {
                    district_id = "";
                }
                Log.d("district_id:", district_id);
            }

            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(getActivity(), "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });

        spinner_category.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(), "您選擇" + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());
                //拎番個id
                if (!adapterView.getSelectedItem().toString().equals("全選")) {
                    category_id = hash_category.get(adapterView.getSelectedItem().toString());
                } else {
                    category_id = "";
                }
                Log.d("category_id:", category_id);
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
                //   submitting();

                //傳送數據去下一個fragment
                Bundle bundle = new Bundle();
                bundle.putString("category_id", category_id);
                bundle.putString("district_id", district_id);
                bundle.putString("large_district_id", large_district_id);
                bundle.putString("price_id", price_id);

                search_result search_result_fragment = new search_result();
                search_result_fragment.setArguments(bundle);
                //傳送數據去下一個fragment

                FragmentManager fragmentManager = getFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search"));
                fragmentTransaction.add(R.id.frame_container, search_result_fragment, "search_result").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

        });


        return view;
    }


    //從server 拿資料
    private void makeJsonArrayRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_Advance_Search_criteria, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("JSON", response.toString());

                try {

                    // loop through each json object
                    String status = response.getString("status");


                    JSONObject criteria = response.getJSONObject("data");

                    if (status.equals("success")) {
                        System.out.print("success");


                        JSONArray category = criteria.getJSONArray("category");
                        JSONObject price = criteria.getJSONObject("price_range");
                        JSONObject district = criteria.getJSONObject("district");
                        //JSONArray lagre_distinct = criteria.getJSONArray("category");
                        for (int i = 0; i < category.length(); i++) {

                            JSONObject categoryDetail = (JSONObject) category.get(i);
                            String name = categoryDetail.getString("name");
                            String ID = categoryDetail.getString("ID");
                            //  hashPrice.put(name, ID);
                            //arrayPrice = new String[category.length()];
                            array_category.add(name);
                            hash_category.put(name, ID);
                        }
                        for (int i = 1; i <= price.length(); i++) {
                            String name = price.getString(String.valueOf(i));
                            arrayPrice.add(name);
                            hash_Price.put(name, String.valueOf(i));
                        }

                        for (int i = 1; i <= district.length(); i++) {

                            JSONObject districtDetail = district.getJSONObject(String.valueOf(i));
                            //get distrist
                            String district_name = districtDetail.getString("district_name");
                            array_lage_district.add(district_name);
                            hash_large_district.put(district_name, String.valueOf(i));
                            //get area array
                            try {
                                JSONArray area = districtDetail.getJSONArray("area");
                                for (int j = 0; j < area.length(); j++) {
                                    JSONObject areaDetail = (JSONObject) area.get(j);
                                    String area_name = areaDetail.getString("area_name");
                                    String area_id = areaDetail.getString("area_id");
                                    array_district.add(area_name);
                                    // array_area_all.add(area_name);
                                    hash_area_all.put(area_name, area_id);
                                    if (district_name.equals("香港島")) {
                                        array_HK_Island.add(area_name);
                                    }
                                    if (district_name.equals("九龍")) {
                                        array_Kowloon.add(area_name);
                                    }
                                    if (district_name.equals("新界")) {
                                        array_New_Territories.add(area_name);
                                    }
                                    if (district_name.equals("離島")) {
                                        array_Islands_District.add(area_name);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.d("tom", "the first term全選");
                            }

                        }


                    } else {
                        String errorMsg = response.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }


                    adapterPrice.notifyDataSetChanged();
                    adapter_district.notifyDataSetChanged();
                    adapter_lagre_district.notifyDataSetChanged();
                    adapter_category.notifyDataSetChanged();


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


//    //交資料上server
//    private void submitting(final String category_id, final String district_id, final String large_district_id, final String price_id) {
//        // Tag used to cancel the request
//        String tag_string_req = "req_login";
//
//        pDialog.setMessage("提交中 ...");
//        showDialog();
//
//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                APIConfig.URL_Advance_Search, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("FeedBack", "Login Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    String error = jObj.getString("status");
//
//                    // Check for error node in json
//                    if (error.equals("success")) {
//                        // user successfully logged in
//                        // Create login session
//                        //session.setLogin(true);
//
//
//                        //display message login successfully
//                        AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext());
//                        ab.setTitle(R.string.submit_success);
//                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
////                                Intent in = new Intent(view.getContext(), MainActivity.class);
////                                view.getContext().startActivity(in);
//
//                                Home home_fragment = new Home();
//
//                                FragmentManager fragmentManager = getFragmentManager();
//                                System.out.println(fragmentManager.getBackStackEntryCount());
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("newfound"));
//                                fragmentTransaction.add(R.id.frame_container, home_fragment, "home").addToBackStack(null);
//                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                fragmentTransaction.commit();
//
//
//                            }
//                        });
//                        ab.create().show();
//
//
//                    } else {
//                        // Error in login. Get the error message
//                        JSONObject data = jObj.getJSONObject("data");
//                        String errorMsg = data.getString("msg");
//
//                        Toast.makeText(getActivity(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("FeedBack", "Login Error: " + error.getMessage());
//                Toast.makeText(getActivity(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("company_name", company_name);
//                params.put("company_tel", company_tel);
//                params.put("company_type", company_type);
//                params.put("company_address", company_address);
//                params.put("cost", company_cost);
//                params.put("business_hour", company_business_hour);
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
//
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

}



