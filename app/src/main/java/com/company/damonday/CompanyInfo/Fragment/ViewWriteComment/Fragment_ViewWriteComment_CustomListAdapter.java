package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.damonday.R;

import java.util.List;

/**
 * Created by lamtaklung on 2/9/2016.
 */
public class Fragment_ViewWriteComment_CustomListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String warning[];
    private List<Fragment_ViewWriteComment_Comment> items;
    private List<Integer> showDetailIndicator;
    Fragment_ViewWriteComment_Comment m;

    public Fragment_ViewWriteComment_CustomListAdapter(Context context, List<Fragment_ViewWriteComment_Comment> items,
                                                       List<Integer> showDetailIndicator, String warning[]){
        this.context = context;
        this.items = items;
        this.showDetailIndicator = showDetailIndicator;
        this.warning = warning;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_companywritecomment_list, parent, false);

        EditText editInfo = (EditText) convertView.findViewById(R.id.editText);
        editInfo.addTextChangedListener(new GenericTextWatcher(editInfo));
        ImageView imgIndicator = (ImageView) convertView.findViewById(R.id.indicator);
        TextView txtInfo = (TextView) convertView.findViewById(R.id.info);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView warningInfo = (TextView) convertView.findViewById(R.id.warning);


        m = items.get(position);

        //popup window view
        if(showDetailIndicator.contains(position)) {
            //disappear editTexit
            editInfo.setVisibility(View.GONE);

            if(m.getInfo() == null || m.getInfo().length() == 0) {
                imgIndicator.setVisibility(View.VISIBLE);
                txtInfo.setVisibility(View.GONE);
            }
            else{
                imgIndicator.setVisibility(View.GONE);
                txtInfo.setVisibility(View.VISIBLE);
            }
        }
        //editText view
        else{
            imgIndicator.setVisibility(View.GONE);
            editInfo.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.GONE);
        }



        if(warning != null ) {
            //display hint in editText
            editInfo.setHint(warning[position]);
            //display warning message
            warningInfo.setText(warning[position]);
        }


        //check the writeComment form is submitted
        if(!m.getSubmitWarning())
            warningInfo.setVisibility(View.GONE);
        else{
            //submit but some fields are empty
            if(m.getInfo() == null || m.getInfo().length() == 0) {
                warningInfo.setVisibility(View.VISIBLE);
            }
        }

        txtInfo.setText(m.getInfo());

        title.setText(m.getTitle());

        editInfo.setText(m.getInfo());



        return convertView;
    }

    //save the editText content when text is changed
    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            final EditText editText = (EditText) view;
            m.setInfo(editText.getText().toString());
        }
    }
}
