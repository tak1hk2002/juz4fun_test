package com.company.damonday.Launch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.company.damonday.Home.Home;
import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import static com.company.damonday.Launch.LaunchPage.PREFS_NAME;

/**
 * Created by lamtaklung on 23/6/2016.
 */
public class LaunchSignUp extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        //Set "hasLoggedIn" to true
        editor.putBoolean("hasDisplayedLaunchLogin", true);

        // Commit the edits!
        editor.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set bar title
        getActivity().setTitle(R.string.launchpage);

        View view = inflater.inflate(R.layout.launch_sign_up, container, false);
        Button btnRegister = (Button) view.findViewById(R.id.btn_register);
        Button btnSkip = (Button) view.findViewById((R.id.btn_skip));
        Button btnLogin = (Button) view.findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Fragment_Login();
                // This method will be executed once the timer is over
                // Start your app main activity
                /*Intent i = new Intent(SplashActivity.this, TestActivity.class);
                startActivity(i);*/
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //clear all of the fragment at the stack
                    //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //System.out.println(fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment, "login").addToBackStack("main");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Fragment_Registration();
                // This method will be executed once the timer is over
                // Start your app main activity
                /*Intent i = new Intent(SplashActivity.this, TestActivity.class);
                startActivity(i);*/
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //clear all of the fragment at the stack
                    //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //System.out.println(fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment, "register").addToBackStack("main");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Home();
                getActivity().getActionBar().show();
                // This method will be executed once the timer is over
                // Start your app main activity
                /*Intent i = new Intent(SplashActivity.this, TestActivity.class);
                startActivity(i);*/
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //clear all of the fragment at the stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //System.out.println(fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment, "home").addToBackStack("main");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }
            }
        });


        return view;
    }
}
