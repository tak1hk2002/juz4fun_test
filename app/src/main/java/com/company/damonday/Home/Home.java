package com.company.damonday.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;
import com.company.damonday.Ranking.CompanyInfo;
import com.company.damonday.Ranking.MyAdapter;
import com.company.damonday.Ranking.Ranking;
import com.company.damonday.Search.search_fast;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lamtaklung on 3/8/15.
 */
public class Home extends Fragment {

    // json array response url
   // private String urlJsonObj = "http://damonday.tk/api/entertainment/home/";

    private static String TAG = Ranking.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;
    private List<CompanyInfo> companyInfoItems = new ArrayList<CompanyInfo>();
    private GridView gridView;
    private MyAdapter adapter;
    private FragmentTabs_try fragmentTabs_try;
    private View searchview;
    private SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //init view
        View view = inflater.inflate(R.layout.home, container, false);
        searchview = view.findViewById(R.id.searchView);




        search =(SearchView)view.findViewById(R.id.search);
        gridView = (GridView) view.findViewById(R.id.gridView);
        getActivity().getActionBar().setTitle(R.string.home);

        makeJsonArrayRequest();


        FragmentManager fragmentManager = getFragmentManager();
        System.out.println("time0:" + fragmentManager.getBackStackEntryCount());
        //search.setIconified(false);
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // Intent i = new Intent(MainActivity.this, Ranking.class);
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
               Fragment fragment_search_fast = new search_fast();



                FragmentManager fragmentManager = getFragmentManager();

                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("home"));

                        //fragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.frame_container));
                        //fragmentTransaction.replace(R.id.frame_container, fragment_search_fast, "search_fast").addToBackStack("main");
                fragmentTransaction.add(R.id.frame_container, fragment_search_fast, "search_fast").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();


            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                FragmentManager fragmentManager = getFragmentManager();
                System.out.println("time_grid:"+fragmentManager.getBackStackEntryCount());
                //pass the following object to next activity
                /*Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);*/
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", Integer.toString(companyInfoItems.get(position).getEnt_id()));
                fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);


                //FragmentManager fragmentManager = getFragmentManager();
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("home"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                System.out.println("time_grid_end:"+fragmentManager.getBackStackEntryCount());

            }
        });


        /*fragmentTabs_try.getView().setFocusableInTouchMode(true);
        fragmentTabs_try.getView().requestFocus();
        fragmentTabs_try.getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    return true;
                }
                return false;
            }
        } );*/

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MyAdapter(getActivity(), companyInfoItems);
        gridView.setAdapter(adapter);
    }


    private void makeJsonArrayRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_HOME, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());


                try {


                    // loop through each json object
                    String status = response.getString("status");
                    JSONArray rank = response.getJSONArray("data");

                    if (status.equals("success")) {

                        for (int i = 0; i < rank.length(); i++) {
                            CompanyInfo companyInfo = new CompanyInfo();
                            JSONObject company = (JSONObject) rank
                                    .get(i);
                            // companyInfo.setTitle(company.getString("name"));
                            companyInfo.setUrl(company.getString("cover_image"));
                            companyInfo.setEnt_id(company.getInt("ID"));

                            companyInfoItems.add(companyInfo);

                        }
                    } else {
                        String errorMsg = response.getString("msg");
                        Log.d("error1","error1");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    Log.d("error2", "error2");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                //hidePDialog();
                // notifying list adapter about data changes
                // so that it renders the list view with updated data

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("error3", "error3");
//                VolleyLog.d("volley", "Error: " + error.networkResponse.statusCode);
//                Log.d("error4", error.getMessage());
//                Log.d("error5","Error: " + error.getMessage());
//                Toast.makeText(getActivity(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                VolleyError error2;
//
//                //hidePDialog();
//            }

            public void onErrorResponse(VolleyError error) {

              //  ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());

                Boolean isInternetPresent = ConnectionDetector.isConnectingToInternet(getActivity()); // true or falsew


                Log.d("Network","bool="+isInternetPresent);
               // String body="";
                //get status code here
                //String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
//                if(error.networkResponse.data!=null) {
//                    try {
//                        body = new String(error.networkResponse.data,"UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
                Toast.makeText(getActivity(),
                        "newwork="+isInternetPresent  , Toast.LENGTH_SHORT).show();


                //do stuff with the body...
            }



        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
