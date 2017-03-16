package com.company.damonday.AccountVerified;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.company.damonday.R;
import com.company.damonday.TestActivity;
import com.company.damonday.function.ProgressImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountVerified.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountVerified#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountVerified extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    // TODO: Rename and change types of parameters
    private String mUsername;
    private String mToken;

    private OnFragmentInteractionListener mListener;
    private View view;
    private ProgressImage pDialog;
    private TextView verifiedResult;
    private Button btnConfirm;

    public AccountVerified() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     * @param token Parameter 2.
     * @return A new instance of fragment AccountVerified.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountVerified newInstance(String username, String token) {
        AccountVerified fragment = new AccountVerified();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
            mToken = getArguments().getString(ARG_TOKEN);
        }

        pDialog = new ProgressImage(getActivity());
        showpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.account_verified);

        view = inflater.inflate(R.layout.deeplink_verified, container, false);
        verifiedResult = (TextView) view.findViewById(R.id.verified_result);
        btnConfirm = (Button) view.findViewById(R.id.btnComfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestActivity) getActivity()).displayView(7);
            }
        });

        makeJsonArrayRequest(mUsername, mToken);

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

    private void makeJsonArrayRequest(String username, String token){
        hidepDialog();
        verifiedResult.setText(R.string.account_verified_success_msg);
        /*AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        ab.setTitle(R.string.account_verified_success_title);
        ab.setMessage(R.string.account_verified_success_msg);
        ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TestActivity) getActivity()).displayView(7);


            }
        });
        ab.create().show();*/
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
