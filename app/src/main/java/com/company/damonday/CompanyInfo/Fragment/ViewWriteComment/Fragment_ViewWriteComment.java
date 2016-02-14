package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment extends Fragment {

    private View view;
    private ListView listView;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private String entId;
    private String[] title, titleDetailRating, titleOverAllRating;
    private RatingAdapter ratingAdapter;
    private ProgressDialog pDialog;
    private static final String TAG = Fragment_ViewWriteComment.class.getSimpleName();
    private SessionManager session;
    private LoginSQLiteHandler DB;
    private Boolean systemLogin;
    //map the selected overall rating to key value
    Map<String, Integer> mapOverallRating = new HashMap<String, Integer>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //get previous entid
        try {
            entId = getArguments().getString("ent_id");
            Log.d("entId", entId);
        }catch(Exception e) {
            e.printStackTrace();
        }

        //get the array list of writeComment option
        title = getResources().getStringArray(R.array.writeComment_title);
        for(int i = 0; i < title.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", title[i]);
            item.put("info", ">");
            items.add(item);
        }

        //get the array list of rating bar option
        ArrayList<RowModel> list = new ArrayList<RowModel>();
        titleDetailRating = getResources().getStringArray(R.array.writeComment_dialog_rating_detail);
        for(int i = 0; i < titleDetailRating.length; i++){

            list.add(new RowModel(titleDetailRating[i]));

        }

        //get the array list of overall rating
        titleOverAllRating = getResources().getStringArray(R.array.writeComment_dialog_rating_overall);

        ratingAdapter = new RatingAdapter(getActivity(), list);

        mapOverallRating.put("正評", 0);
        mapOverallRating.put("一般", 1);
        mapOverallRating.put("負評", 2);

        DB = new LoginSQLiteHandler(getActivity());

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Boolean[] ratingBarClicked = {false};
        view = inflater.inflate(R.layout.view_companywritecomment, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        Button btnReset = (Button) view.findViewById(R.id.reset);
        Button btnSubmit = (Button) view.findViewById(R.id.submit);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items,
                R.layout.view_companywritecomment_list, new String[] {"title", "info"}, new int[] {R.id.title, R.id.info});
        listView.setAdapter(simpleAdapter);
        setListViewHeightBasedOnChildren(listView);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < items.size(); i++){
                    items.get(i).put("info", ">");
                }
                simpleAdapter.notifyDataSetChanged();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //init
                String submitVars[] = new String[items.size() - 1];
                String token = "";
                ArrayList<String> rating = new ArrayList<String>();
                Boolean passChecking = true;
                String warning[] = getResources().getStringArray(R.array.writeComment_warning);
                //------------------------------------------------------------------------

                //without detailRating because it will be obtained individually
                for (int i = 0; i < items.size() - 1; i++){
                    //get the value that the user inputted
                    submitVars[i] = items.get(i).get("info").toString().trim();
                    if(submitVars[i].equals(">"))
                        submitVars[i] = "";
                    if(submitVars[i].isEmpty()){
                        Toast.makeText(getActivity(), warning[i], Toast.LENGTH_SHORT).show();
                        passChecking = false;
                    }
                }

                //hardcode detail rating
                if(ratingBarClicked[0] == false) {
                    Toast.makeText(getActivity(), warning[4], Toast.LENGTH_SHORT).show();
                    passChecking = false;
                }

                //get rating bar value
                for(int i =0; i < titleDetailRating.length; i++){
                    rating.add(Float.toString(getModel(i).getRating()));
                }

                if(passChecking){
                    //check facebook login or system login
                    session = new SessionManager(getActivity());
                    if(AccessToken.getCurrentAccessToken() != null) {
                        token = AccessToken.getCurrentAccessToken().toString();
                    }
                    else if (session.isLoggedIn()) {
                        systemLogin = true;
                        HashMap<String, String> user = DB.getUserDetails();
                        token = user.get("token");
                    }
                    System.out.println(submitVars[0]);
                    System.out.println(submitVars[1]);
                    System.out.println(submitVars[2]);
                    System.out.println(submitVars[3]);
                    System.out.println(rating);
                    submitWriteComment(token, submitVars[0], submitVars[1], submitVars[2],
                            Integer.toString(mapOverallRating.get(submitVars[3])), rating);

                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder[] ab = {new AlertDialog.Builder(getActivity())};
                AlertDialog dialog;
                final EditText txtTitle = new EditText(getActivity());
                final EditText txtContent = new EditText(getActivity());
                final EditText txtExpense = new EditText(getActivity());
                final String[] txtOverAllRating = new String[1];
                final int[] selectedOverallRating = {-1};

                //only allow user to enter digits and "."
                txtExpense.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

                //get the dialog content
                String stringTitle = items.get(position).get("info").toString().trim();
                if (stringTitle.equals(">")) {
                    txtTitle.setText("");
                } else {
                    txtTitle.setText(stringTitle);
                }
                //get the dialog content
                String stringContent = items.get(position).get("info").toString().trim();
                if (stringContent.equals(">")) {
                    txtContent.setText("");
                } else {
                    txtContent.setText(stringContent);
                }
                //get the dialog content
                String stringExpense = items.get(position).get("info").toString().trim();
                if (stringExpense.equals(">")) {
                    txtExpense.setText("");
                } else {
                    txtExpense.setText(stringExpense);
                }

                //get the dialog content
                String stringOverAllRating = items.get(position).get("info").toString().trim();
                if (stringOverAllRating.equals(">")) {
                    txtOverAllRating[0] = "";
                } else {
                    txtOverAllRating[0] = stringOverAllRating;
                    if(mapOverallRating.get(stringOverAllRating) != null)
                        selectedOverallRating[0] = mapOverallRating.get(stringOverAllRating);
                }

                switch (position) {
                    //title
                    case 0:
                        ab[0].setTitle(R.string.writeComment_dialog_title);

                        ab[0].setView(txtTitle);

                        ab[0].setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable title = txtTitle.getText();
                                //OR
                                String title = txtTitle.getText().toString().trim();
                                if (title.isEmpty())
                                    title = ">";
                                items.get(position).put("info", title);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab[0].setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        //when alertview is launched, the keyboard show immediately
                        dialog = ab[0].create();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                        dialog.show();
                        break;
                    //content
                    case 1:
                        ab[0].setTitle(R.string.writeComment_dialog_content);

                        ab[0].setView(txtContent);

                        ab[0].setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable content = txtContent.getText();
                                //OR
                                String content = txtContent.getText().toString().trim();
                                if (content.isEmpty())
                                    content = ">";
                                items.get(position).put("info", content);
                                simpleAdapter.notifyDataSetChanged();
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
                    //consumption
                    case 2:
                        ab[0].setTitle(R.string.writeComment_dialog_consumption);

                        ab[0].setView(txtExpense);

                        ab[0].setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable expense = txtExpense.getText();
                                //OR
                                String expense = txtExpense.getText().toString().trim();
                                if (expense.isEmpty())
                                    expense = ">";
                                items.get(position).put("info", expense);
                                simpleAdapter.notifyDataSetChanged();
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
                    //Overall Rating
                    case 3:
                        final AlertDialog.Builder builderOverALlRating = new AlertDialog.Builder(getActivity());
                        builderOverALlRating.setTitle(R.string.writeComment_dialog_overallrating);
                        builderOverALlRating.setSingleChoiceItems(titleOverAllRating, selectedOverallRating[0], new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        txtOverAllRating[0] = titleOverAllRating[which];
                                        selectedOverallRating[0] = which;
                                        break;
                                    case 1:
                                        txtOverAllRating[0] = titleOverAllRating[which];
                                        selectedOverallRating[0] = which;
                                        break;
                                    case 2:
                                        txtOverAllRating[0] = titleOverAllRating[which];
                                        selectedOverallRating[0] = which;
                                        break;
                                }

                            }
                        });
                        builderOverALlRating.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                items.get(position).put("info", txtOverAllRating[0]);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        builderOverALlRating.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderOverALlRating.show();
                        break;
                    //Detail rating
                    case 4:

                        final AlertDialog.Builder builderDetailRating = new AlertDialog.Builder(getActivity());
                        //builderSingle.setIcon(R.drawable.ic_launcher);
                        builderDetailRating.setTitle(R.string.writeComment_dialog_detailrating);

                        builderDetailRating.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                System.out.println(getModel(1).getRating());
                                ratingBarClicked[0] = true;

                            }
                        });

                        builderDetailRating.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderDetailRating.setAdapter(
                                ratingAdapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        builderDetailRating.show();
                        break;
                    default:
                }

            }
        });


        return view;



    }


    //***************** RatingAdapter***************************************
    private RowModel getModel(int position){
        return (RowModel)ratingAdapter.getItem(position);
    }

    public class RatingAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<RowModel> list;
        public RatingAdapter(Context context, ArrayList<RowModel> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //步骤2.2： 编写ListView中每个单元的呈现
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewWrapper wrapper;
            RatingBar ratebar = null;
            //步骤2.3：如果没有创建View，根据layout创建之，并将widget的存储类的对象与之捆绑为tag
            if(row == null){
                LayoutInflater inflater=(LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.view_companywritecomment_rank_dialog_list, parent,false);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
                //步骤2.4：在生成View的时候，添加将widget的触发处理
                ratebar = wrapper.getRatingBar();
                ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        //步骤2.4.1：存储变化的数据
                        Integer index = (Integer)ratingBar.getTag();
                        RowModel model = getModel(index);
                        model.rating = rating;
                        //步骤2.4.2：设置变化
                        LinearLayout parent = (LinearLayout)ratingBar.getParent();
                        TextView label = (TextView)parent.findViewById(R.id.title_ranking);
                        label.setText(model.getLabel());
                    }
                });
            }else{ //步骤2.4：利用已有的View，获得相应的widget
                wrapper = (ViewWrapper) row.getTag();
                ratebar = wrapper.getRatingBar();
            }
            //步骤2.5：设置显示的内容，同时设置ratingbar捆绑tag为list的位置，因为setTag()是View的方法，因此我们不能降至加在ViewWrapper，所以需要加载ViewWrapper中的widget中，这里选择了ratebar进行捆绑。
            RowModel model= getModel(position);
            wrapper.getLabel().setText(model.getLabel());
            ratebar.setTag(new Integer(position));
            ratebar.setRating(model.rating);
            return row;
        }



    }

    //********************************************************************************************

    //***********************JSON volley**********************************************************
    public void submitWriteComment(final String token, final String title, final String content, final String expense, final String overallRating, final ArrayList<String> detailRating){
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        final String detailRatingDB[] = getResources().getStringArray(R.array.writeComment_dialog_rating_detail_db);
        pDialog.setMessage("提交中 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_Write_Comment, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("response", response.toString());

                try{
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("status");
                    // Check for error node in json
                    if (error.equals("success")) {


                        //display message that the submission is successful
                        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                        ab.setTitle(R.string.writeComment_submit_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ab.create().show();


                    }else {
                        // Error in login. Get the error message
                        JSONObject data = jObj.getJSONObject("data");
                        String errorMsg = data.getString("msg");

                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("title", title);
                params.put("ent_id", entId);
                params.put("comment", content);
                params.put("assess", overallRating);
                for (int i = 0; i < detailRating.size(); i++){
                    params.put(detailRatingDB[i], detailRating.get(i));
                }
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //******************************************************************************************



    //doulbe scrollView
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        SimpleAdapter listAdapter = (SimpleAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        /*int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < (listAdapter.getCount()); i++)
        {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();*/
        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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




