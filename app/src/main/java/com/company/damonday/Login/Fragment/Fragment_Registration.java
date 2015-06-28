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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Login.FragmentTabs;
import com.company.damonday.Login.LoginConfig;
import com.company.damonday.Login.SQLiteHandler;
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
public class Fragment_Registration extends Fragment {
    private static final String TAG = Fragment_Registration.class.getSimpleName();
    private Button btnRegister;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmedPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private View v;
    private TabHost mTabHost;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        v = inflater.inflate(R.layout.login_register_tab, container, false);
        inputUsername = (EditText) v.findViewById(R.id.username);
        inputEmail = (EditText) v.findViewById(R.id.email);
        inputPassword = (EditText) v.findViewById(R.id.password);
        inputConfirmedPassword = (EditText) v.findViewById(R.id.confirmed_password);
        btnRegister = (Button) v.findViewById(R.id.btnRegister);


        // Progress dialog
        pDialog = new ProgressDialog(v.getContext());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity());

        // SQLite database handler
        db = new SQLiteHandler(getActivity());

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
                    registerUser(email, username, password);
                }
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

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                LoginConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    if (error.equals("success")) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table
                        db.addUser(email, username);
                        Log.d("email", email);


                        //display message login successfully
                        AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                        ab.setTitle(R.string.register_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent in = new Intent(v.getContext(), FragmentTabs.class);
                                v.getContext().startActivity(in);
                            }
                        });
                        ab.create().show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        JSONObject data = jObj.getJSONObject("data");
                        String errorMsg = data.getString("msg");

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
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("email", email);
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
