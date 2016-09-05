package com.company.damonday.Search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by tom on 21/6/15.
 */
public class search extends Fragment {

    //private Spinner spinnerPrice, spinner_lagre_district, spinner_district, spinner_category;
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
    private HashMap<String, Integer> hash_Price = new HashMap<String, Integer>();
    private HashMap<String, Integer> hash_large_district = new HashMap<String, Integer>();
    //private HashMap<String, String> hash_district = new HashMap<String, String>();
    private HashMap<String, String> hash_area_all = new HashMap<String, String>();
    private HashMap<String, String> hash_HK_Island =new HashMap<>();
    private HashMap<String, Object> hash_area = new HashMap<>();
    private String price_id;
    private String district_id;
    private String large_district_id;
    private String category_id;

    private ListView listView;
    private Button button_search;
    private Button button_reset;
    private ProgressImage pDialog;
    private View view;
    private JsonObjectRequest jsonObjReq;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private Map<String, Integer> mapExpense = new HashMap<String, Integer>();
    private String[] title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getResources().getStringArray(R.array.advancedSearch_title);
        for(int i = 0; i < title.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", title[i]);
            item.put("info", "");
            items.add(item);
        }


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        button_search = (Button) view.findViewById(R.id.button_search);
        button_reset = (Button) view.findViewById(R.id.button_reset);

        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items,
                R.layout.view_companywritecomment_list, new String[] {"title", "info"}, new int[] {R.id.title, R.id.info});
        listView.setAdapter(simpleAdapter);

        array_district.add("全選");
        //arrayPrice.add("全選");
        // array_category.add("全選");
        array_area_all.add("全選");
        array_Islands_District.add("全選");
        array_New_Territories.add("全選");
        array_Kowloon.add("全選");
        array_HK_Island.add("全選");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
                AlertDialog dialog;

                final ImageView imgIndicator = (ImageView) view.findViewById(R.id.indicator);
                final TextView txtInfo = (TextView) view.findViewById(R.id.info);


                //get the dialog content
                String dialogDistrict = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogCat = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogExpense = items.get(position).get("info").toString().trim();

                final String[] area = {""};
                final String[] district = {""};
                final String[] expense = {""};

                final int[] selectedArea = {-1};
                final int[] selectedDistrict = {-1};
                final int[] selectedExpense = {-1};


                if (dialogDistrict.isEmpty()) {
                    district[0] = "";
                } else {
                    district[0] = dialogDistrict;
                    if (hash_large_district.get(dialogDistrict) != null)
                        selectedDistrict[0] = hash_large_district.get(dialogDistrict);
                }

                if (dialogExpense.isEmpty()) {
                    expense[0] = "";
                } else {
                    expense[0] = dialogExpense;
                    if (hash_Price.get(dialogExpense) != null)
                        selectedExpense[0] = hash_Price.get(dialogExpense);
                }

                switch (position) {
                    //Area
                    case 0:
                        //get the dialog content
                        final String dialogArea = items.get(position).get("info").toString().trim();

                        if (dialogArea.isEmpty()) {
                            area[0] = "";
                        } else {
                            area[0] = dialogArea;
                            if (hash_large_district.get(dialogArea) != null)
                                selectedArea[0] = hash_large_district.get(dialogArea);
                        }

                        ab.setTitle(title[position]);
                        //setSingleChiceItems cannot support List of arrayList, so I need to convert it to array
                        ab.setSingleChoiceItems(array_lage_district.toArray(new String[array_lage_district.size()]), selectedArea[0], new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        area[0] = array_lage_district.get(which);
                                        selectedArea[0] = which;
                                        break;
                                    case 1:
                                        area[0] = array_lage_district.get(which);
                                        selectedArea[0] = which;
                                        break;
                                    case 2:
                                        area[0] = array_lage_district.get(which);
                                        selectedArea[0] = which;
                                        break;
                                    case 3:
                                        area[0] = array_lage_district.get(which);
                                        selectedArea[0] = which;
                                        break;
                                    case 4:
                                        area[0] = array_lage_district.get(which);
                                        selectedArea[0] = which;
                                        break;
                                }

                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (area[0].isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }

                                //update the district list after selecting area
                                System.out.println(area[0]);
                                if (area[0].equals("全選")) {
                                    array_district = array_area_all;
                                    Log.d("tom", "all");
                                }
                                if (area[0].equals("香港島")) {
                                    array_district = array_HK_Island;
                                    Log.d("tom", "HK_island");
                                }

                                if (area[0].equals("九龍")) {
                                    array_district = array_Kowloon;
                                    Log.d("tom", "kowloon");
                                }

                                if (area[0].equals("新界")) {
                                    array_district = array_New_Territories;
                                    Log.d("tom", "NT");
                                }
                                if (area[0].equals("離島")) {
                                    array_district = array_Islands_District;
                                    Log.d("tom", "island");
                                }


                                items.get(position).put("info", area[0]);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();


                        break;
                    //District
                    case 1:
                        ab.setTitle(title[position]);
                        //setSingleChiceItems cannot support List of arrayList, so I need to convert it to array
                        ab.setSingleChoiceItems(array_district.toArray(new String[array_district.size()]), -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        district[0] = array_district.get(which);
                                        selectedDistrict[0] = which;
                                        break;
                                    case 1:
                                        district[0] = array_district.get(which);
                                        selectedDistrict[0] = which;
                                        break;
                                    case 2:
                                        district[0] = array_district.get(which);
                                        selectedDistrict[0] = which;
                                        break;
                                    case 3:
                                        district[0] = array_district.get(which);
                                        selectedDistrict[0] = which;
                                        break;
                                    case 4:
                                        district[0] = array_district.get(which);
                                        selectedDistrict[0] = which;
                                        break;
                                }

                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (district[0].isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", district[0]);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        break;
                    //expense
                    case 3:
                        ab.setTitle(title[position]);
                        //setSingleChiceItems cannot support List of arrayList, so I need to convert it to array
                        ab.setSingleChoiceItems(arrayPrice.toArray(new String[arrayPrice.size()]), selectedExpense[0], new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 1:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 2:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 3:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 4:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                }

                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (expense[0].isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", expense[0]);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        break;
                }
            }
        });


        makeJsonArrayRequest();




        /*//選中時
        spinner_lagre_district.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(), "您選擇" + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());

                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                // ((TextView) adapterView.getChildAt(0)).setTextSize(5);


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
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
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
                //  ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                Log.d("getSelectedItem:", adapterView.getSelectedItem().toString());
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
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
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
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
        });*/



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

                ((TestActivity) getActivity()).showBackButton();        //tomc 7/8/2016 actionbar button
                ((TestActivity) getActivity()).hideMenuButton();        //tomc 7/8/2016 actionbar button
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // FragmentManager fragmentManager = getFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search"));
                fragmentTransaction.add(R.id.frame_container, search_result_fragment, "search_result").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

        });


        button_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

            }

        });


        return view;
    }


    //從server 拿資料
    private void makeJsonArrayRequest() {


        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
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


                        arrayPrice.add("全選");               //solve text color bug      2016/6/23 tomc
                        hash_Price.put("全選", 0);            //hard code
                        array_category.add("全選");           //solve text color bug     2016/6/23 tomc
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
                            hash_Price.put(name, i);
                        }

                        for (int i = 1; i <= district.length(); i++) {

                            JSONObject districtDetail = district.getJSONObject(String.valueOf(i));
                            //get distrist
                            String district_name = districtDetail.getString("district_name");
                            array_lage_district.add(district_name);
                            hash_large_district.put(district_name, i-1);
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
                                        hash_HK_Island.put(area_name, area_id);
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


//                    ((TextView) adapterPrice.getChildAt(0)).setTextColor(Color.WHITE);


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
                        R.string.connection_server_warning, Toast.LENGTH_SHORT).show();
                //hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (jsonObjReq != null)  {
            jsonObjReq.cancel();
            Log.d("onStop", "Search requests are all cancelled");
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.d("search", "resume");
//        // Set title
//        getActivity()
//                .setTitle(R.string.advance_search);
//    }
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        Log.d("search", "setUserVisibleHint");
//        if(isVisibleToUser) {
//            // Set title
//            getActivity()
//                    .setTitle(R.string.advance_search);
//        }
//    }


}



