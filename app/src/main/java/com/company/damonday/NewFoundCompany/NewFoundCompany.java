package com.company.damonday.NewFoundCompany;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.company.damonday.Home.Home;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;

import com.company.damonday.R;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    String[] title;
    ListView listView;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private String priceID;
    private ArrayList<String> array_category = new ArrayList<String>();
    private HashMap<String, String> hashPrice = new HashMap<String, String>();
    private HashMap<Integer, String> hash_category = new HashMap<Integer, String>();
    Button btn_submit;
    Button btn_reset;
    LinearLayout linearCat;
    APIConfig optionDetail;
    List<String> arrayPrice = new ArrayList<String>();
    private ArrayAdapter adapterPrice;
    Map<String, Integer> mapExpense = new HashMap<String, Integer>();
    ArrayList<Integer> selectedCat = new ArrayList();
    ArrayList<String> selectedCatName = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);
        //hi

        //get the array list of newFound option
        title = getResources().getStringArray(R.array.newFound_title);
        for(int i = 0; i < title.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", title[i]);
            item.put("info", "");
            items.add(item);
        }



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getOptionDetail();
        view = inflater.inflate(R.layout.newfound, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items,
                R.layout.view_companywritecomment_list, new String[] {"title", "info"}, new int[] {R.id.title, R.id.info});
        listView.setAdapter(simpleAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final EditText companyName = new EditText(getActivity());
                final EditText companyAddress = new EditText(getActivity());
                final EditText companyTel = new EditText(getActivity());
                final EditText businessHour = new EditText(getActivity());
                final String[] expense = {""};
                final int[] selectedExpense = {-1};
                final boolean[] selectedCatItem = new boolean[4];
                final ImageView imgIndicator = (ImageView) view.findViewById(R.id.indicator);
                final TextView txtInfo = (TextView) view.findViewById(R.id.info);
                final TimePicker openTime = new TimePicker(getActivity());
                openTime.setIs24HourView(true);
                final TimePicker endTime = new TimePicker(getActivity());
                endTime.setIs24HourView(true);
                //get the dialog content
                String dialogCompanyName = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogCompanyAddress = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogCompanyTel = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogBusinessHour = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogExpense = items.get(position).get("info").toString().trim();
                //get the dialog content
                String dialogCat = items.get(position).get("info").toString().trim();
                final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
                AlertDialog dialog;
                final AlertDialog[] insideDialog = new AlertDialog[1];

                //set the edit text color
                companyName.setTextColor(getResources().getColor(R.color.font_white));
                companyAddress.setTextColor(getResources().getColor(R.color.font_white));
                companyTel.setTextColor(getResources().getColor(R.color.font_white));
                businessHour.setTextColor(getResources().getColor(R.color.font_white));


                //initial the time picker
                openTime.setCurrentHour(0);
                openTime.setCurrentMinute(0);

                //only allow user to enter digits and "."
                companyTel.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

                //set only single line
                companyName.setSingleLine(true);
                businessHour.setSingleLine(true);

                //change the Cursor color to white
                Field f = null;
                try {
                    f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(companyName, R.drawable.color_cursor);
                } catch (Exception ignored) {
                }

                try {
                    f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(companyAddress, R.drawable.color_cursor);
                } catch (Exception ignored) {
                }

                try {
                    f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(companyTel, R.drawable.color_cursor);
                } catch (Exception ignored) {
                }





                if (dialogCompanyName.isEmpty()) {
                    //imgIndicator.setVisibility(view.VISIBLE);
                    companyName.setText("");
                } else {
                    //imgIndicator.setVisibility(view.GONE);
                    companyName.setText(dialogCompanyName);
                }
                if (dialogCompanyAddress.isEmpty()) {
                    companyAddress.setText("");
                } else {
                    companyAddress.setText(dialogCompanyAddress);
                }
                if (dialogCompanyTel.isEmpty()) {
                    companyTel.setText("");
                } else {
                    companyTel.setText(dialogCompanyTel);
                }
                if (dialogBusinessHour.isEmpty()) {
                    businessHour.setText("");
                } else {
                    businessHour.setText(dialogBusinessHour);
                }
                if (dialogExpense.isEmpty()) {
                    expense[0] = "";
                } else {
                    expense[0] = dialogExpense;
                    if(mapExpense.get(dialogExpense) != null)
                        selectedExpense[0] = mapExpense.get(dialogExpense);
                }

                //get the history of selecting cat
                if(!dialogCat.isEmpty()) {
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
                switch (position) {
                    //company name
                    case 0:
                        ab.setTitle(title[position]);

                        ab.setView(companyName);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable title = txtTitle.getText();
                                //OR
                                String name = companyName.getText().toString().trim();
                                if (name.isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", name);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        //when alertview is launched, the keyboard show immediately
                        dialog = ab.create();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                        dialog.show();
                        break;

                    case 1:
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
                                if (selectedCatName.size() == 0) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", selectedCatName);
                                simpleAdapter.notifyDataSetChanged();
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
                    //Company Address
                    case 2:
                        ab.setTitle(title[position]);

                        ab.setView(companyAddress);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable title = txtTitle.getText();
                                //OR
                                String address = companyAddress.getText().toString().trim();
                                if (address.isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", address);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        //when alertview is launched, the keyboard show immediately
                        dialog = ab.create();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                        dialog.show();
                        break;
                    //Company Telephone
                    case 3:
                        ab.setTitle(title[position]);

                        ab.setView(companyTel);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable title = txtTitle.getText();
                                //OR
                                String tel = companyTel.getText().toString().trim();
                                if (tel.isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", tel);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        //when alertview is launched, the keyboard show immediately
                        dialog = ab.create();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                        dialog.show();
                        break;
                    //expense
                    case 4:
                        ab.setTitle(title[position]);
                        //setSingleChiceItems cannot support List of arrayList, so I need to convert it to array
                        ab.setSingleChoiceItems(arrayPrice.toArray(new String[arrayPrice.size()]), selectedExpense[0], new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 1:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 2:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 3:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                    case 4:
                                        expense[0] = arrayPrice.get(which);
                                        selectedExpense[0] = which;
                                        break;
                                }

                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (expense[0].isEmpty()) {
                                    imgIndicator.setVisibility(view.VISIBLE);
                                    txtInfo.setVisibility(view.GONE);
                                } else {
                                    imgIndicator.setVisibility(view.GONE);
                                    txtInfo.setVisibility(view.VISIBLE);
                                }
                                items.get(position).put("info", expense[0]);
                                simpleAdapter.notifyDataSetChanged();
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
                    //business hour
                    case 5:
                        ab.setTitle(R.string.newFound_business_hour_start);
                        ab.setView(openTime);
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                AlertDialog.Builder xy = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);

                                xy.setTitle(R.string.newFound_business_hour_end);
                                xy.setView(endTime);

                                xy.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
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


        btn_submit = (Button) view.findViewById(R.id.button_submit);
        btn_reset = (Button) view.findViewById(R.id.button_reset);
        pDialog = new ProgressImage(view.getContext());








        btn_submit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                String submitVars[] = new String[items.size() - 1];

                /*for (int i = 0; i < items.size() - 1; i++){
                    //get the value that the user inputted
                    submitVars[i] = items.get(i).get("info").toString().trim();
                    if(submitVars[i].equals(">"))
                        submitVars[i] = "";
                    if(submitVars[i].isEmpty()){
                        //Toast.makeText(getActivity(), warning[i], Toast.LENGTH_SHORT).show();
                        passChecking = false;
                    }
                }*/

                submitting(company_name, company_tel, company_type, company_address, company_cost, company_business_hour);


            }
        });


        btn_reset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {



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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}