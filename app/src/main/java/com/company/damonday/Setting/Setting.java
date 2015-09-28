package com.company.damonday.Setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.company.damonday.R;

/**
 * Created by tom on 21/6/15.
 */
public class Setting extends Fragment {
private LinearLayout islogouted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        islogouted = (LinearLayout)view.findViewById(R.id.logined);
        islogouted.setVisibility(View.GONE);



        Button button_aboutus =(Button)view.findViewById(R.id.button_aboutus);
        button_aboutus.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {

                Bundle bundle = new Bundle();
//                bundle.putString("ent_id", Integer.toString(companyInfoItems.get(position).getEnt_id()));
                //              fragmentTabs_try = new FragmentTabs_try();
//                fragmentTabs_try.setArguments(bundle);
                AboutUs about_us_fragment = new AboutUs();

                FragmentManager fragmentManager = getFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("setting"));
                fragmentTransaction.add(R.id.frame_container, about_us_fragment, "about_us").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

        });



        return view;
    }

}
