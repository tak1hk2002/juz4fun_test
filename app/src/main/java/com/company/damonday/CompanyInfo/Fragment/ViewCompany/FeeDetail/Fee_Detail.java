package com.company.damonday.CompanyInfo.Fragment.ViewCompany.FeeDetail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by lamtaklung on 8/5/2016.
 */
public class Fee_Detail extends Fragment {

    private String entId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        try {
            //get id from previous page
            entId = getArguments().getString("ent_id");
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "No data loaded", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TableLayout tableLayout = new TableLayout(getActivity());
        TableRow row1 = getTableRow1();
        tableLayout.addView(row1);

        TableRow row2 = getTableRow2();
        tableLayout.addView(row2);

        return tableLayout;
    }

    public TableRow getTableRow1(){
        TableRow tableRow = new TableRow(getActivity());

        TextView name = new TextView(getActivity());
        name.setText("hi");
        tableRow.addView(name);

        TextView age = new TextView(getActivity());
        name.setText("hihihi");
        tableRow.addView(age);

        return tableRow;
    }

    public TableRow getTableRow2(){
        TableRow tableRow = new TableRow(getActivity());
        TextView name = new TextView(getActivity());
        name.setText("hihihihihihiihihihhihihihi");
        tableRow.addView(name);

        return tableRow;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

