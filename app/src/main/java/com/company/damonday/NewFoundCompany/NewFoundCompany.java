package com.company.damonday.NewFoundCompany;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Home.Home;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;

import com.company.damonday.R;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lamtaklung on 28/9/15.
 */
public class NewFoundCompany extends Fragment {
    private View view;

    private ProgressImage pDialog;
    EditText EditText_company_name;
    EditText EditText_company_tel;
    EditText EditText_company_type;
    EditText EditText_company_address;
    EditText EditText_cost;
    EditText EditText_business_hour;
    String company_name;
    String company_tel;
    String company_type;
    String company_address;
    String company_cost;
    String company_business_hour;
    Button btn_submit;
    Button btn_reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);
        //hi


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newfound, container, false);
        getActivity().getActionBar().setTitle(R.string.newfound);

        EditText_company_name = (EditText) view.findViewById(R.id.company_name);
        EditText_company_tel = (EditText) view.findViewById(R.id.company_tel);
        EditText_company_type = (EditText) view.findViewById(R.id.company_type);

        EditText_company_address = (EditText) view.findViewById(R.id.company_address);
        EditText_cost = (EditText) view.findViewById(R.id.company_cost);
        EditText_business_hour = (EditText) view.findViewById(R.id.company_business_hour);
        btn_submit = (Button) view.findViewById(R.id.button_submit);
        btn_reset = (Button) view.findViewById(R.id.button_reset);
        pDialog = new ProgressImage(view.getContext());

        btn_submit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {


                company_name = EditText_company_name.getText().toString();
                company_tel = EditText_company_tel.getText().toString();
                company_type = EditText_company_type.getText().toString();
                company_address = EditText_company_address.getText().toString();
                company_cost = EditText_cost.getText().toString();
                company_business_hour = EditText_business_hour.getText().toString();

                submitting(company_name, company_tel, company_type, company_address, company_cost, company_business_hour);


            }
        });


        btn_reset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
               EditText_company_name.setText("");
                EditText_company_tel.setText("");
                EditText_company_type.setText("");
                EditText_company_address.setText("");
                EditText_cost.setText("");
                EditText_business_hour.setText("");




            }
        });


        return view;
    }


    private void submitting(final String company_name, final String company_tel, final String company_type, final String company_address, final String company_cost, final String company_business_hour) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_newfound, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("FeedBack", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.equals("success")) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);


                        //display message login successfully
                        AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext());
                        ab.setTitle(R.string.submit_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                Intent in = new Intent(view.getContext(), MainActivity.class);
//                                view.getContext().startActivity(in);

                                Home home_fragment = new Home();

                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //tomc 31/1/2016  To disable the back button in home
                                //System.out.println(fragmentManager.getBackStackEntryCount());
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("newfound"));
                                fragmentTransaction.add(R.id.frame_container, home_fragment, "home").addToBackStack(null);
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();
                             //   getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);


                            }
                        });
                        ab.create().show();


                    } else {
                        // Error in login. Get the error message
                        JSONObject data = jObj.getJSONObject("data");
                        String errorMsg = data.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FeedBack", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("company_name", company_name);
                params.put("company_tel", company_tel);
                params.put("company_type", company_type);
                params.put("company_address", company_address);
                params.put("cost", company_cost);
                params.put("business_hour", company_business_hour);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}