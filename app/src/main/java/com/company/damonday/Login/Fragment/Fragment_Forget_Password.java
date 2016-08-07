package com.company.damonday.Login.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.damonday.R;
import com.company.damonday.function.ProgressImage;

public class Fragment_Forget_Password extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private ProgressImage pDialog;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Forget_Password.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Forget_Password newInstance(String param1, String param2) {
        Fragment_Forget_Password fragment = new Fragment_Forget_Password();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Forget_Password() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Progress dialog
        pDialog = new ProgressImage(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.login_forget_password, container, false);
        getActivity().setTitle(R.string.forget_password);
        final EditText edEmail = (EditText) view.findViewById(R.id.email);
        Button cancel = (Button) view.findViewById(R.id.btnCancel);
        Button confirm = (Button) view.findViewById(R.id.btnComfirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((edEmail.getText().toString()).isEmpty())
                    Toast.makeText(getActivity(),
                            R.string.login_warning_forget_password_email, Toast.LENGTH_SHORT)
                            .show();
                else
                    sendEmail(edEmail.getText().toString());
            }
        });

        return view;
    }

    private void sendEmail(final String email) {

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
