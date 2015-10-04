package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.company.damonday.Login.SQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lamtaklung on 13/9/15.
 */
public class Fragment_ViewWriteComment extends Fragment {

    private SQLiteHandler db;
    private SessionManager session;
    private View view;
    private ListView listView;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // SqLite database handler
        db = new SQLiteHandler(activity);

        // session manager
        session = new SessionManager(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Log.d("isLoggedIn", Boolean.toString(session.isLoggedIn()));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!session.isLoggedIn()) {
            view = inflater.inflate(R.layout.login_login_or_register, container, false);
            Button btnLogin = (Button) view.findViewById(R.id.login);
            Button btnRegister = (Button) view.findViewById(R.id.register);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
            view = inflater.inflate(R.layout.view_companywritecomment, container, false);
            listView = (ListView) view.findViewById(R.id.listView);
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items,
                    R.layout.view_companywritecomment_list, new String[] {"title", "info"}, new int[] {R.id.title, R.id.info});
            listView.setAdapter(simpleAdapter);
        }

        return view;



    }

}
