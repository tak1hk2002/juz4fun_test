package com.company.damonday.function.DeepLink;

import android.accounts.Account;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.company.damonday.AccountVerified.AccountVerified;
import com.company.damonday.CompanyInfo.FragmentTabs_try;
import com.company.damonday.R;
import com.company.damonday.ResetPassword.ResetPassword;
import com.company.damonday.Search.FastSearch;

/**
 * Created by lamtaklung on 21/2/2017.
 */

public class DeepLinkSwitch {

    private Uri link;
    private Activity activity;

    public DeepLinkSwitch(Uri link) {
        this.link = link;
    }

    public String getHost(){
        return link.getHost();
    }

    public String getParamater(String key){
        return link.getQueryParameter(key);
    }

    public Fragment goToFragment(){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (getHost()){
            case "verified":
                String username = getParamater("username");
                String token = getParamater("token");
                System.out.println("username: "+ username);
                System.out.println("token: "+ token);

                bundle.putString("username", username);
                bundle.putString("token", token);
                fragment = AccountVerified.newInstance(username, token);
                fragment.setArguments(bundle);
                //go to verified page
                break;
            case "resetpassword":
                String email = getParamater("email");
                String resetToken = getParamater("reset_token");
                System.out.println("email: "+ email);
                System.out.println("resetToken: "+ resetToken);

                bundle.putString("email", email);
                bundle.putString("reset_token", resetToken);
                fragment = ResetPassword.newInstance(email, resetToken);
                fragment.setArguments(bundle);
                break;
            case "entinfo":
                break;
            default:
        }
        return fragment;
    }


}
