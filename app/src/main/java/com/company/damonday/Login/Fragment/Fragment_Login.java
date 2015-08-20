package com.company.damonday.Login.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Login.DisplayLogin;
import com.company.damonday.function.APIConfig;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.MainActivity;
import com.company.damonday.R;
import com.company.damonday.function.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lamtaklung on 31/5/15.
 */
public class Fragment_Login extends Fragment {
    private static final String TAG = Fragment_Registration.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.login_login_tab, container, false);
        inputUsername = (EditText)v.findViewById(R.id.username);
        inputPassword = (EditText)v.findViewById(R.id.password);
        btnLogin = (Button) v.findViewById(R.id.btnLogin);


        // Progress dialog
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            Intent intent = new Intent(v.getContext(), DisplayLogin.class);
            startActivity(intent);

        }

        btnLogin.setOnClickListener(new AdapterView.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                // Check for empty data in the form
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(username, password);
                } else {
                    //Prompt user to enter credentials
                    Toast.makeText(getActivity(),
                            "請輸入使用者名稱和密碼", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });





        return v;

    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("登入中 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");

                    // Check for error node in json
                    if (error.equals("success")) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);


                        //display message login successfully
                        AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                        ab.setTitle(R.string.login_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent in = new Intent(v.getContext(), MainActivity.class);
                                v.getContext().startActivity(in);
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);


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