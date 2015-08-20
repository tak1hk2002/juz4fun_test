package com.company.damonday.Ranking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.CompanyInfo.FragmentTabs;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
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


        //wait for displaying ranking
        //pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Loading...");
        //pDialog.show();



        makeJsonArrayRequest();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(Ranking.this, "You Clicked " + companyInfoItems.get(position).getUrl(), Toast.LENGTH_LONG).show();

                //pass the following object to next activity
                Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);
            }
        });


    }


    @Override
    public void onStart(){
        super.onStart();
        adapter = new MyAdapter(this, companyInfoItems);
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
                                Toast.makeText(getApplicationContext(),
                                        errorMsg,
                                        Toast.LENGTH_LONG).show();
                            }





                        } catch (JSONException e) {
                            Log.d("error", "error");
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

}
