package com.company.damonday.NewFoundCompany;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Framework.SubmitForm.SubmitForm;
import com.company.damonday.Framework.SubmitForm.SubmitForm_CustomListAdapter;
import com.company.damonday.Home.Home;
import com.company.damonday.function.APIConfig;

import com.company.damonday.R;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    Spinner EditText_cost;
    EditText EditText_business_hour;
    String company_name;
    String company_tel;
    String company_type;
    String company_address;
    String company_cost;
    String company_business_hour;
    private String[] title, warning;
    private ListView listView;
    private List<SubmitForm> items = new ArrayList<SubmitForm>();
    private String priceID;
    private ArrayList<String> array_category = new ArrayList<String>();
    private HashMap<String, Integer> hashPrice = new HashMap<String, Integer>();
    private HashMap<Integer, String> hash_category = new HashMap<Integer, String>();
    private Button btnSubmit, btnReset;
    private LinearLayout linearCat;
    private APIConfig optionDetail;
    private List<String> arrayPrice = new ArrayList<String>();
    private ArrayAdapter adapterPrice;
    private Map<String, Integer> mapExpense = new HashMap<String, Integer>();
    private ArrayList<Integer> selectedCat = new ArrayList();
    private ArrayList<String> selectedCatName = new ArrayList<>();
    private JsonObjectRequest jsonObjReq;
    private List<Integer> showDetailIndicator = Arrays.asList(1, 5);
    private List<Integer> hideEditText = Arrays.asList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);
        //hi

        //get the array list of newFound option
        title = getResources().getStringArray(R.array.newFound_title);
        for(int i = 0; i < title.length; i++){
            SubmitForm comment = new SubmitForm();
            comment.setTitle(title[i]);
            comment.setSubmitWarning(false);
            items.add(comment);
        }

        //get the warning text
        warning = getResources().getStringArray(R.array.newFound_warning);



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getOptionDetail();
        view = inflater.inflate(R.layout.newfound, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        btnReset = (Button) view.findViewById(R.id.button_reset);
        btnSubmit = (Button) view.findViewById(R.id.button_submit);

        final SubmitForm_CustomListAdapter customAdapter = new SubmitForm_CustomListAdapter(getActivity(), items, showDetailIndicator, hideEditText, warning);


        listView.setAdapter(customAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final String[] expense = {""};
                String openTimeHour = null;
                String openTimeMin = null;
                String endTimeHour = null;
                String endTimeMin = null;
                final int[] selectedExpense = {-1};
                final boolean[] selectedCatItem = new boolean[4];
                final ImageView imgIndicator = (ImageView) view.findViewById(R.id.indicator);
                final TextView txtInfo = (TextView) view.findViewById(R.id.info);
                final TimePicker openTime = new TimePicker(getActivity());
                openTime.setIs24HourView(true);
                final TimePicker endTime = new TimePicker(getActivity());
                endTime.setIs24HourView(true);


                final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
                AlertDialog dialog;
                final AlertDialog[] insideDialog = new AlertDialog[1];


                switch (position) {
                    //cat
                    case 1:
                        //get the dialog content

                        String dialogCat = "";
                        if (items.get(position).getInfo() != null)
                            dialogCat = items.get(position).getInfo();
                        //get the history of selecting cat
                        if (!dialogCat.isEmpty()) {
                            for (int i = 0; i < array_category.size(); i++) {
                                selectedCatItem[i] = false;
                                for (int j = 0; j < selectedCat.size(); j++) {
                                    if (selectedCat.get(j) == i) {
                                        selectedCatItem[i] = true;
                                        break;
                                    }
                                }
                            }
                        }

                        ab.setTitle(title[position]);
                        //setSingleChiceItems cannot support List of arrayList, so I need to convert it to array
                        ab.setMultiChoiceItems(array_category.toArray(new String[array_category.size()]), selectedCatItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                System.out.println(indexSelected);
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedCat.add(indexSelected);
                                    selectedCatName.add(hash_category.get(indexSelected));

                                } else if (selectedCat.contains(indexSelected)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedCat.remove(Integer.valueOf(indexSelected));
                                    selectedCatName.remove(hash_category.get(indexSelected));
                                }
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //items.get(position).setInfo(selectedCatName);
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        ab.show();
                        break;
                    //business hour
                    case 5:
                        //get the dialog content
                        String dialogBusinessHour = "";
                        if (items.get(position).getInfo() != null)
                            dialogBusinessHour = items.get(position).getInfo();
                        System.out.println(dialogBusinessHour);
                        if (dialogBusinessHour.isEmpty() || dialogBusinessHour.length() == 0) {
                            //initial the time picker
                            openTime.setCurrentHour(0);
                            openTime.setCurrentMinute(0);
                            endTime.setCurrentHour(0);
                            endTime.setCurrentMinute(0);
                            openTimeHour = null;
                            openTimeMin = null;
                            endTimeHour = null;
                            endTimeMin = null;
                        } else {
                            openTimeHour = items.get(position).getOpenHour().toString();
                            openTimeMin = items.get(position).getOpenMin().toString();
                            endTimeHour = items.get(position).getEndHour().toString();
                            endTimeMin = items.get(position).getEndMin().toString();
                            //initial the time picker
                            openTime.setCurrentHour(Integer.parseInt(openTimeHour));
                            openTime.setCurrentMinute(Integer.parseInt(openTimeMin));
                            endTime.setCurrentHour(Integer.parseInt(endTimeHour));
                            endTime.setCurrentMinute(Integer.parseInt(endTimeMin));
                            //businessHour.setText(dialogBusinessHour);
                        }
                        ab.setTitle(R.string.newFound_business_hour_start);
                        ab.setView(openTime);
                        final String finalOpenTimeHour = openTimeHour;
                        final String finalOpenTimeMin = openTimeMin;
                        final String finalEndTimeHour = endTimeHour;
                        final String finalEndTimeMin = endTimeMin;
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                AlertDialog.Builder xy = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);

                                xy.setTitle(R.string.newFound_business_hour_end);
                                xy.setView(endTime);

                                xy.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        //convert the business time to string format
                                        int openHour = openTime.getCurrentHour();
                                        int openMin = openTime.getCurrentMinute();
                                        int endHour = endTime.getCurrentHour();
                                        int endMin = endTime.getCurrentMinute();
                                        String sOpenHour, sOpenMin, sEndHour, sEndMin;
                                        if(openHour < 10){
                                            sOpenHour = "0"+openHour;
                                        } else {
                                            sOpenHour = String.valueOf(openHour);
                                        }
                                        if(openMin < 10){
                                            sOpenMin = "0"+openMin;
                                        } else {
                                            sOpenMin = String.valueOf(openMin);
                                        }
                                        if(endHour < 10){
                                            sEndHour = "0"+endHour;
                                        } else {
                                            sEndHour = String.valueOf(endHour);
                                        }
                                        if(endMin < 10){
                                            sEndMin = "0"+endMin;
                                        } else {
                                            sEndMin = String.valueOf(endMin);
                                        }
                                        String actualOpenTime = sOpenHour + ":" + sOpenMin;
                                        String actualEndTime = sEndHour + ":" + sEndMin;
                                        String actualTime = actualOpenTime + " - " + actualEndTime;
                                        System.out.println(actualTime);

                                        items.get(position).setInfo(actualTime);
                                        items.get(position).setOpenHour(openTime.getCurrentHour());
                                        items.get(position).setOpenMin(openTime.getCurrentMinute());
                                        items.get(position).setEndHour(endTime.getCurrentHour());
                                        items.get(position).setEndMin(endTime.getCurrentMinute());
                                        customAdapter.notifyDataSetChanged();
                                    }
                                });
                                xy.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        insideDialog[0].dismiss();
                                    }
                                });
                                insideDialog[0] = xy.create();
                                insideDialog[0].show();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialog = ab.create();
                        dialog.show();
                        break;

                }
            }
        });




       // getActivity().getActionBar().setTitle(R.string.newfound);
        getActivity().setTitle(R.string.newfound);


        pDialog = new ProgressImage(view.getContext());








        btnSubmit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                String submitVars[] = new String[items.size()];

                for (int i = 0; i < items.size(); i++){
                    //get the value that the user inputted
                    submitVars[i] = items.get(i).getInfo().trim();
                    System.out.println(submitVars[i]);
                    if(submitVars[i].isEmpty()){
                        //Toast.makeText(getActivity(), warning[i], Toast.LENGTH_SHORT).show();
                        //passChecking = false;
                    }
                }
                //what is the format of business hour
                //
                //
                //
                //
                //
                //

                //submitting(company_name, company_tel, company_type, company_address, company_cost, company_business_hour);


            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setInfo("");
                    //get each view of  the listview
                }
                customAdapter.notifyDataSetChanged();
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


        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
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
                            hashPrice.put(name, i-1);
                        }

                        for (int i = 0; i < category.length(); i++) {

                            JSONObject categoryDetail = (JSONObject) category.get(i);
                            String name = categoryDetail.getString("name");
                            String ID = categoryDetail.getString("ID");
                            //  hashPrice.put(name, ID);
                            //arrayPrice = new String[category.length()];
                            array_category.add(name);
                            hash_category.put(Integer.parseInt(ID), name);
                        }

                        //put the checkbox into linearLayout
                        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        for(int i=0;i<array_category.size();i++){

                            CheckBox checkBox = new CheckBox(getActivity());
                            checkBox.setText(array_category.get(i));
                            checkBox.setTextColor(getResources().getColor(R.color.font_light_white));

                            checkBox.setLayoutParams(lparams);


                        }




                    }else{
                        String errorMsg = criteria.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }




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

    @Override
    public void onStop() {
        super.onStop();
        if (jsonObjReq != null)  {
            jsonObjReq.cancel();
            Log.d("onStop", "New found requests are all cancelled");
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