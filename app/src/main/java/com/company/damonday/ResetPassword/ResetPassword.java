package com.company.damonday.ResetPassword;

import android.content.Context;
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
import com.company.damonday.TestActivity;
import com.company.damonday.function.ProgressImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResetPassword.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResetPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EMAIL = "email";
    private static final String RESETTOKEN = "reset_token";

    // TODO: Rename and change types of parameters
    private String mEmail;
    private String mResetToken;

    private OnFragmentInteractionListener mListener;
    private EditText inputPassword, inputConfirmedPassword;
    private Button btnCancel, btnConfirm;
    private View view;
    private ProgressImage progressImage;

    public ResetPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @param resetToken Parameter 2.
     * @return A new instance of fragment ResetPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPassword newInstance(String email, String resetToken) {
        ResetPassword fragment = new ResetPassword();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        args.putString(RESETTOKEN, resetToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("aaajfkdljflkdjsfkjsdkjflksdjflklds");
        if (getArguments() != null) {
            mEmail = getArguments().getString(EMAIL);
            mResetToken = getArguments().getString(RESETTOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.reset_password);

        view = inflater.inflate(R.layout.deeplink_reset_password, container, false);
        inputPassword = (EditText) view.findViewById(R.id.password);
        inputConfirmedPassword = (EditText) view.findViewById(R.id.confirmed_password);
        btnConfirm = (Button) view.findViewById(R.id.btnComfirm);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        // Progress dialog
        progressImage = new ProgressImage(getActivity());

        // Register Button Click event
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String password = inputPassword.getText().toString();
                String confirmedPassword = inputConfirmedPassword.getText().toString();

                if (password.isEmpty()) {
                    Toast.makeText(getActivity(),
                            R.string.hint_password, Toast.LENGTH_LONG)
                            .show();

                }
                else if(!password.equals(confirmedPassword)){
                    Toast.makeText(getActivity(),
                            R.string.hint_confirmed_password, Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    //registerUser(mEmail, mResetToken, password);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                ((TestActivity) getActivity()).displayView(7);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
