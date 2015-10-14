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

import com.company.damonday.Login.Fragment.Fragment_Login;
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
//hi
    private SQLiteHandler db;
    private SessionManager session;
    private View view;
    private ListView listView;
    private AccessToken accessToken;
    private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // SqLite database handler
        db = new SQLiteHandler(activity);

        // session manager
        session = new SessionManager(activity);

        accessToken = AccessToken.getCurrentAccessToken();
        Log.d("accessToken", accessToken.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Log.d("isLoggedIn", Boolean.toString(session.isLoggedIn()));

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }

        };

        updateWithToken(AccessToken.getCurrentAccessToken());


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
                    Fragment_Login fragment_login = new Fragment_Login();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    System.out.println(fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("companyDetail"));
                    fragmentTransaction.add(R.id.frame_container, fragment_login, "login").addToBackStack(null);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

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

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            /*new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, GeekTrivia.class);
                    startActivity(i);

                    finish();
                }
            });*/
        } else {
            /*new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);

                    finish();
                }
            });*/
        }
    }

}
