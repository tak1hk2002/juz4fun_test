package com.company.damonday.Login.Fragment;

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
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Login.FragmentTabs;
import com.company.damonday.function.APIConfig;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MainActivity;
import com.company.damonday.R;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lamtaklung on 31/5/15.
 */
public class Fragment_Registration extends Fragment {
    private static final String TAG = Fragment_Registration.class.getSimpleName();
    private Button btnRegister, btnCancel;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmedPassword;
    private ProgressImage pDialog;
    private SessionManager session;
    private LoginSQLiteHandler db;
    private View v;
    private StringRequest strReq;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
Log.d("tomtomtomtom","register");
        getActivity().setTitle(R.string.register);
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.login_register_tab, container, false);
        inputUsername = (EditText) v.findViewById(R.id.username);
        inputEmail = (EditText) v.findViewById(R.id.email);
        inputPassword = (EditText) v.findViewById(R.id.password);
        inputConfirmedPassword = (EditText) v.findViewById(R.id.confirmed_password);
        btnRegister = (Button) v.findViewById(R.id.btn_register);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel);


        // Progress dialog
        pDialog = new ProgressImage(getActivity());

        // Session manager
        session = new SessionManager(getActivity());

        // SQLite database handler
        db = new LoginSQLiteHandler(getActivity());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(v.getContext(),
                    MainActivity.class);
            startActivity(intent);
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmedPassword = inputConfirmedPassword.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getActivity(),
                            R.string.hint_email, Toast.LENGTH_LONG)
                            .show();

                }
                else if (username.isEmpty()) {
                    Toast.makeText(getActivity(),
                            R.string.hint_username, Toast.LENGTH_LONG)
                            .show();

                }
                else if (password.isEmpty()) {
                    Toast.makeText(getActivity(),
                            R.string.hint_password, Toast.LENGTH_LONG)
                            .show();

                }
                else if(!password.equals(confirmedPassword)){
                    Toast.makeText(getActivity(),
                           R.string.hint_confirmed_password, Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    System.out.println(email+"  "+username+"  "+password);
                    registerUser(email, username, password);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        return v;
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String email, final String username,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        showDialog();

        strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int status = jObj.getInt("status");
                    if (status == 1) {
                        //JSONObject data = jObj.getJSONObject("data");
                        //get toke from API
                        //String token = data.getString("token");
                        // Inserting row in users table
                        //db.addUser(token, email, username);

                        //display message login successfully
                        AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                        ab.setTitle(R.string.register_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Bundle bundle = new Bundle();
                                bundle.putString("lastFragment", "setting");

                                Fragment_Login fragment_login = new Fragment_Login();
                                fragment_login.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame_container, fragment_login, "login").addToBackStack(null);
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();
                            }
                        });
                        ab.create().show();

                    } else if (status == 0) {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*@Override
    public void onStop() {
        super.onStop();
        if (strReq != null)  {
            strReq.cancel();
            Log.d("onStop", "Registration requests are all cancelled");
        }
    }*/

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
