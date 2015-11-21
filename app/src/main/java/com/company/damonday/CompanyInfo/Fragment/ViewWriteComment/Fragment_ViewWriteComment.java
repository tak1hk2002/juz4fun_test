package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.company.damonday.R;
import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment extends Fragment {

    private View view;
    private ListView listView;
    private AccessToken accessToken;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private List<Map<String, Object>> itemsRating = new ArrayList<Map<String, Object>>();
    private String entId;
    private String[] title;
    private String[] titleRating;


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

        title = getResources().getStringArray(R.array.writeComment_title);
        for(int i = 0; i < title.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", title[i]);
            item.put("info", ">");
            items.add(item);
        }

        titleRating = getResources().getStringArray(R.array.writeComment_dialog_rating_detail);
        for(int i = 0; i < titleRating.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", titleRating[i]);
            item.put("ranking", "1");
            itemsRating.add(item);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.view_companywritecomment, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items,
                R.layout.view_companywritecomment_list, new String[] {"title", "info"}, new int[] {R.id.title, R.id.info});
        listView.setAdapter(simpleAdapter);
        setListViewHeightBasedOnChildren(listView);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                //new AlertDialog.Builder(getActivity(), R.style.Base_Theme_AppCompat);
                final EditText txtTitle = new EditText(getActivity());
                final EditText txtContent = new EditText(getActivity());
                final EditText txtExpense = new EditText(getActivity());
                txtExpense.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                txtExpense.setRawInputType(Configuration.KEYBOARD_12KEY);

                String stringTitle = items.get(0).get("info").toString().trim();
                if(!stringTitle.equals(">")){
                    txtTitle.setText(stringTitle);
                }
                else{
                    txtTitle.setText(">");
                }
                String stringContent = items.get(1).get("info").toString().trim();
                if(!stringContent.equals(">")){
                    txtContent.setText(stringContent);
                }
                else{
                    txtContent.setText(">");
                }
                String stringExpense = items.get(2).get("info").toString().trim();
                if(!stringExpense.equals(">")){
                    txtExpense.setText(stringExpense);
                }
                else{
                    txtExpense.setText(">");
                }
                //txtExpense.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                switch(position){

                    case 0:
                        ab.setTitle(R.string.writeComment_dialog_title);

                        ab.setView(txtTitle);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable title = txtTitle.getText();
                                //OR
                                String title = txtTitle.getText().toString().trim();
                                items.get(0).put("info", title);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        ab.show();
                        break;
                    case 1:
                        ab.setTitle(R.string.writeComment_dialog_content);

                        ab.setView(txtContent);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable content = txtContent.getText();
                                //OR
                                String content = txtContent.getText().toString().trim();
                                items.get(1).put("info", content);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        ab.show();

                        break;
                    case 2:
                        ab.setTitle(R.string.writeComment_dialog_consumption);

                        ab.setView(txtExpense);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable expense = txtExpense.getText();
                                //OR
                                String expense = txtExpense.getText().toString().trim();
                                items.get(2).put("info", expense);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        });

                        ab.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        ab.show();
                        break;
                    case 3:
                        final Dialog rankDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
                        rankDialog.setContentView(R.layout.view_companywritecomment_rank_dialog);
                        rankDialog.setCancelable(true);
                        ListView listView = (ListView) rankDialog.findViewById(R.id.listView);
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), itemsRating,
                                R.layout.view_companywritecommnet_rank_dialog_list, new String[] {"title", "ranking"},
                                new int[] {R.id.title_ranking, R.id.dialog_ratingbar});
                        simpleAdapter.setViewBinder(new MyBinder());
                        listView.setAdapter(simpleAdapter);
                        //final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                        //ratingBar.setRating(1);

                        //TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                        //text.setText("hi");

                        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                        updateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rankDialog.dismiss();
                            }
                        });
                        rankDialog.show();
                        break;
                    default:
                }

            }
        });


        return view;



    }



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




}

class MyBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if(view.getId() == R.id.dialog_ratingbar){
            String stringval = (String) data;
            System.out.println(stringval);
            float ratingValue = Float.parseFloat(stringval);
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(ratingValue);
            return true;
        }
        return false;
    }
}
