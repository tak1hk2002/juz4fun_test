package com.company.damonday.Search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.damonday.Framework.SubmitForm.SubmitForm;
import com.company.damonday.Framework.SubmitForm.SubmitForm_CustomListAdapter;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.damonday.Launch.LaunchPage.PREFS_NAME;


/**
 * Created by tom on 21/6/15.
 */
public class search extends Fragment {

    //private Spinner spinnerPrice, spinner_lagre_district, spinner_district, spinner_category;
    private ArrayAdapter adapterPrice, adapter_lagre_district, adapter_district, adapter_category;
    private ArrayList<String> arrayPrice = new ArrayList<String>();
    private ArrayList<String> array_area_all = new ArrayList<String>();

    private ArrayList<String> array_category = new ArrayList<String>();
    private HashMap<Integer, String> hash_category = new HashMap<Integer, String>();
    private HashMap<Integer, String> hash_Price = new HashMap<Integer, String>();
    private HashMap<Integer, String> hash_large_district = new HashMap<Integer, String>();
    private ArrayList<String> array_lage_district = new ArrayList<>();
    //private HashMap<String, String> hash_district = new HashMap<String, String>();

    private HashMap<Integer, ArrayList<String>> array_district = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> array_HK_Island = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> array_Kowloon = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> array_Islands_District = new HashMap<>();
    private HashMap<Integer, ArrayList<String>>array_New_Territories = new HashMap<>();
    private HashMap<Integer, HashMap<String, String >> areaIdAll = new HashMap<>();
    private HashMap<Integer, HashMap<String, String >> areaIdHkIsland =new HashMap<>();
    private HashMap<Integer, HashMap<String, String >> areaIdKowloon =new HashMap<>();
    private HashMap<Integer, HashMap<String, String >> areaIdNewTerritories =new HashMap<>();
    private HashMap<Integer, HashMap<String, String >> areaIdIslands =new HashMap<>();
    private int selectedAreaIndex = -1;
    private String selectedAreaName = "";
    private ArrayList<String> areaSelection = new ArrayList<String>();
    private boolean[] selectedDistrictItem;
    private boolean[] tempSelectedDistrictItem;
    private List<String> selectedDistrictName = new ArrayList<>();

    private boolean[] selectedExpenseItem;
    private List<String> selectedExpenseName = new ArrayList<>();
    private boolean[] tempSelectedExpenseItem;
    private boolean[] selectedCatItem;
    private List<String> selectedCatName = new ArrayList<>();
    private boolean[] tempSelectedCatItem;

    boolean selectAll = true;

    private ArrayList<Integer> priceID = new ArrayList<>();
    private ArrayList<Integer> districtID = new ArrayList<>();
    private ArrayList<Integer> categoryID = new ArrayList<>();

    private ListView listView;
    private Button button_search;
    private Button button_reset;
    private ProgressImage pDialog;
    private View view;
    private JsonObjectRequest jsonObjReq;
    private List<SubmitForm> items = new ArrayList<SubmitForm>();
    private Map<String, Integer> mapExpense = new HashMap<String, Integer>();
    private String[] title, warning;
    private List<Integer> showDetailIndicator = Arrays.asList(0, 1, 2, 3);
    private JSONObject priceJsonObject;
    private JSONArray categoryJsonArray, districtJsonArray;


    public search() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.advance_search);

        //get search criteria
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
        String category = settings.getString("category", null);
        String price = settings.getString("price", null);
        String district = settings.getString("district", null);
        if(category != null && price != null) {
            try {
                categoryJsonArray = new JSONArray(category);
                priceJsonObject = new JSONObject(price);
                districtJsonArray = new JSONArray(district);
                System.out.println(categoryJsonArray);
                System.out.println(priceJsonObject);
                System.out.println(districtJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        title = getResources().getStringArray(R.array.advancedSearch_title);
        for(int i = 0; i < title.length; i++){
            SubmitForm comment = new SubmitForm();
            comment.setTitle(title[i]);
            comment.setSubmitWarning(false);
            items.add(comment);
        }

        //get the warning text
        warning = getResources().getStringArray(R.array.advancedSearch_warning);

        //get option detail
        try {
            createOptionDetail();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        button_search = (Button) view.findViewById(R.id.button_search);
        button_reset = (Button) view.findViewById(R.id.button_reset);

        final SubmitForm_CustomListAdapter customAdapter = new SubmitForm_CustomListAdapter(getActivity(), items, showDetailIndicator, null, warning);

        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
                AlertDialog dialog = null;

                switch (position) {
                    //地域
                    case 0:
                        final int tempSelectedAreaIndex = selectedAreaIndex;
                        selectedAreaName = items.get(position).getInfo().trim();

                        ab.setTitle(title[position]);
                        //setSingleChiceItems cannot support List of arrayList, so I need to convert it to array
                        ab.setSingleChoiceItems(array_lage_district.toArray(new String[array_lage_district.size()]), selectedAreaIndex, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedAreaIndex = which;
                                selectedAreaName = array_lage_district.get(which);

                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                areaSelection = array_district.get(selectedAreaIndex);
                                tempSelectedDistrictItem = new boolean[areaSelection.size()];
                                selectedDistrictItem = new boolean[areaSelection.size()];
                                //reset the district
                                items.get(1).setInfo("");
                                selectedDistrictName = new ArrayList<String>();

                                System.out.println(areaSelection);

                                items.get(position).setInfo(selectedAreaName);
                                //large_district_id = Integer.toString(selectedAreaIndex + 1);
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedAreaIndex = tempSelectedAreaIndex;
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();


                        break;
                    //地區
                    case 1:
                        ab.setTitle(title[position]);
                        ab.setMultiChoiceItems(areaSelection.toArray(new String[areaSelection.size()]), selectedDistrictItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                districtID = new ArrayList<>();
                                selectedDistrictName = new ArrayList<String>();
                                //list to be string
                                if(selectedDistrictItem != null) {
                                    //Just add select all string into list
                                    if(tempSelectedDistrictItem[0])
                                        selectedDistrictName.add(areaSelection.get(0));


                                    for (int i = 0; i < selectedDistrictItem.length; i++) {
                                        System.out.println(tempSelectedDistrictItem[i]);
                                        //add selected name into a selected list
                                        //add the final select ID into list
                                        if(tempSelectedDistrictItem[i]) {
                                            if(!tempSelectedDistrictItem[0])
                                                selectedDistrictName.add(areaSelection.get(i));
                                            //do not count Select all ID in district list
                                            if(i != 0)
                                                districtID.add(i);
                                        }
                                        //remove selected name from selected list
                                        else{
                                            if(!tempSelectedDistrictItem[0])
                                                selectedDistrictName.remove(areaSelection.get(i));
                                        }
                                        //put the selected list result into selected item
                                        selectedDistrictItem[i] = tempSelectedDistrictItem[i];
                                    }
                                }
                                //keep selected name string in history
                                items.get(position).setInfo(TextUtils.join(", ", selectedDistrictName).toString());

                                Log.d("districtID", districtID.toString());
                                customAdapter.notifyDataSetChanged();

                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(tempSelectedDistrictItem != null) {
                                    for (int i = 0; i < tempSelectedDistrictItem.length; i++) {
                                        tempSelectedDistrictItem[i] = selectedDistrictItem[i];
                                    }
                                }

                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        //call handler of checkbox to control select all function
                        tempSelectedDistrictItem = onDialog(dialog, selectedDistrictItem, tempSelectedDistrictItem);
                        break;
                    //cat
                    case 2:

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
                                    //Just add select all string into list
                                    if(tempSelectedCatItem[0])
                                        selectedCatName.add(hash_category.get(0));

                                    for (int i = 0; i < selectedCatItem.length; i++) {
                                        //add selected name into a selected list
                                        //add the final select ID into list
                                        if(tempSelectedCatItem[i]) {
                                            if(!tempSelectedCatItem[0])
                                                selectedCatName.add(hash_category.get(i));
                                            if(i != 0)
                                                categoryID.add(i);
                                        }
                                        //remove selected name from selected list
                                        else{
                                            if(!tempSelectedCatItem[0])
                                                selectedCatName.remove(hash_category.get(i));
                                        }
                                        //put the selected list result into selected item
                                        selectedCatItem[i] = tempSelectedCatItem[i];
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
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        tempSelectedCatItem = onDialog(dialog, selectedCatItem, tempSelectedCatItem);
                        break;
                    //expense
                    case 3:

                        ab.setTitle(title[position]);
                        ab.setMultiChoiceItems(arrayPrice.toArray(new String[arrayPrice.size()]), selectedExpenseItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //AlertDialog alertDialog = (AlertDialog) dialog;
                                //final ListView listView = alertDialog.getListView();

                                priceID = new ArrayList<>();
                                selectedExpenseName = new ArrayList<String>();

                                //list to be string
                                if(selectedExpenseItem != null) {

                                    //Just add select all string into list
                                    if(tempSelectedExpenseItem[0])
                                        selectedExpenseName.add(hash_Price.get(0));

                                    for (int i = 0; i < selectedExpenseItem.length; i++) {
                                        //add selected name into a selected list
                                        //add the final select ID into list

                                        if(tempSelectedExpenseItem[i]) {
                                            if(!tempSelectedExpenseItem[0])
                                                selectedExpenseName.add(hash_Price.get(i));
                                            if(i != 0)
                                                priceID.add(i);
                                        }
                                        //remove selected name from selected list
                                        else{
                                            if(!tempSelectedExpenseItem[0])
                                                selectedExpenseName.remove(hash_Price.get(i));
                                        }
                                        //put the selected list result into selected item
                                        selectedExpenseItem[i] = tempSelectedExpenseItem[i];
                                    }
                                }
                                //keep selected name string in history
                                items.get(position).setInfo(TextUtils.join(", ", selectedExpenseName).toString());

                                Log.d("priceID", priceID.toString());
                                customAdapter.notifyDataSetChanged();
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
                        tempSelectedExpenseItem = onDialog(dialog, selectedExpenseItem, tempSelectedExpenseItem);
                        break;
                }
            }
        });





        button_search.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {

                HashMap<Integer, HashMap<String, String >> hashDistrictID = new HashMap<Integer, HashMap<String, String>>();
                ArrayList<String> districtList = new ArrayList<String>();
                ArrayList<String> catList = new ArrayList<String>();
                //districtID
                if (selectedAreaName.equals("全選")) {
                    hashDistrictID = areaIdHkIsland;
                }
                else if (selectedAreaName.equals("香港島")) {
                    hashDistrictID = areaIdHkIsland;
                }
                else if (selectedAreaName.equals("九龍")) {
                    hashDistrictID = areaIdKowloon;
                }
                else if (selectedAreaName.equals("新界")) {
                    hashDistrictID = areaIdNewTerritories;
                }
                else if (selectedAreaName.equals("離島")) {
                    hashDistrictID = areaIdIslands;
                }

                for (int i = 0; i < districtID.size(); i++){
                    HashMap<String, String> hashKey =  hashDistrictID.get(districtID.get(i));
                    for (String key: hashKey.keySet())
                        districtList.add(key);
                }
                String districtString = TextUtils.join(",", districtList);
                System.out.println(districtString);

                //Category
                String catString = TextUtils.join(",", categoryID);
                System.out.println(catString);

                //price
                String priceString = TextUtils.join(",", priceID);
                System.out.println(priceString);



                /*//傳送數據去下一個fragment
                Bundle bundle = new Bundle();
                bundle.putBooleanArray("category_id", selectedCatItem);
                bundle.putBooleanArray("district_id", selectedDistrictItem);
                bundle.putBooleanArray("price_id", selectedExpenseItem);


                search_result search_result_fragment = new search_result();
                search_result_fragment.setArguments(bundle);
                //傳送數據去下一個fragment

                ((TestActivity) getActivity()).showBackButton();        //tomc 7/8/2016 actionbar button
                ((TestActivity) getActivity()).hideMenuButton();        //tomc 7/8/2016 actionbar button
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // FragmentManager fragmentManager = getFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search"));
                fragmentTransaction.add(R.id.frame_container, search_result_fragment, "search_result").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();*/

            }

        });


        button_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                selectedAreaIndex = -1;
                selectedAreaName = "";
                areaSelection = new ArrayList<String>();

                selectedDistrictItem = null;
                tempSelectedDistrictItem = null;
                selectedDistrictName = new ArrayList<>();

                selectedExpenseItem = new boolean[arrayPrice.size()];
                selectedExpenseName = new ArrayList<>();
                tempSelectedExpenseItem = new boolean[arrayPrice.size()];

                selectedCatItem = new boolean[array_category.size()];
                selectedCatName = new ArrayList<>();
                tempSelectedCatItem = new boolean[array_category.size()];

                for (int i= 0; i < items.size(); i++){
                    items.get(i).setInfo("");
                }
                customAdapter.notifyDataSetChanged();
            }

        });


        return view;
    }

    //to control the action of the multiple checkbox
    public boolean[] onDialog (AlertDialog dialog, final boolean[] items, final boolean[] selectedItem){


        final ListView listView = dialog.getListView();
        // ListView Item Click Listener that enables "Select all" choice
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isChecked = listView.isItemChecked(position);

                if(isChecked)
                    selectedItem[position] = true;
                else
                    selectedItem[position] = false;

                if (position == 0) {
                    if(selectAll) {
                        Log.d("HIHI", "HIHIHI");
                        for (int i = 1; i < items.length; i++) { // we start with first element after "Select all" choice
                            if (isChecked && !listView.isItemChecked(i)
                                    || !isChecked && listView.isItemChecked(i)) {
                                listView.performItemClick(listView, i, 0);
                                if(isChecked && !listView.isItemChecked(i))
                                    selectedItem[i] = true;
                                else if (!isChecked && listView.isItemChecked(i))
                                    selectedItem[i] = false;
                            }
                        }
                    }
                } else {
                    if (!isChecked && listView.isItemChecked(0)) {
                        Log.d("ababab", "ababab");
                        // if other item is unselected while "Select all" is selected, unselect "Select all"
                        // false, performItemClick, true is a must in order for this code to work
                        selectAll = false;
                        listView.performItemClick(listView, 0, 0);
                        selectedItem[0] = false;
                        selectAll = true;
                    }
                }
            }
        });
        return selectedItem;
    }

    private void createOptionDetail() throws JSONException {

        ArrayList<String> allArrayAreaName = new ArrayList<>();
        allArrayAreaName.add("全選");

        arrayPrice.add("全選");               //solve text color bug      2016/6/23 tomc
        hash_Price.put(0, "全選");            //hard code
        array_category.add("全選");           //solve text color bug     2016/6/23 tomc
        hash_category.put(0, "全選");
        //arrayPrice.add("全選");
        // array_category.add("全選");
        array_area_all.add("全選");


        for (int i = 1; i < categoryJsonArray.length(); i++) {

            JSONObject categoryDetail = (JSONObject) categoryJsonArray.get(i);
            String name = categoryDetail.getString("cht_name");
            String id = categoryDetail.getString("id");
            //  hashPrice.put(name, ID);
            //arrayPrice = new String[category.length()];
            array_category.add(name);
            hash_category.put(i, name);
        }
        //inital
        selectedCatItem = new boolean[array_category.size()];
        tempSelectedCatItem = new boolean[array_category.size()];

        for (int i = 1; i <= priceJsonObject.length(); i++) {
            String name = priceJsonObject.getString(String.valueOf(i));
            arrayPrice.add(name);
            hash_Price.put(i, name);
        }
        //inital
        selectedExpenseItem = new boolean[arrayPrice.size()];
        tempSelectedExpenseItem = new boolean[arrayPrice.size()];

        //create mapping for district and area
        for (int i = 0 ; i < districtJsonArray.length(); i++) {
            try {
                ArrayList<String> arrayAreaName = new ArrayList<>();
                arrayAreaName.add("全選");
                JSONObject districtDetail = (JSONObject) districtJsonArray.get(i);
                //get distrist
                String district_name = districtDetail.getString("district_name");
                array_lage_district.add(district_name);
                hash_large_district.put(i, district_name);
                //get area array

                JSONArray area = districtDetail.getJSONArray("area");
                for (int j = 0; j < area.length(); j++) {
                    HashMap<String, String> areaDetailHash = new HashMap<>();
                    JSONObject areaDetail = (JSONObject) area.get(j);
                    String area_name = areaDetail.getString("area_name");
                    String area_id = areaDetail.getString("id");

                    areaDetailHash.put(area_id, area_name);
                    allArrayAreaName.add(area_name);
                    areaIdAll.put(j + 1, areaDetailHash);

                    if (district_name.equals("香港島")) {
                        arrayAreaName.add(area_name);
                        areaIdHkIsland.put(j + 1, areaDetailHash);
                    }
                    if (district_name.equals("九龍")) {
                        arrayAreaName.add(area_name);
                        areaIdKowloon.put(j + 1, areaDetailHash);
                    }
                    if (district_name.equals("新界")) {
                        arrayAreaName.add(area_name);
                        areaIdNewTerritories.put(j + 1, areaDetailHash);
                    }
                    if (district_name.equals("離島")) {
                        arrayAreaName.add(area_name);
                        areaIdIslands.put(j + 1, areaDetailHash);
                    }
                }
                array_district.put(i, arrayAreaName);
            } catch (JSONException e) {
                Log.d("tom", "the first term全選");
            }
            array_district.put(0, allArrayAreaName);
        }
        Log.d("areaIdAll",areaIdAll.toString());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (jsonObjReq != null)  {
            jsonObjReq.cancel();
            Log.d("onStop", "Search requests are all cancelled");
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.d("search", "resume");
//        // Set title
//        getActivity()
//                .setTitle(R.string.advance_search);
//    }
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        Log.d("search", "setUserVisibleHint");
//        if(isVisibleToUser) {
//            // Set title
//            getActivity()
//                    .setTitle(R.string.advance_search);
//        }
//    }


}



