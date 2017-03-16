package com.company.damonday.Setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lamtaklung on 28/9/15.
 */
public class AboutUs extends Fragment {
    TextView textview;
    String data = "";
    JsonObjectRequest jsonObjReq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TestActivity) getActivity()).showBackButton();
        ((TestActivity) getActivity()).hideMenuButton();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_about_us, container, false);
        //getActivity().getActionBar().setTitle(R.string.about_us);
        getActivity().setTitle(R.string.about_us);
        makeJsonArrayRequest();
        textview = (TextView) view.findViewById(R.id.textview);
        // textview.setText(data);
        Log.d("JSON1", data);
        return view;
    }

    private void makeJsonArrayRequest() {

        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_SETTING_ABOUT_US, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("JSON", response.toString());


                try {


                    // loop through each json object
                    int status = response.getInt("status");


                    if (status == 1) {

                        data = response.getString("data");
                        Log.d("JSON", data);
                        //textview= (TextView)view.findViewById(R.id.textview);
                        textview.setText(data);

//
//                        for (int i = 0; i < rank.length(); i++) {
//                            CompanyInfo companyInfo = new CompanyInfo();
//                            JSONObject company = (JSONObject) rank
//                                    .get(i);
//                            companyInfo.setTitle(company.getString("name"));
//                            companyInfo.setUrl(company.getString("cover_image"));
//                            companyInfo.setEnt_id(company.getInt("ID"));
//
//                            companyInfoItems.add(companyInfo);
//
//                        }
                    } else if (status == 0){
                        String errorMsg = response.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_SHORT).show();
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

                //  adapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            Log.d("onStop", "About Us requests are all cancelled");
        }
    }


}
