
package com.company.damonday.Search;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.annotation.SuppressLint;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.graphics.Typeface;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.SearchView.OnQueryTextListener;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.VolleyLog;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.company.damonday.CompanyInfo.FragmentTabs_try;
        import com.company.damonday.R;
        import com.company.damonday.function.APIConfig;
        import com.company.damonday.function.AppController;
        import com.google.android.gms.analytics.ecommerce.Product;


public class search_fast extends Fragment {
    View view;
    View myFragmentView;
    SearchView search;
    ImageButton buttonBarcode;
    ImageButton buttonAudio;
    Typeface type;
    ListView searchResults;
    private List<CompanyObject> companyObjects = new ArrayList<CompanyObject>();
    String found = "N";
    private ProgressDialog pDialog;
    private SearchResultsAdapter adapter;


    //This arraylist will have data as pulled from server. This will keep cumulating.
    //ArrayList<search_fast_model> productResults = new ArrayList<search_fast_model>();
    //Based on the search string, only filtered products will be moved here from productResults
    ArrayList<search_fast_model> filteredProductResults = new ArrayList<search_fast_model>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public String geturl(String keyword){
       String geturl=APIConfig.URL_Fast_Search;

        geturl=geturl+"?keyword="+keyword;

        Log.d("geturl", geturl);
        return geturl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get the context of the HomeScreen Activity
        // final HomeScreen activity = (HomeScreen) getActivity();
        view = inflater.inflate(R.layout.search, container, false);     //tomc
        getActivity().getActionBar().setTitle(R.string.advance_search);     //tomc
        //define a typeface for formatting text fields and listview.

   //     type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/book.TTF");
        myFragmentView = inflater.inflate(R.layout.search_fast, container, false);

        search = (SearchView) myFragmentView.findViewById(R.id.searchView1);
        search.setQueryHint("Start typing to search...");

        searchResults = (ListView) myFragmentView.findViewById(R.id.listview_search);
        // buttonBarcode = (ImageButton) myFragmentView.findViewById(R.id.imageButton2);
        // buttonAudio = (ImageButton) myFragmentView.findViewById(R.id.imageButton1);


        //this part of the code is to handle the situation when user enters any search criteria, how should the
        //application behave?

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity(), String.valueOf(hasFocus), Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 1) {
                    Log.d("arraylen1", String.valueOf(filteredProductResults.size()));
                    searchResults.setVisibility(myFragmentView.VISIBLE);
                    Log.d("keyword", newText);


                    submitting(newText);

                    //myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(newText);
                } else {

                    searchResults.setVisibility(myFragmentView.INVISIBLE);
                    Log.d("arraylen2", String.valueOf(filteredProductResults.size()));
                    //filteredProductResults.clear();
                }


                return false;
            }

        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //pass the following object to next activity
                /*Intent i = new Intent(Ranking.this, FragmentTabs.class);
                i.putExtra("Ent_id", companyInfoItems.get(position).getEnt_id());
                startActivity(i);*/

                //pass object to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", filteredProductResults.get(position).getUser_id());
               Fragment fragmentTabs_try = new FragmentTabs_try();
                fragmentTabs_try.setArguments(bundle);


                FragmentManager fragmentManager = getFragmentManager();
                // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.frame_container));
                //fragmentTransaction.hide(getFragmentManager().findFragmentByTag("search_fast"));
                fragmentTransaction.add(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });



        return myFragmentView;
    }

    //this filters products from productResults and copies to filteredProductResults based on search text

//    public void filterProductArray(String newText) {
//
//        String pName;
//
//        filteredProductResults.clear();
//        for (int i = 0; i < productResults.size(); i++) {
//            pName = productResults.get(i).getTitle().toLowerCase();
//            if (pName.contains(newText.toLowerCase()))
//            // ||
//            //  productResults.get(i).getProductBarcode().contains(newText))
//            {
//                filteredProductResults.add(productResults.get(i));
//
//            }
//        }
//
//    }


    private void submitting(final String keyword) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("提交中 ...");
 //       showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                geturl(keyword), new Response.Listener<String>() {

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
                        JSONArray jArray = jObj.getJSONArray("data");
                        filteredProductResults.clear();
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject oneObject = jArray.getJSONObject(i);

                            String name = oneObject.getString("name");
                            String ID = oneObject.getString("ID");


                            search_fast_model model = new search_fast_model();
                            model.setTitle(name);
                            model.setUser_id(ID);


                            // adding movie to movies array
                           // productResults.add(model);  filteredProductResults.clear();
                            filteredProductResults.add(model);
                        }


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

                adapter=new SearchResultsAdapter(getActivity(), filteredProductResults);
                Log.d("hi","hi1");
                searchResults.setAdapter(adapter);
                Log.d("hi", "hi2");
                //adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FeedBack", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
         //       hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                // params.put("category_id", category_id);
                // params.put("district_id", district_id);
                // params.put("large_district_id", large_district_id);
                params.put("keyword", keyword);

                return params;
            }

        };
        // Adding request to request queue

        Log.d("keyword", keyword);
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



//
    class SearchResultsAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        private ArrayList<search_fast_model> productDetails = new ArrayList<search_fast_model>();
        int count;
       // Typeface type;
        Context context;

        //constructor method
        public SearchResultsAdapter(Context context, ArrayList<search_fast_model> product_details) {

            layoutInflater = LayoutInflater.from(context);

            this.productDetails = product_details;
            this.count = product_details.size();
            this.context = context;


        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int arg0) {
            return productDetails.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {




            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.search_fast_list_row, null);}

            TextView title = (TextView) convertView.findViewById(R.id.title);



            search_fast_model m = productDetails.get(position);

            title.setText(m.getTitle());


            return convertView;
        }



    }
}
