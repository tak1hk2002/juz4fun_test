package com.company.damonday.Search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.MyFavourites.MyFavouritesObject;
import com.company.damonday.MyFavourites.MyFavourites_adapter;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;
//import com.company.damonday.adapter.CustomListAdapter;
//import com.company.damonday.app.AppController;
//import com.company.damonday.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 21/6/15.
 */
public class SearchResult extends Fragment {
    private String price_id;
    private String district_id;
    private String category_id;
    private FragmentTabs_try fragmentTabs_try;
    private ImageView ivMyFavourite, ivLike, ivFair, ivDislike;
    private TextView noResult;

    private View view;

    // Log tag
    private static final String TAG = AdvancedSearch.class.getSimpleName();

    // Movies json url
    // private static final String url = "http://damonday.tk/api/comment/latest_comments/?page=1";
    private ProgressImage pDialog;
    private List<MyFavouritesObject> companyObjects = new ArrayList<MyFavouritesObject>();
    private ListView listView;
    private MyFavourites_adapter adapter;
    private String geturl = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//GET BUNDLE FROM LAST FRAGMENT
        Bundle bundle = this.getArguments();

        price_id = bundle.getString("price_id");
        district_id = bundle.getString("district_id");
        category_id = bundle.getString("category_id");
        geturl();

        Log.d("search", price_id + "+" + district_id  + "+" + category_id);

    }


    public String geturl() {
        ArrayList<String> parmsList = new ArrayList<>();
        String parmsString = "";

        geturl = APIConfig.URL_Advance_Search;
        if(!category_id.equals(""))
            parmsList.add("catId=" + category_id);
        if(!price_id.equals(""))
            parmsList.add("priceRange=" + price_id);
        if(!district_id.equals(""))
            parmsList.add("areaId=" + district_id);
        for(int i = 0; i < parmsList.size(); i++){
            if(i == 0){
                parmsString = "?";
            }
            parmsString += parmsList.get(i);

            if(i < (parmsList.size() - 1)){
                parmsString += "&";
            }
        }
        geturl = String.format(geturl + parmsString);

        Log.d("geturl", geturl);
        return geturl;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_result, container, false);
        //getActivity().getActionBar().setTitle(R.string.advance_search_result);
        getActivity().setTitle(R.string.advance_search_result);
        ((TestActivity) getActivity()).showBackButton();             //showbackbutton tomc 23/4/2016
        ((TestActivity) getActivity()).hideMenuButton();             //hideMenuButton tomc 23/4/2016
        pDialog = new ProgressImage(getActivity());
        pDialog.show();
        //setContentView(R.layout.latestcomment);
        submitting(category_id, district_id, price_id);
        listView = (ListView) view.findViewById(R.id.list);
        noResult = (TextView) view.findViewById(R.id.no_result);
        noResult.setText(R.string.search_no_result);
        adapter = new MyFavourites_adapter(getActivity(), companyObjects, true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", companyObjects.get(position).getEntId());
                fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);


                FragmentManager fragmentManager = getFragmentManager();
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search_result"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });


        return view;
    }


    private void submitting(final String category_id, final String district_id, final String price_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                geturl, new Response.Listener<String>() {

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
                        JSONArray jArray = jObj.getJSONArray("data");
                        if(jArray.length() > 0) {
                            noResult.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            if (jArray != null && jArray.length() > 0) {
                                for (int i = 0; i < jArray.length(); i++) {

                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    JSONObject score = oneObject.getJSONObject("score");
                                    String id = oneObject.getString("id");
                                    String coverImage = oneObject.getString("cover_image");
                                    String campanyName = oneObject.getString("company_name");
                                    String price_range = oneObject.getString("price_range");
                                    String average_score = score.getString("average_score");
                                    JSONArray category = oneObject.getJSONArray("cat");
                                    String name = oneObject.getString("name");
                                    String category_name = "";
                                    for (int j = 0; j < category.length(); j++) {
                                        String cat = category.getString(j);
                                        if (category_name != "")
                                            category_name += ", ";
                                        category_name += cat;
                                    }
                                    System.out.println("category_name: " + category_name);


//                            String category ="室內";  //tomc 26/20/2015
//                            String title ="真好玩工作室";

                                    String like = score.getString("like");
                                    String fair = score.getString("fair");
                                    String dislike = score.getString("dislike");
                                    //category
                                    //title


                                    MyFavouritesObject companyObject = new MyFavouritesObject();
                                    companyObject.setTitle(name);
                                    companyObject.setCompanyName(campanyName);
                                    companyObject.setCategory(category_name);
                                    companyObject.setEntId(id);
                                    companyObject.setPicUrl(coverImage);
                                    companyObject.setAverageScore(average_score);
                                    companyObject.setLike(like);
                                    companyObject.setDislike(dislike);
                                    companyObject.setFair(fair);
                                    companyObject.setPrice(price_range);


                                    // adding movie to movies array
                                    companyObjects.add(companyObject);
                                }
                            }
                        }
                        else{
                            noResult.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }


                    } else if (status == 0) {
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
                adapter.notifyDataSetChanged();

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("catId", category_id);
                params.put("priceRange", price_id);
                params.put("areaId", district_id);


                return params;
            }

        };
        // Adding request to request queue
        Log.d("strReq", strReq.toString());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialog();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


    private void hideDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//       // getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

}




