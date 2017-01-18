
package com.company.damonday.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnCloseListener;
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
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.Home.Home;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;


public class FastSearch extends Fragment {
    private View view;
    private View myFragmentView;
    private SearchView search;
    private Typeface type;
    private ListView searchResults;
    private ProgressImage pDialog;
    private FastSearchAdapter adapter;
    private StringRequest strReq;


    //This arraylist will have data as pulled from server. This will keep cumulating.
    //ArrayList<search_fast_model> productResults = new ArrayList<search_fast_model>();
    //Based on the search string, only filtered products will be moved here from productResults
    ArrayList<FastSearchModel> filteredProductResults = new ArrayList<FastSearchModel>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pDialog = new ProgressImage(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get the context of the HomeScreen Activity
        // final HomeScreen activity = (HomeScreen) getActivity();
        view = inflater.inflate(R.layout.search, container, false);     //tomc
        // getActivity().getActionBar().setTitle(R.string.advance_search);     //tomc
        getActivity().setTitle(R.string.fast_search);     //tomc
        //define a typeface for formatting text fields and listview.
        final Fragment Home = new Home();
        FragmentManager fragmentManager = getFragmentManager();
        //     type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/book.TTF");
        myFragmentView = inflater.inflate(R.layout.search_fast, container, false);
        search = (SearchView) myFragmentView.findViewById(R.id.searchView1);
        search.setQueryHint("搵野玩");                 //tomc 16/4/2016
        int searchPlateId = search.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = search.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundResource(R.color.font_white);
        }

        searchResults = (ListView) myFragmentView.findViewById(R.id.listview_search);
        search.setIconified(false);
        //this part of the code is to handle the situation when user enters any search criteria, how should the
        //application behave?

        search.setOnCloseListener(new OnCloseListener() {
            @Override
            public boolean onClose() {

                ((TestActivity) getActivity()).onBackPressed();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search_fast"));
//                fragmentTransaction.add(R.id.frame_container, Home, "Home").addToBackStack(null);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.commit();
//                System.out.println("time4_home:" + fragmentManager.getBackStackEntryCount());
                //   getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);

                return false;


            }
        });
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                // Toast.makeText(getActivity(), String.valueOf(hasFocus), Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("newText length: "+newText.length());
                if (newText.length() >= 1) {
                    Log.d("arraylen1", String.valueOf(filteredProductResults.size()));
                    searchResults.setVisibility(myFragmentView.VISIBLE);
                    Log.d("keyword", newText);

                    submitting(newText);

                    //myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(newText);
                } else {

                    //searchResults.setVisibility(myFragmentView.INVISIBLE);
                    Log.d("arraylen2", String.valueOf(filteredProductResults.size()));
                    searchResults.setAdapter(null);
                    adapter.notifyDataSetChanged();

                }


                return true;
            }

        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //pass the following object to next activity
                /*Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);*/

                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", filteredProductResults.get(position).getUser_id());
                Fragment fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);


                FragmentManager fragmentManager = getFragmentManager();
                System.out.println("time3" + fragmentManager.getBackStackEntryCount());
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.frame_container));
                //fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search_fast"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                //System.out.println("time4" + fragmentManager.getBackStackEntryCount());

            }
        });


        return myFragmentView;
    }

    //this filters products from productResults and copies to filteredProductResults based on search text

//    public void filterProductArray(String newText) {
//
//        String pName;
//
//        filteredProductResults.clear();
//        for (int i = 0; i < productResults.size(); i++) {
//            pName = productResults.get(i).getTitle().toLowerCase();
//            if (pName.contains(newText.toLowerCase()))
//            // ||
//            //  productResults.get(i).getProductBarcode().contains(newText))
//            {
//                filteredProductResults.add(productResults.get(i));
//
//            }
//        }
//
//    }


    private void submitting(final String keyword) {
        // Tag used to cancel the request
        String tag_string_req = "req_fast_search";

//        pDialog.setMessage("提交中 ...");
        //showDialog();

        //new object for Api url
        APIConfig apiConfig = new APIConfig(keyword);

        strReq = new StringRequest(Request.Method.GET,
                apiConfig.getUrlFastSearch(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("FeedBack", "Login Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int status = jObj.getInt("status");

                    // Check for error node in json
                    if (status == 1) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);
                        JSONArray jArray = jObj.getJSONArray("data");
                        filteredProductResults.clear();
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject oneObject = jArray.getJSONObject(i);

                            String name = oneObject.getString("name");
                            String ID = oneObject.getString("id");
                            String company = oneObject.getString("company_name");


                            FastSearchModel model = new FastSearchModel();
                            model.setTitle(name);
                            model.setUser_id(ID);
                            model.setCompanyName(company);


                            // adding movie to movies array
                            // productResults.add(model);  filteredProductResults.clear();
                            filteredProductResults.add(model);
                        }


                    } else if (status == 0) {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                adapter = new FastSearchAdapter(getActivity(), filteredProductResults);

                searchResults.setAdapter(adapter);

                //adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FastSearch", "Search Error: " + error.getMessage());
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
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                // params.put("category_id", category_id);
                // params.put("district_id", district_id);
                // params.put("large_district_id", large_district_id);
                params.put("keyword", keyword);

                return params;
            }

        };
        // Adding request to request queue

        Log.d("keyword", keyword);
        Log.d("strReq", strReq.toString());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (strReq != null)  {
            strReq.cancel();
            Log.d("onStop", "Search requests are all cancelled");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //hideDialog();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }



    //
}
