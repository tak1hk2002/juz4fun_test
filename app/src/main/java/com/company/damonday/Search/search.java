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

    private Spinner spinnerPrice;
    private ArrayAdapter adapterPrice;
    private String[] arrayPrice;
    private HashMap<String, String> hashPrice = new HashMap<String, String>();

    private Button button_search;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        spinnerPrice = (Spinner) view.findViewById(R.id.spinner);


        button_search =(Button)view.findViewById(R.id.button_search);
        makeJsonArrayRequest();
        Log.d("JSON", "hiyaaaaa");
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
                System.out.print("hi");
                Log.d("JSON", "hi0");
                Log.d("JSON", response.toString());
                Log.d("JSON","hia");
                System.out.print("hi");
                Log.d("JSON", "hib");

                try {
                    Log.d("JSON", "hic");
                    System.out.print("hi");
                    // loop through each json object
                    String status = response.getString("status");
                    System.out.print("hi2");
                    Log.d("JSON", "hic");
                    JSONObject criteria = response.getJSONObject("data");
                    System.out.print("hi3");
                    Log.d("JSON", "hid");
                    if(status.equals("success")) {
                        System.out.print("success");
                        Log.d("JSON", "hie");
//                        data = rank.getString("value");
//                        Log.d("JSON", data);
//                        //textview= (TextView)view.findViewById(R.id.textview);
//                        textview.setText(data);

                        JSONArray category = criteria.getJSONArray("category");
                        System.out.print("category");
                        Log.d("JSON", "hif");
                        for (int i = 0; i < category.length(); i++) {
                            System.out.print("hi");
                            JSONObject categoryDetail = (JSONObject)category.get(i);
                            String name = categoryDetail.getString("name");
                            String ID = categoryDetail.getString("ID");
                            hashPrice.put(name, ID);
                            arrayPrice = new String[category.length()];
                            arrayPrice[i] = name;
                            Log.d("JSON", "hit" + arrayPrice[i]);
                            Log.d("JSON", "hiz" + name);

                        }
                        Log.d("JSON", "hiy");
                        adapterPrice = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,arrayPrice);
                        //設定下拉選單的樣式
                        Log.d("JSON", "hiye");
                        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPrice.setAdapter(adapterPrice);
                        Log.d("JSON", "hiyaa");

//

                    }else{
                        String errorMsg = response.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }

                    //adapterPrice.notifyDataSetChanged();



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
