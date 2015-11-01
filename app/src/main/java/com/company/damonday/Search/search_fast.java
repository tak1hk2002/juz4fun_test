
package com.company.damonday.Search;

        import java.util.ArrayList;
        import java.util.HashMap;
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
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
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
//    //in this myAsyncTask, we are fetching data from server for the search string entered by user.
//    class myAsyncTask extends AsyncTask<String, Void, String>
//    {
//        JSONParser jParser;
//        JSONArray productList;
//        String url=new String();
//        String textSearch;
//        ProgressDialog pd;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            productList=new JSONArray();
//            jParser = new JSONParser();
//            pd= new ProgressDialog(getActivity());
//            pd.setCancelable(false);
//            pd.setMessage("Searching...");
//            pd.getWindow().setGravity(Gravity.CENTER);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... sText) {
//
//            url="http://lawgo.in/lawgo/products/user/1/search/"+sText[0];
//            String returnResult = getProductList(url);
//            this.textSearch = sText[0];
//            return returnResult;
//
//        }
//
//        public String getProductList(String url)
//        {
//
//            Product tempProduct = new Product();
//            String matchFound = "N";
//            //productResults is an arraylist with all product details for the search criteria
//            //productResults.clear();
//
//
//            try {
//
//
//                JSONObject json = jParser.getJSONFromUrl(url);
//
//                productList = json.getJSONArray("ProductList");
//
//                //parse date for dateList
//                for(int i=0;i<productList.length();i++)
//                {
//                    tempProduct = new Com();
//
//                    JSONObject obj=productList.getJSONObject(i);
//
//                   // tempProduct.setProductCode(obj.getString("ProductCode"));
//                    tempProduct.setProductName(obj.getString("ProductName"));
//                    tempProduct.setProductGrammage(obj.getString("ProductGrammage"));
//                    tempProduct.setProductBarcode(obj.getString("ProductBarcode"));
//                    tempProduct.setProductDivision(obj.getString("ProductCatCode"));
//                    tempProduct.setProductDepartment(obj.getString("ProductSubCode"));
//                    tempProduct.setProductMRP(obj.getString("ProductMRP"));
//                    tempProduct.setProductBBPrice(obj.getString("ProductBBPrice"));
//
//                    //check if this product is already there in productResults, if yes, then don't add it again.
//                    matchFound = "N";
//
//                    for (int j=0; j < productResults.size();j++)
//                    {
//
//                        if (productResults.get(j).getProductCode().equals(tempProduct.getProductCode()))
//                        {
//                            matchFound = "Y";
//                        }
//                    }
//
//                    if (matchFound == "N")
//                    {
//                        productResults.add(tempProduct);
//                    }
//
//                }
//
//                return ("OK");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ("Exception Caught");
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//            if(result.equalsIgnoreCase("Exception Caught"))
//            {
//                Toast.makeText(getActivity(), "Unable to connect to server,please try later", Toast.LENGTH_LONG).show();
//
//                pd.dismiss();
//            }
//            else
//            {
//
//
//                //calling this method to filter the search results from productResults and move them to
//                //filteredProductResults
//                filterProductArray(textSearch);
//                searchResults.setAdapter(new SearchResultsAdapter(getActivity(),filteredProductResults));
//                pd.dismiss();
//            }
//        }
//
//    }
//}
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
         //   type = Typeface.createFromAsset(context.getAssets(), "fonts/book.TTF");

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
