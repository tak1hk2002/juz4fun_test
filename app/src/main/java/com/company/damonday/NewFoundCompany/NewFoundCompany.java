package com.company.damonday.NewFoundCompany;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;

import com.company.damonday.R;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lamtaklung on 28/9/15.
 */
public class NewFoundCompany extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;

    private ProgressImage pDialog;
    EditText EditText_company_name;
    EditText EditText_company_tel;
    EditText EditText_company_type;
    EditText EditText_company_address;
    Spinner EditText_cost;
    EditText EditText_business_hour;
    String company_name;
    String company_tel;
    String company_type;
    String company_address;
    String company_cost;
    String company_business_hour;
    private String priceID;
    private ArrayList<String> array_category = new ArrayList<String>();
    private HashMap<String, String> hashPrice = new HashMap<String, String>();
    private HashMap<String, String> hash_category = new HashMap<String, String>();
    Button btn_submit;
    Button btn_reset;
    LinearLayout linearCat;
    APIConfig optionDetail;
    private ArrayList<String> arrayPrice = new ArrayList<String>();
    private ArrayAdapter adapterPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getOptionDetail();
        view = inflater.inflate(R.layout.newfound, container, false);
        // getActivity().getActionBar().setTitle(R.string.newfound);
        getActivity().setTitle(R.string.newfound);

        EditText_company_name = (EditText) view.findViewById(R.id.company_name);
        EditText_company_tel = (EditText) view.findViewById(R.id.company_tel);
        //EditText_company_type = (EditText) view.findViewById(R.id.company_type);

        EditText_company_address = (EditText) view.findViewById(R.id.company_address);
        EditText_cost = (Spinner) view.findViewById(R.id.company_cost);
        EditText_business_hour = (EditText) view.findViewById(R.id.company_business_hour);
        btn_submit = (Button) view.findViewById(R.id.button_submit);
        btn_reset = (Button) view.findViewById(R.id.button_reset);
        pDialog = new ProgressImage(view.getContext());
        linearCat = (LinearLayout) view.findViewById(R.id.cat_checkbox);

        // Spinner click listener
        EditText_cost.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        adapterPrice = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayPrice);
        // Drop down layout style - list view with radio button
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        EditText_cost.setAdapter(adapterPrice);

        btn_submit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                company_name = EditText_company_name.getText().toString();
                company_tel = EditText_company_tel.getText().toString();
                //company_type = EditText_company_type.getText().toString();
                company_address = EditText_company_address.getText().toString();
                //company_cost = EditText_cost.getText().toString();
                company_business_hour = EditText_business_hour.getText().toString();
                submitting(company_name, company_tel, company_type, company_address, company_cost, company_business_hour);
            }
        });


        btn_reset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText_company_name.setText("");
                EditText_company_tel.setText("");
                //EditText_company_type.setText("");
                EditText_company_address.setText("");
                //EditText_cost.setText("");
                EditText_business_hour.setText("");
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        //拎番個id
        if (!parent.getSelectedItem().toString().equals("全選")) {
            priceID = hashPrice.get(parent.getSelectedItem().toString());
        } else {
            priceID = "";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                        R.string.connection_server_warning, Toast.LENGTH_LONG).show();
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


    private void getOptionDetail() {
        //showpDialog();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                APIConfig.URL_Advance_Search_criteria, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                Log.d("json", response.toString());

                try {

                    ArrayList<Spanned> info = new ArrayList<Spanned>();

                    // loop through each json object
                    String status = response.getString("status");
                    JSONObject criteria = response.getJSONObject("data");

                    if (status.equals("success")) {
                        JSONArray category = criteria.getJSONArray("category");
                        JSONObject price = criteria.getJSONObject("price_range");

                        for (int i = 1; i <= price.length(); i++) {
                            String name = price.getString(String.valueOf(i));
                            arrayPrice.add(name);
                            hashPrice.put(name, String.valueOf(i));
                        }

                        for (int i = 0; i < category.length(); i++) {

                            JSONObject categoryDetail = (JSONObject) category.get(i);
                            String name = categoryDetail.getString("name");
                            String ID = categoryDetail.getString("ID");
                            //  hashPrice.put(name, ID);
                            //arrayPrice = new String[category.length()];
                            array_category.add(name);
                            hash_category.put(name, ID);
                        }

                        //put the checkbox into linearLayout
                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        for (int i = 0; i < array_category.size(); i++) {

                            CheckBox checkBox = new CheckBox(getActivity());
                            checkBox.setText(array_category.get(i));
                            checkBox.setTextColor(getResources().getColor(R.color.font_light_white));

                            checkBox.setLayoutParams(lparams);

                            linearCat.addView(checkBox);

                        }


                    } else {
                        String errorMsg = criteria.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }

                    adapterPrice.notifyDataSetChanged();


                } catch (JSONException e) {
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        R.string.connection_server_warning, Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

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