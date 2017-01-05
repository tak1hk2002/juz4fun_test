package com.company.damonday.Login.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Home.Home;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Forget_Password extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private ProgressImage pDialog;
    private StringRequest strReq;
    private static final String TAG = Fragment_Forget_Password.class.getSimpleName();


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Forget_Password.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Forget_Password newInstance(String param1, String param2) {
        Fragment_Forget_Password fragment = new Fragment_Forget_Password();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Forget_Password() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Progress dialog
        pDialog = new ProgressImage(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.login_forget_password, container, false);
        getActivity().setTitle(R.string.forget_password);
        final EditText edEmail = (EditText) view.findViewById(R.id.email);
        final EditText edUsername = (EditText) view.findViewById(R.id.username);
        Button cancel = (Button) view.findViewById(R.id.btnCancel);
        Button confirm = (Button) view.findViewById(R.id.btnComfirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((edEmail.getText().toString()).isEmpty())
                    Toast.makeText(getActivity(),
                            R.string.login_warning_forget_password_email, Toast.LENGTH_SHORT)
                            .show();

                else if ((edUsername.getText().toString()).isEmpty())
                    Toast.makeText(getActivity(),
                            R.string.login_warning_forget_password_username, Toast.LENGTH_SHORT)
                            .show();
                else
                    sendEmail(edEmail.getText().toString(), edUsername.getText().toString());
            }
        });

        return view;
    }

    private void sendEmail(final String email, final String username) {
// Tag used to cancel the request
        String tag_string_req = "req_forget_password";

        showDialog();

        strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_FORGET_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("status");

                    // Check for error node in json
                    if (error == 1) {
                        AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_DARK);
                        ab.setTitle(R.string.forgot_password_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                Intent in = new Intent(view.getContext(), MainActivity.class);
//                                view.getContext().startActivity(in);

                                Home home_fragment = new Home();

                                FragmentManager fragmentManager = getFragmentManager();


                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //tomc 31/1/2016  To disable the back button in home
                                //  getActivity().getActionBar().setTitle(R.string.home);
                                getActivity().setTitle(R.string.home);
                                ((TestActivity) getActivity()).showMenuButton();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("feedback"));
                                fragmentTransaction.add(R.id.frame_container, home_fragment, "home").addToBackStack("main");
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();

                                // this.getActionBar().setDisplayHomeAsUpEnabled(false);
                                // getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);      //tomc 31/1/2016  To disable the back button in home


                            }
                        });
                        ab.create().show();
                        try {
                            redirectTo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (error == 0) {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        R.string.connection_server_warning, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void redirectTo(){
        Fragment fragment = null;
        fragment = new Home();
        //getActivity().getActionBar().show();
        // This method will be executed once the timer is over
        // Start your app main activity
                                /*Intent i = new Intent(SplashActivity.this, TestActivity.class);
                                startActivity(i);*/
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            //clear all of the fragment at the stack
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //System.out.println(fragmentManager.getBackStackEntryCount());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment, "home").addToBackStack("main");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
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
