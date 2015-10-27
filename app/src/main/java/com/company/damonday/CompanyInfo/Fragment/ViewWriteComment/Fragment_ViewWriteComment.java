package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.Login.SQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment extends Fragment {

    private View view;
    private ListView listView;
    private AccessToken accessToken;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
    private String entId;


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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.view_companywritecomment, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items,
                R.layout.view_companywritecomment_list, new String[] {"title", "info"}, new int[] {R.id.title, R.id.info});
        listView.setAdapter(simpleAdapter);


        return view;



    }


}
