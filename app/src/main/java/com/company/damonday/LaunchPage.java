package com.company.damonday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.damonday.CompanyInfo.Launch.LaunchSignUp;


/**
 * Created by lamtaklung on 23/6/2016.
 */
public class LaunchPage extends Fragment {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Fragment fragment = null;
                fragment = new LaunchSignUp();
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
                    fragmentTransaction.replace(R.id.frame_container, fragment, "launchsignup").addToBackStack("main");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.launch_page, container, false);
        return view;
    }


}
