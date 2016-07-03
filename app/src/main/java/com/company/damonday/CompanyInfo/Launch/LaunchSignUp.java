package com.company.damonday.CompanyInfo.Launch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.company.damonday.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

/**
 * Created by lamtaklung on 23/6/2016.
 */
public class LaunchSignUp extends Fragment {

    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launch_sign_up, container, false);
        Button btnEmail = (Button) view.findViewById(R.id.btn_email);
        Button btnSkip = (Button) view.findViewById((R.id.btn_skip));
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.btn_fb_login);
        loginButton.setFragment(this);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }
}
