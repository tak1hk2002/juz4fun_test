package com.company.damonday.Ranking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.FragmentTabs;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;
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
public class Ranking_try extends Fragment {

    // json array response url
    private String urlJsonObj = "http://damonday.tk/api/entertainment/rank/";

    private static String TAG = Ranking.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;
    private List<CompanyInfo> companyInfoItems = new ArrayList<CompanyInfo>();
    private GridView gridView;
    private MyAdapter adapter;
    private FragmentTabs_try fragmentTabs_try;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //init view
        View view = inflater.inflate(R.layout.ranking, container, false);

        gridView = (GridView)view.findViewById(R.id.gridView);


        makeJsonArrayRequest();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //pass the following object to next activity
                /*Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);*/

                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", Integer.toString(companyInfoItems.get(position).getEnt_id()));
                fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);


                FragmentManager fragmentManager = getFragmentManager();
               // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("ranking"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

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
                APIConfig.URL_RANKING, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());


                try {


                    // loop through each json object
                    String status = response.getString("status");
                    JSONArray rank = response.getJSONArray("data");

                    if(status.equals("success")) {

                        for (int i = 0; i < rank.length(); i++) {
                            CompanyInfo companyInfo = new CompanyInfo();
                            JSONObject company = (JSONObject) rank
                                    .get(i);
                            companyInfo.setTitle(company.getString("name"));
                            companyInfo.setUrl(company.getString("cover_image"));
                            companyInfo.setEnt_id(company.getInt("ID"));

                            companyInfoItems.add(companyInfo);

                        }
                    }else{
                        String errorMsg = response.getString("msg");
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
                //hidePDialog();
                // notifying list adapter about data changes
                // so that it renders the list view with updated data

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
