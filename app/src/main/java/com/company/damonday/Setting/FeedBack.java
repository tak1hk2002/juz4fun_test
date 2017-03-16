package com.company.damonday.Setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Framework.SubmitForm.SubmitForm;
import com.company.damonday.Framework.SubmitForm.SubmitForm_CustomListAdapter;
import com.company.damonday.Home.Home;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 21/9/15.
 */


public class FeedBack extends Fragment {
    String Subject;
    String Content;
    String Email;
    Button btn_submit, btnSubmit, btnReset;
    Button btn_reset;
    private View view;

    ListView listView;
    private List<SubmitForm> items = new ArrayList<SubmitForm>();
    private List<Integer> showDetailIndicator = Arrays.asList(2);
    private List<Integer> hideEditText = Arrays.asList(2);
    private List<Integer> numericEditText = Arrays.asList();
    private String[] warning, title;

    private ProgressImage pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.feedback);

        ((TestActivity) getActivity()).showBackButton();
        ((TestActivity) getActivity()).hideMenuButton();

        //get the array list of newFound option
        title = getResources().getStringArray(R.array.feedback_title);
        for(int i = 0; i < title.length; i++){
            SubmitForm submitForm = new SubmitForm();
            submitForm.setTitle(title[i]);
            submitForm.setSubmitWarning(false);
            items.add(submitForm);
        }

        //get the warning text
        warning = getResources().getStringArray(R.array.feedback_warning);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newfound, container, false);

        pDialog = new ProgressImage(view.getContext());


        view = inflater.inflate(R.layout.newfound, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        btnReset = (Button) view.findViewById(R.id.button_reset);
        btnSubmit = (Button) view.findViewById(R.id.button_submit);

        final SubmitForm_CustomListAdapter customAdapter = new SubmitForm_CustomListAdapter(getActivity(), items, showDetailIndicator,
                                                                                            hideEditText, numericEditText, warning);


        listView.setAdapter(customAdapter);


        btnSubmit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                //init
                String submitVars[] = new String[items.size()];
                Boolean passChecking = true;
                //------------------------------------------------------------------------

                for (int i = 0; i < items.size(); i++) {
                    //get the value that the user inputted
                    submitVars[i] = items.get(i).getInfo().trim();

                    //show warning of each view
                    if (submitVars[i].isEmpty()) {
                        items.get(i).setSubmitWarning(true);
                        passChecking = false;
                    }
                    else{
                        items.get(i).setSubmitWarning(false);
                    }
                }

                if (passChecking) {
                    System.out.println(submitVars[0]);
                    System.out.println(submitVars[1]);
                    System.out.println(submitVars[2]);
                    submitting(submitVars[0], submitVars[1], submitVars[2]);
                }

                customAdapter.notifyDataSetChanged();

            }
        });


        btnReset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                for(int i = 0; i < items.size(); i++){
                    //View view = null;
                    items.get(i).setInfo("");
                    items.get(i).setSubmitWarning(false);
                    //get each view of  the listview
                    //view = listView.getAdapter().getView(i, view, listView);
                }
                customAdapter.notifyDataSetChanged();

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder[] ab = {new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)};
                AlertDialog dialog;
                final EditText txtContent = new EditText(getActivity());


                //set the edit text color
                txtContent.setTextColor(getResources().getColor(R.color.font_white));

                txtContent.setLines(15);
                txtContent.setGravity(Gravity.TOP);
                txtContent.setGravity(Gravity.LEFT);

                //change the Cursor color to white
                Field f = null;
                try {
                    f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(txtContent, R.drawable.color_cursor);
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }

                switch(position) {
                    //content
                    case 2:
                        //get the dialog content
                        String stringContent = "";
                        if (items.get(position).getInfo() != null)
                            stringContent = items.get(position).getInfo().trim();

                        if (stringContent.isEmpty()) {
                            txtContent.setText("");
                        } else {
                            txtContent.setText(stringContent);
                        }


                        ab[0].setTitle(R.string.writeComment_dialog_content);

                        ab[0].setView(txtContent);

                        ab[0].setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable content = txtContent.getText();
                                //OR
                                String content = txtContent.getText().toString().trim();
                                items.get(position).setInfo(content);
                                customAdapter.notifyDataSetChanged();
                                //listView.setAdapter(customAdapter);
                                //setListViewHeightBasedOnChildren(listView);
                            }
                        });

                        ab[0].setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                                dialog.dismiss();
                            }
                        });

                        //when alertview is launched, the keyboard show immediately
                        dialog = ab[0].create();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                        dialog.show();

                        break;
                }
            }

        });


        return view;
    }


    private void submitting(final String subject, final String email, final String content) {
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
                    int status = jObj.getInt("status");

                    // Check for error node in json
                    if (status == 1) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);


                        //display message login successfully
                        AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_DARK);
                        ab.setTitle(R.string.submit_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //go to setting page
                                ((TestActivity) getActivity()).displayView(6);

//                                Intent in = new Intent(view.getContext(), MainActivity.class);
//                                view.getContext().startActivity(in);

                                /*Setting setting_fragment = new Setting();

                                FragmentManager fragmentManager = getFragmentManager();


                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);  //tomc 31/1/2016  To disable the back button in home
                                //  getActivity().getActionBar().setTitle(R.string.home);
                                getActivity().setTitle(R.string.home);
                                ((TestActivity) getActivity()).showMenuButton();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("feedback"));
                                fragmentTransaction.add(R.id.frame_container, setting_fragment, "setting").addToBackStack("main");
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();*/

                                // this.getActionBar().setDisplayHomeAsUpEnabled(false);
                                // getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);      //tomc 31/1/2016  To disable the back button in home


                            }
                        });
                        ab.create().show();


                    } else if (status == 0) {
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
                Log.e("FeedBack", error.getMessage());
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






