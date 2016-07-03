package com.company.damonday.Setting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.company.damonday.MainActivity;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 21/9/15.
 */



    public class FeedBack extends Fragment {
    EditText EditText_Subject;
    EditText EditText_Content;
    EditText EditText_Email;
    String Subject;
    String Content;
    String Email;
    Button btn_submit;
    Button btn_reset;
    private View view;

    private ProgressImage pDialog;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);



        }



        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             view = inflater.inflate(R.layout.setting_feedback, container, false);
            //getActivity().getActionBar().setTitle(R.string.feedback);
            getActivity().setTitle(R.string.feedback);
            EditText_Subject=(EditText)view.findViewById(R.id.subject);
            EditText_Content=(EditText)view.findViewById(R.id.content);
            EditText_Email=(EditText)view.findViewById(R.id.email);
            btn_submit = (Button)view.findViewById(R.id.button_submit);
            btn_reset = (Button)view.findViewById(R.id.button_reset);
            pDialog = new ProgressImage(view.getContext());

            btn_submit.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Subject=EditText_Subject.getText().toString();
                    Content=EditText_Content.getText().toString();
                    Email=EditText_Email.getText().toString();


                    submitting(Subject,Content,Email);


                }
            });


            btn_reset.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    EditText_Subject.setText("");
                    EditText_Content.setText("");
                    EditText_Email.setText("");


                }
            });


            return view;
        }


    private void submitting(final String subject, final String content,final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_SETTING_feedback, new Response.Listener<String>() {

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

                             //   System.out.println("testtom31/1=" + fragmentManager.getBackStackEntryCount());
                              //  getActivity().getActionBar().setTitle(R.string.home);
                                getActivity().setTitle(R.string.home);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("feedback"));

                                fragmentTransaction.add(R.id.frame_container, home_fragment, "home").addToBackStack(null);
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();

                               // this.getActionBar().setDisplayHomeAsUpEnabled(false);
                               // getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);      //tomc 31/1/2016  To disable the back button in home



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
                params.put("subject", subject);
                params.put("content", content);
                params.put("email", email);

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






