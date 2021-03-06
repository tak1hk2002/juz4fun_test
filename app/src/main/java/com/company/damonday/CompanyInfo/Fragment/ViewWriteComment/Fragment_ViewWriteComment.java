package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.appyvet.rangebar.RangeBar;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.Framework.ScrollView.ScrollView;
import com.company.damonday.Framework.SubmitForm.SubmitForm;
import com.company.damonday.Framework.SubmitForm.SubmitForm_CustomListAdapter;
import com.company.damonday.Home.Home;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;
import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment extends Fragment {

    private View view;
    private ListView listView;
    private List<SubmitForm> items = new ArrayList<SubmitForm>();
    private String entId;
    private String[] title, titleDetailRating, titleOverAllRating, warning;
    private RatingAdapter ratingAdapter;
    private ProgressImage pDialog;
    private static final String TAG = Fragment_ViewWriteComment.class.getSimpleName();
    private SessionManager session;
    private LoginSQLiteHandler DB;
    private Boolean submitWarning = false;
    private Boolean systemLogin;
    //map the selected overall rating to key value
    private int selectedOverallRatingItem = -1;
    private int tempSelectedOverallRatingItem = -1;
    private int overallRating;
    private String selectedOverallRatingName;


    private Map<String, String> mapDetailRating = new HashMap<String, String>();
    private StringRequest strReq;
    //內容，消費，整體評分，詳細評分 show indicator
    private List<Integer> showDetailIndicator = Arrays.asList(1,3,4);
    private List<Integer> showEditText = Arrays.asList();
    private List<Integer> numericEditText = Arrays.asList(2);
    private String detailRatingDB[];
    private ScrollView doubleScroll = new ScrollView();



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
            SubmitForm comment = new SubmitForm();
            comment.setTitle(title[i]);
            comment.setSubmitWarning(false);
            items.add(comment);
        }

        //get the array list of rating bar option
        ArrayList<RowModel> list = new ArrayList<RowModel>();
        titleDetailRating = getResources().getStringArray(R.array.writeComment_dialog_rating_detail);
        for(int i = 0; i < titleDetailRating.length; i++){

            list.add(new RowModel(titleDetailRating[i]));

        }


        //get the warning text
        warning = getResources().getStringArray(R.array.writeComment_warning);

        //get the array list of overall rating
        titleOverAllRating = getResources().getStringArray(R.array.writeComment_dialog_rating_overall);

        //get the db name of detail rating
        detailRatingDB = getResources().getStringArray(R.array.writeComment_dialog_rating_detail_db);


        ratingAdapter = new RatingAdapter(getActivity(), list);


        DB = new LoginSQLiteHandler(getActivity());

        // Progress dialog
        pDialog = new ProgressImage(getActivity());




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Boolean[] ratingBarClicked = {false};
        view = inflater.inflate(R.layout.view_companywritecomment, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        Button btnReset = (Button) view.findViewById(R.id.reset);
        Button btnSubmit = (Button) view.findViewById(R.id.submit);
        final SubmitForm_CustomListAdapter customAdapter = new SubmitForm_CustomListAdapter(getActivity(), items, showDetailIndicator,
                                                                                            showEditText, numericEditText, warning);
        listView.setAdapter(customAdapter);
        doubleScroll.setListViewHeightBasedOnChildren2(listView);

        btnReset.setOnClickListener(new View.OnClickListener() {
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
                listView.setAdapter(customAdapter);
                doubleScroll.setListViewHeightBasedOnChildren2(listView);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //init
                String submitVars[] = new String[items.size()];
                String token = "";
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
                    //check facebook login or system login
                    session = new SessionManager(getActivity());
                    //if (AccessToken.getCurrentAccessToken() != null) {
                        //token = AccessToken.getCurrentAccessToken().toString();
                    //} else if (session.isLoggedIn()) {
                        //systemLogin = true;
                        HashMap<String, String> user = DB.getUserDetails();
                        token = user.get("token");
                    //}
                    System.out.println(entId);
                    System.out.println(token);
                    System.out.println(submitVars[0]);
                    Log.d("content", submitVars[1]);
                    System.out.println(submitVars[2]);
                    System.out.println(overallRating);
                    System.out.println(mapDetailRating);
                    submitWriteComment(token, submitVars[0], submitVars[1], submitVars[2],
                            overallRating, mapDetailRating);

                }

                customAdapter.notifyDataSetChanged();
                //update the height of the listView
                listView.setAdapter(customAdapter);
                doubleScroll.setListViewHeightBasedOnChildren2(listView);
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


                switch (position) {
                    //content
                    case 1:
                        //get the dialog content
                        String stringContent = "";
                        if(items.get(position).getInfo() != null)
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
                    //Overall Rating
                    case 3:
                        ab[0].setTitle(R.string.writeComment_dialog_overallrating);
                        ab[0].setSingleChoiceItems(titleOverAllRating, selectedOverallRatingItem, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedOverallRatingItem = which;
                                selectedOverallRatingName = titleOverAllRating[which];

                            }
                        });
                        ab[0].setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                tempSelectedOverallRatingItem = selectedOverallRatingItem;
                                overallRating = selectedOverallRatingItem;
                                switch(overallRating){
                                    case 0: //Good
                                        overallRating = 1;
                                        break;
                                    case 1: //normal
                                        overallRating = 2;
                                        break;
                                    case 2: //bad
                                        overallRating = 3;
                                        break;
                                }
                                items.get(position).setInfo(selectedOverallRatingName);
                                customAdapter.notifyDataSetChanged();
                                //listView.setAdapter(customAdapter);
                                //setListViewHeightBasedOnChildren(listView);
                            }
                        });

                        ab[0].setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedOverallRatingItem = tempSelectedOverallRatingItem;
                                dialog.dismiss();
                            }
                        });
                        ab[0].show();
                        break;
                    //Detail rating
                    case 4:
                        ab[0].setTitle(R.string.writeComment_dialog_detailrating);

                        ab[0].setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ratingBarClicked[0] = true;

                                //Calculate average of detail rating
                                int total_rank = 0;
                                for (int i = 0; i < titleDetailRating.length; i++) {
                                    total_rank = total_rank + getModel(i).getRange();
                                    mapDetailRating.put(detailRatingDB[i], Integer.toString(getModel(i).getRange()));
                                }
                                NumberFormat nf = NumberFormat.getInstance();
                                nf.setMaximumFractionDigits(1);
                                String average = String.format("%.1f", (double) total_rank / (double) titleDetailRating.length);

                                //Store the average to items
                                items.get(position).setInfo(average);
                                customAdapter.notifyDataSetChanged();
                                //listView.setAdapter(customAdapter);
                                //setListViewHeightBasedOnChildren(listView);
                            }
                        });

                        ab[0].setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        ab[0].setAdapter(
                                ratingAdapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        ab[0].show();
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

        //编写ListView中每个单元的呈现
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewWrapper wrapper;
            RangeBar rangeBar = null;
            //步骤2.3：如果没有创建View，根据layout创建之，并将widget的存储类的对象与之捆绑为tag
            if(row == null){
                LayoutInflater inflater=(LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.view_companywritecomment_rank_dialog_list, parent,false);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
                //步骤2.4：在生成View的时候，添加将widget的触发处理
                rangeBar = wrapper.getRangeBar();
                rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                    @Override
                    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                        //步骤2.4.1：存储变化的数据
                        Integer index = (Integer) rangeBar.getTag();
                        RowModel model = getModel(index);
                        model.range = rightPinIndex;
                        //步骤2.4.2：设置变化
                        LinearLayout parent = (LinearLayout) rangeBar.getParent();
                        TextView label = (TextView) parent.findViewById(R.id.title_ranking);
                        label.setText(model.getLabel());
                    }
                });
            }else{ //步骤2.4：利用已有的View，获得相应的widget
                wrapper = (ViewWrapper) row.getTag();
                rangeBar = wrapper.getRangeBar();
            }
            //步骤2.5：设置显示的内容，同时设置ratingbar捆绑tag为list的位置，因为setTag()是View的方法，因此我们不能降至加在ViewWrapper，所以需要加载ViewWrapper中的widget中，这里选择了ratebar进行捆绑。
            RowModel model= getModel(position);
            wrapper.getLabel().setText(model.getLabel());
            rangeBar.setTag(new Integer(position));
            rangeBar.setSeekPinByIndex(model.range);
            return row;
        }



    }

    //********************************************************************************************

    //***********************JSON volley**********************************************************
    public void submitWriteComment(final String token, final String title, final String content, final String expense, final int overallRating, final Map<String, String> detailRating){
        // Tag used to cancel the request
        String tag_string_req = "submitWriteComment";
        showDialog();

        strReq = new StringRequest(Request.Method.POST,
                APIConfig.URL_Write_Comment, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("response", response.toString());

                try{
                    JSONObject jObj = new JSONObject(response);
                    int status = jObj.getInt("status");
                    // Check for error node in json
                    if (status == 1) {

                        AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_DARK);
                        ab.setTitle(R.string.submit_success);
                        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                             //(refresh)go to company detail
                                Bundle bundle = new Bundle();
                                bundle.putString("ent_id", entId);
                                FragmentTabs_try fragmentTabs_try = new FragmentTabs_try();
                                fragmentTabs_try.setArguments(bundle);


                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentManager.popBackStack();
                                fragmentTransaction.replace(R.id.frame_container, fragmentTabs_try, "companyDetail").addToBackStack(null);
                                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fragmentTransaction.commit();

                            }
                        });
                        ab.create().show();


                    }else if (status == 0) {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");

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
                System.out.println("token: "+ token);
                System.out.println("title: "+ title);
                System.out.println("entId: "+ entId);
                System.out.println("comment: "+ content);
                System.out.println("assess: "+ Integer.toString(overallRating));
                System.out.println("price: "+ expense);
                params.put("token", token);
                params.put("title", title);
                params.put("entId", entId);
                params.put("comment", content);
                params.put("assess", Integer.toString(overallRating));
                params.put("price", expense);
                for (int i = 0; i < detailRating.size(); i++){
                    params.put(detailRatingDB[i], detailRating.get(detailRatingDB[i]));
                    System.out.println(detailRatingDB[i]+": "+ detailRating.get(detailRatingDB[i]));
                }
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //******************************************************************************************


    @Override
    public void onStop() {
        super.onStop();
        if (strReq != null)  {
            strReq.cancel();
            Log.d("onStop", "Write comment requests are all cancelled");
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




