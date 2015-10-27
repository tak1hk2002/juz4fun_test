package com.company.damonday.CompanyInfo.Fragment.ViewWriteComment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.R;

/**
 * Created by lamtaklung on 27/10/2015.
 */
public class Fragment_login_register extends Fragment {
    private View view;
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
        view = inflater.inflate(R.layout.login_login_or_register, container, false);
        Button btnLogin = (Button) view.findViewById(R.id.login);
        Button btnRegister = (Button) view.findViewById(R.id.register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass variable to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("ent_id", entId);
                Fragment_Login fragment_login = new Fragment_Login();
                fragment_login.setArguments(bundle);

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
                //pass variable to next fragment
                Bundle bundle = new Bundle();
                bundle.putString("writeComment", "register");
                Fragment_Registration fragment_registration = new Fragment_Registration();
                fragment_registration.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("companyDetail"));
                fragmentTransaction.add(R.id.frame_container, fragment_registration, "register").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }
        });

        return view;

    }
}
