package com.company.damonday.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;
import com.company.damonday.Framework.ImageList.ImageInfo;
import com.company.damonday.Framework.ImageList.ImageList_CustomListAdapter;
import com.company.damonday.Search.FastSearch;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lamtaklung on 3/8/15.
 */
public class Home extends Fragment {

    private static String TAG = Home.class.getSimpleName();
    // Progress dialog
    private List<ImageInfo> imageInfoItems = new ArrayList<ImageInfo>();
    private GridView gridView;
    private TextView noHome;
    private ImageList_CustomListAdapter adapter;
    private FragmentTabs_try fragmentTabs_try;
    private LinearLayout searchview;
    private JsonObjectRequest jsonObjReq;
    //private SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().show();

        makeJsonArrayRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //init view
        View view = inflater.inflate(R.layout.home, container, false);
        searchview = (LinearLayout) view.findViewById(R.id.searchView);

        //search =(SearchView)view.findViewById(R.id.search);
        gridView = (GridView) view.findViewById(R.id.gridView);
        noHome = (TextView) view.findViewById(R.id.no_home);

        noHome.setText(R.string.main_no_home);

        adapter = new ImageList_CustomListAdapter(getActivity(), imageInfoItems, true);
        gridView.setAdapter(adapter);
        getActivity().setTitle(R.string.home);

        //getActivity().getActionBar().setTitle(R.string.home);
        //System.out.println("ordertest2");
        ((TestActivity) getActivity()).hideBackButton();             //very important tomc 23/4/2016
        FragmentManager fragmentManager = getFragmentManager();
        System.out.println("homeOnCreateView:" + fragmentManager.getBackStackEntryCount());
        //search.setIconified(false);
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //((TestActivity) getActivity()).showBackButton();
                //((TestActivity) getActivity()).hideMenuButton();
                //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                Fragment fragment_search_fast = new FastSearch();
                FragmentManager fragmentManager = getFragmentManager();
                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);   //important to clear repeat fragment in the stack //tomc 24/1/2016
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.hide(getFragmentManager().findFragmentByTag("home"));
                fragmentTransaction.replace(R.id.frame_container, fragment_search_fast, "search_fast").addToBackStack("main");  //tomc 24/1/2016
                //fragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.frame_container));
                //fragmentTransaction.replace(R.id.frame_container, fragment_search_fast, "search_fast").addToBackStack("main");
                //fragmentTransaction.add(R.id.frame_container, fragment_search_fast, "search_fast").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                System.out.println("time_grid:" + fragmentManager.getBackStackEntryCount());
                //pass the following object to next activity
                /*Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);*/
                //((TestActivity) getActivity()).showBackButton();
                // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", Integer.toString(imageInfoItems.get(position).getEntID()));
                fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);

                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);   //important to clear repeat fragment in the stack //tomc 24/1/2016
                //FragmentManager fragmentManager = getFragmentManager();
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("home"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                System.out.println("time_grid_end:" + fragmentManager.getBackStackEntryCount());

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
    }


    private void makeJsonArrayRequest() {

        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_HOME, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // loop through each json object
                    int status = response.getInt("status");

                    if (status == 1) {
                        JSONArray rank = response.getJSONArray("data");
                        if(rank.length() > 0) {
                            noHome.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < rank.length(); i++) {
                                ImageInfo imageInfo = new ImageInfo();
                                JSONObject company = (JSONObject) rank
                                        .get(i);
                                imageInfo.setTitle(company.getString("name"));
                                imageInfo.setUrl(company.getString("cover_image"));
                                imageInfo.setCompany(company.getString("company_name"));
                                imageInfo.setEntID(company.getInt("id"));
                                imageInfoItems.add(imageInfo);

                            }
                        }
                        else{
                            noHome.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);
                        }
                    } else if (status == 0) {
                        String errorMsg = response.getString("msg");
                        Log.d("error1", "error1");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
            Log.d("onStop", "home requests are all cancelled");
        }
    }

}
