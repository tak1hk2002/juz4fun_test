package com.company.damonday.NewFoundCompany;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spanned;
import android.text.TextUtils;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Framework.SubmitForm.SubmitForm;
import com.company.damonday.Framework.SubmitForm.SubmitForm_CustomListAdapter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.damonday.Launch.LaunchPage.PREFS_NAME;

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
    private ArrayList<String> array_category = new ArrayList<String>();
    private HashMap<Integer, String> hash_category = new HashMap<Integer, String>();
    private HashMap<Integer, String> hash_Price = new HashMap<Integer, String>();
    private boolean[] selectedCatItem;
    private List<String> selectedCatName = new ArrayList<>();
    private boolean[] tempSelectedCatItem;
    private ArrayList<Integer> categoryID = new ArrayList<>();
    private int priceID;
    private int selectedExpenseItem = -1;
    private String selectedExpenseName;
    private int tempSelectedExpenseItem = -1;


    private Button btnSubmit, btnReset;
    private LinearLayout linearCat;
    private APIConfig optionDetail;
    private List<String> arrayPrice = new ArrayList<String>();
    private ArrayAdapter adapterPrice;
    private Map<String, Integer> mapExpense = new HashMap<String, Integer>();
    private JsonObjectRequest jsonObjReq;
    private List<Integer> showDetailIndicator = Arrays.asList(1, 4, 5);
    private List<Integer> hideEditText = Arrays.asList();
    private List<Integer> warningIndex = Arrays.asList(0,1);
    private JSONObject priceJsonObject;
    private JSONArray categoryJsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get search criteria
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
        String category = settings.getString("category", null);
        String price = settings.getString("price", null);
        if(category != null && price != null) {
            try {
                categoryJsonArray = new JSONArray(category);
                priceJsonObject = new JSONObject(price);
                System.out.println(categoryJsonArray);
                System.out.println(priceJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

        //create Option detail including category and price
        try {
            createOptionDetail();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.newfound, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        btnReset = (Button) view.findViewById(R.id.button_reset);
        btnSubmit = (Button) view.findViewById(R.id.button_submit);

        final SubmitForm_CustomListAdapter customAdapter = new SubmitForm_CustomListAdapter(getActivity(), items, showDetailIndicator, hideEditText, warning);


        listView.setAdapter(customAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                String openTimeHour = null;
                String openTimeMin = null;
                String endTimeHour = null;
                String endTimeMin = null;
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

                        ab.setTitle(title[position]);
                        ab.setMultiChoiceItems(array_category.toArray(new String[array_category.size()]), selectedCatItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //AlertDialog alertDialog = (AlertDialog) dialog;
                                //final ListView listView = alertDialog.getListView();

                                categoryID = new ArrayList<>();
                                selectedCatName = new ArrayList<String>();

                                //list to be string
                                if(selectedCatItem != null) {
                                    for (int i = 0; i < selectedCatItem.length; i++) {
                                        tempSelectedCatItem[i] = selectedCatItem[i];

                                        //add the final select ID into list
                                        if(selectedCatItem[i]) {
                                            selectedCatName.add(hash_category.get(i+1));
                                            categoryID.add(i);
                                        }
                                        else
                                            selectedCatName.remove(hash_category.get(i+1));
                                    }
                                }
                                //keep selected name string in history
                                items.get(position).setInfo(TextUtils.join(", ", selectedCatName).toString());

                                Log.d("categoryID", categoryID.toString());
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < selectedCatItem.length; i++) {
                                    selectedCatItem[i] = tempSelectedCatItem[i];
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        break;
                    case 4:
                        ab.setTitle(R.string.newFound_expense);
                        ab.setSingleChoiceItems(arrayPrice.toArray(new String[arrayPrice.size()]), selectedExpenseItem, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExpenseItem = which;
                                selectedExpenseName = arrayPrice.get(which);
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                priceID = selectedExpenseItem;
                                tempSelectedExpenseItem = selectedExpenseItem;
                                items.get(position).setInfo(selectedExpenseName);
                                Log.d("priceID", Integer.toString(priceID));
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExpenseItem = tempSelectedExpenseItem;
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
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

                //init
                String submitVars[] = new String[items.size()];
                Boolean passChecking = true;

                for (int i = 0; i < items.size(); i++){
                    //get the value that the user inputted
                    submitVars[i] = items.get(i).getInfo().trim();
                    if(warningIndex.contains(i)){
                        if(submitVars[i].isEmpty()) {
                            //Toast.makeText(getActivity(), warning[i], Toast.LENGTH_SHORT).show();
                            //passChecking = false;
                            items.get(i).setSubmitWarning(true);
                            passChecking = false;
                        }
                        else{
                            System.out.println(submitVars[i]);
                            items.get(i).setSubmitWarning(false);
                        }
                    }
                    else {
                        items.get(i).setSubmitWarning(false);
                        System.out.println(submitVars[i]);
                    }
                }

                if(passChecking){
                    System.out.println("Company: " +submitVars[0]);
                    System.out.println("Cat: " +submitVars[1]);
                    System.out.println("address: " +submitVars[2]);
                    System.out.println("tel: " +submitVars[3]);
                    System.out.println("price: " + submitVars[4]);
                    System.out.println("Company: " +submitVars[5]);


                    submitting(submitVars[0], submitVars[3], submitVars[1], submitVars[2], submitVars[4], submitVars[5]);

                }
                customAdapter.notifyDataSetChanged();
                //what is the format of business hour
                //
                //
                //
                //
                //
                //



            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setInfo("");
                    //get each view of  the listview
                }
                selectedCatItem = new boolean[array_category.size()];
                tempSelectedCatItem = new boolean[array_category.size()];
                categoryID = new ArrayList<Integer>();
                selectedCatName = new ArrayList<>();

                selectedExpenseItem = -1;
                tempSelectedExpenseItem = -1;
                selectedExpenseName = "";
                priceID = -1;


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
                    int status = jObj.getInt("status");

                    // Check for error node in json
                    if (status == 1) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);


                        AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_DARK);
                        ab.setTitle(R.string.submit_success);
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
                                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("newfound"));
                                fragmentTransaction.add(R.id.frame_container, home_fragment, "home").addToBackStack("main");
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();

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
                Log.e("FeedBack", "Login Error: " + error.getMessage());
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
                params.put("name", company_name);
                params.put("tel", company_tel);
                params.put("type", company_type);
                params.put("address", company_address);
                params.put("price", company_cost);
                params.put("businessHour", company_business_hour);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



    private void createOptionDetail() throws JSONException {
        if(categoryJsonArray != null) {
            for (int i = 1; i < categoryJsonArray.length(); i++) {

                JSONObject categoryDetail = (JSONObject) categoryJsonArray.get(i);
                String name = categoryDetail.getString("cht_name");
                String id = categoryDetail.getString("id");
                //  hashPrice.put(name, ID);
                //arrayPrice = new String[category.length()];
                array_category.add(name);
                hash_category.put(i, name);
            }
        }
        //inital
        selectedCatItem = new boolean[array_category.size()];
        tempSelectedCatItem = new boolean[array_category.size()];

        if(priceJsonObject != null) {
            for (int i = 1; i <= priceJsonObject.length(); i++) {
                String name = priceJsonObject.getString(String.valueOf(i));
                arrayPrice.add(name);
                hash_Price.put(i, name);
            }
        }
        //inital
        //selectedExpenseItem = new boolean[arrayPrice.size()];
        //tempSelectedExpenseItem = new boolean[arrayPrice.size()];

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