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

    private String price_id;
    private String district_id;
    private String large_district_id;
    private String category_id;

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
                AlertDialog dialog;

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
                        String districtName = items.get(position).getInfo();
                        if(!districtName.equals("")) {
                            selectedDistrictName = new ArrayList(Arrays.asList(districtName.split(", ")));
                        }

                        ab.setTitle(title[position]);
                        ab.setMultiChoiceItems(areaSelection.toArray(new String[areaSelection.size()]), selectedDistrictItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    selectedDistrictName.add(areaSelection.get(indexSelected));

                                } else  {
                                    selectedDistrictName.remove(areaSelection.get(indexSelected));
                                }
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                items.get(position).setInfo(TextUtils.join(", ", selectedDistrictName).toString());
                                if(selectedDistrictItem != null) {
                                    for (int i = 0; i < selectedDistrictItem.length; i++) {
                                        tempSelectedDistrictItem[i] = selectedDistrictItem[i];
                                    }
                                }

                                Log.d("selectedDistrictName", selectedDistrictName.toString());
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(tempSelectedDistrictItem != null) {
                                    for (int i = 0; i < tempSelectedDistrictItem.length; i++) {
                                        selectedDistrictItem[i] = tempSelectedDistrictItem[i];
                                    }
                                }

                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        break;
                    //cat
                    case 2:
                        String catName = items.get(position).getInfo();
                        if(!catName.equals("")) {
                            selectedCatName = new ArrayList(Arrays.asList(catName.split(", ")));
                        }

                        ab.setTitle(title[position]);
                        ab.setMultiChoiceItems(array_category.toArray(new String[array_category.size()]), selectedCatItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    selectedCatName.add(hash_category.get(indexSelected));

                                } else  {
                                    selectedCatName.remove(hash_category.get(indexSelected));
                                }
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                items.get(position).setInfo(TextUtils.join(", ", selectedCatName).toString());
                                if(selectedCatItem != null) {
                                    for (int i = 0; i < selectedCatItem.length; i++) {
                                        tempSelectedCatItem[i] = selectedCatItem[i];
                                    }
                                }
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(tempSelectedCatItem != null) {
                                    for (int i = 0; i < tempSelectedCatItem.length; i++) {
                                        selectedCatItem[i] = tempSelectedCatItem[i];
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        break;
                    //expense
                    case 3:
                        String expenseName = items.get(position).getInfo();
                        if(!expenseName.equals("")) {
                            selectedExpenseName = new ArrayList(Arrays.asList(expenseName.split(", ")));
                        }

                        ab.setTitle(title[position]);
                        ab.setMultiChoiceItems(arrayPrice.toArray(new String[arrayPrice.size()]), selectedExpenseItem, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    selectedExpenseName.add(hash_Price.get(indexSelected));

                                } else  {
                                    selectedExpenseName.remove(hash_Price.get(indexSelected));
                                }
                            }
                        });
                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                items.get(position).setInfo(TextUtils.join(", ", selectedExpenseName).toString());
                                if(selectedExpenseItem != null) {
                                    for (int i = 0; i < selectedExpenseItem.length; i++) {
                                        tempSelectedExpenseItem[i] = selectedExpenseItem[i];
                                    }
                                }
                                customAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(tempSelectedExpenseItem != null) {
                                    for (int i = 0; i < tempSelectedExpenseItem.length; i++) {
                                        selectedExpenseItem[i] = tempSelectedExpenseItem[i];
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog = ab.create();
                        dialog.show();
                        break;
                }
            }
        });



        button_search.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {



                //傳送數據去下一個fragment
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
                fragmentTransaction.commit();

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

    private void createOptionDetail() throws JSONException {

        ArrayList<String> allArrayAreaName = new ArrayList<>();
        allArrayAreaName.add("全選");

        arrayPrice.add("全選");               //solve text color bug      2016/6/23 tomc
        hash_Price.put(0, "全選");            //hard code
        array_category.add("全選");           //solve text color bug     2016/6/23 tomc
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
                    areaIdAll.put(array_district.size() - 1, areaDetailHash);

                    if (district_name.equals("香港島")) {
                        arrayAreaName.add(area_name);
                        areaIdHkIsland.put(j, areaDetailHash);
                    }
                    if (district_name.equals("九龍")) {
                        arrayAreaName.add(area_name);
                        areaIdKowloon.put(j, areaDetailHash);
                    }
                    if (district_name.equals("新界")) {
                        arrayAreaName.add(area_name);
                        areaIdNewTerritories.put(j, areaDetailHash);
                    }
                    if (district_name.equals("離島")) {
                        arrayAreaName.add(area_name);
                        areaIdIslands.put(j, areaDetailHash);
                    }
                }
                array_district.put(i, arrayAreaName);
            } catch (JSONException e) {
                Log.d("tom", "the first term全選");
            }
            array_district.put(0, allArrayAreaName);
        }
        Log.d("array_district",array_district.toString());
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



