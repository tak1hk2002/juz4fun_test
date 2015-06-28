package com.company.damonday.Ranking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.R;
import com.company.damonday.function.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import function.GetPreviousObject;


/**
 * Created by LAM on 22/4/2015.
 */

public class Ranking extends Activity {

    // json array response url
    private String urlJsonObj = "http://damonday.tk/api/entertainment/rank/";

    private static String TAG = Ranking.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;
    private List<CompanyInfo> companyInfoItems = new ArrayList<CompanyInfo>();
    private GridView gridView;
    private MyAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        gridView = (GridView)findViewById(R.id.gridView);
        adapter = new MyAdapter(this, companyInfoItems);
        gridView.setAdapter(adapter);

        //wait for displaying ranking
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);



        makeJsonArrayRequest();


    }



    private void makeJsonArrayRequest() {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            CompanyInfo companyInfo = new CompanyInfo();
                            // loop through each json object
                            String status = response.getString("status");
                            JSONArray rank = response.getJSONArray("data");


                            for (int i = 0; i < rank.length(); i++) {

                                JSONObject company = (JSONObject) rank
                                        .get(i);
                                Log.d("company",company.toString());
                                companyInfo.setTitle(company.getString("name"));
                                companyInfo.setUrl(company.getString("cover_image"));
                                companyInfo.setEnt_id(company.getInt("ent_id"));

                                companyInfoItems.add(companyInfo);

                            }


                        } catch (JSONException e) {
                            Log.d("error", "error");
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private class Item
    {
        final String name;
        final String url;
        final int ent_id;

        Item(String name, String url, int ent_id)
        {
            this.name = name;
            this.url = url;
            this.ent_id = ent_id;
        }


    }
}
