package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.company.damonday.R;

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
    private String[] title;
    private String[] titleRating;
    private RatingAdapter ratingAdapter;


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


        ArrayList<RowModel> list = new ArrayList<RowModel>();
        titleRating = getResources().getStringArray(R.array.writeComment_dialog_rating_detail);
        for(int i = 0; i < titleRating.length; i++){

            list.add(new RowModel(titleRating[i]));

        }

        ratingAdapter = new RatingAdapter(getActivity(), list);


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
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                System.out.println(position);
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                //new AlertDialog.Builder(getActivity(), R.style.Base_Theme_AppCompat);
                final EditText txtTitle = new EditText(getActivity());
                final EditText txtContent = new EditText(getActivity());
                final EditText txtExpense = new EditText(getActivity());
                txtExpense.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                txtExpense.setRawInputType(Configuration.KEYBOARD_12KEY);

                //get the dialog content
                String stringTitle = items.get(0).get("info").toString().trim();
                Log.d("stringTitle", stringTitle);

                if (stringTitle.equals(">")) {
                    txtTitle.setText("");
                } else {
                    txtTitle.setText(stringTitle);
                }

                String stringContent = items.get(1).get("info").toString().trim();
                if (!stringContent.equals(">")) {
                    txtContent.setText("");
                } else {
                    txtContent.setText(stringContent);
                }
                String stringExpense = items.get(2).get("info").toString().trim();
                if (!stringExpense.equals(">")) {
                    txtExpense.setText("");
                } else {
                    txtExpense.setText(stringExpense);
                }
                //txtExpense.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                switch (position) {

                    case 0:
                        ab.setTitle(R.string.writeComment_dialog_title);

                        ab.setView(txtTitle);

                        ab.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable title = txtTitle.getText();
                                //OR
                                String title = txtTitle.getText().toString().trim();
                                if (title.isEmpty())
                                    title = ">";
                                Log.d("title", title);
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
                                dialog.dismiss();
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
                                dialog.dismiss();
                            }
                        });

                        ab.show();
                        break;
                    case 3:
                        /*final Dialog rankDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
                        rankDialog.setContentView(R.layout.view_companywritecomment_rank_dialog);
                        rankDialog.setCancelable(true);
                        ratingbarListView = (ListView) rankDialog.findViewById(R.id.listView);
                        ratingbarListView.setAdapter(ratingAdapter);

                        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                        updateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println(getModel(1).g);
                                rankDialog.dismiss();
                            }
                        });
                        rankDialog.show();*/


                        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                        builderSingle.setIcon(R.drawable.ic_launcher);
                        builderSingle.setTitle(R.string.writeComment_dialog_rating);

                        builderSingle.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                System.out.println(getModel(1).getRating());

                            }
                        });

                        builderSingle.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setAdapter(
                                ratingAdapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        builderSingle.show();
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




