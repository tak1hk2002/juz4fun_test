package com.company.damonday.CompanyInfo.Fragment.ViewCommentDetail;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.appyvet.rangebar.RangeBar;
import com.company.damonday.R;
import com.company.damonday.function.APIConfig;
import com.company.damonday.function.AppController;
import com.company.damonday.function.ProgressImage;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by lamtaklung on 18/9/15.
 */
public class Fragment_ViewCommentDetail extends Fragment {
    private NetworkImageView profilePic;
    private ImageView averagePic;
    private TextView title, date, username, averageScore, content, funScore, serviceScore, environmentScore, equipmentScore, priceScore;
    private APIConfig commentUrl;
    private ProgressImage pDialog;
    private JsonObjectRequest jsonObjReq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            //get id from previous page
            String commentId = getArguments().getString("comment_id");
            //new object for Api url
            commentUrl = new APIConfig(commentId);
            pDialog = new ProgressImage(getActivity());
            pDialog.show();
            Log.d("comment_id", commentId);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "No data loaded", Toast.LENGTH_LONG).show();
        }


        makeJsonArrayRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_companydetailcomment, container, false);

        profilePic = (NetworkImageView) view.findViewById(R.id.profile_pic);
        title = (TextView) view.findViewById(R.id.title);
        date = (TextView) view.findViewById(R.id.date);
        username = (TextView) view.findViewById(R.id.username);
        averagePic = (ImageView) view.findViewById(R.id.average_pic);
        averageScore = (TextView) view.findViewById(R.id.average_score);
        content = (TextView) view.findViewById(R.id.content);

        funScore = (TextView) view.findViewById(R.id.fun_score);
        serviceScore = (TextView) view.findViewById(R.id.service_score);
        environmentScore = (TextView) view.findViewById(R.id.environment_score);
        equipmentScore = (TextView) view.findViewById(R.id.equipment_score);
        priceScore = (TextView) view.findViewById(R.id.price_score);

        profilePic.setDefaultImageResId(R.drawable.mascot_die_pic);
        profilePic.setErrorImageResId(R.drawable.mascot_die_pic);
/*
        funScore.setEnabled(false);
        funScore.setBarColor(R.color.font_red);
        funScore.setConnectingLineColor(R.color.font_red);
        serviceScore.setEnabled(false);
        serviceScore.setBarColor(R.color.font_red);
        serviceScore.setConnectingLineColor(R.color.font_red);
        environmentScore.setEnabled(false);
        environmentScore.setBarColor(R.color.font_red);
        environmentScore.setConnectingLineColor(R.color.font_red);
        equipmentScore.setEnabled(false);
        equipmentScore.setBarColor(R.color.font_red);
        equipmentScore.setConnectingLineColor(R.color.font_red);
        priceScore.setEnabled(false);
        priceScore.setBarColor(R.color.font_red);
        priceScore.setConnectingLineColor(R.color.font_red);
*/


        return view;
    }

    private void makeJsonArrayRequest() {
        showpDialog();
        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                commentUrl.getUrlCommentDetail(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    int status = jsonObject.getInt("status");

                    if (status == 1) {
                        JSONObject commentInfo = jsonObject.getJSONObject("data");

                        getActivity().setTitle(commentInfo.getString("title"));

                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        profilePic.setImageUrl(commentInfo.getString("profile_picture"), imageLoader);
                        title.setText(commentInfo.getString("title"));
                        date.setText(commentInfo.getString("date"));
                        username.setText(commentInfo.getString("username"));
                        content.setText(commentInfo.getString("comment"));
                        averageScore.setText(commentInfo.getString("average_score"));
                        funScore.setText(commentInfo.getString("interest"));
                        serviceScore.setText(commentInfo.getString("service"));
                        environmentScore.setText(commentInfo.getString("environment"));
                        equipmentScore.setText(commentInfo.getString("equipment"));
                        priceScore.setText(commentInfo.getString("worth"));

                        if(Float.parseFloat(commentInfo.getString("average_score")) < 2.1)
                            averagePic.setImageResource(R.drawable.mascot_send_comment);
                        else if (Float.parseFloat(commentInfo.getString("average_score")) >= 2.1 && Float.parseFloat(commentInfo.getString("average_score")) <= 3.5)
                            averagePic.setImageResource(R.drawable.mascot_smile_comment);
                        else if (Float.parseFloat(commentInfo.getString("average_score")) > 3.5)
                            averagePic.setImageResource(R.drawable.mascot_happy_comment);

                    } else if(status == 0) {
                        String errorMsg = jsonObject.getString("msg");
                        Toast.makeText(getActivity(),
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    Log.d("error", "error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = getResources().getString(R.string.connection_fail_warning);
                } else if (error instanceof ServerError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof AuthFailureError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof ParseError) {
                    message = getResources().getString(R.string.connection_server_warning);
                } else if (error instanceof NoConnectionError) {
                    message = getResources().getString(R.string.connection_fail_warning);
                } else if (error instanceof TimeoutError) {
                    message = getResources().getString(R.string.connection_server_warning);
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    @Override
    public void onStop() {
        super.onStop();
        if (jsonObjReq != null)  {
            jsonObjReq.cancel();
            Log.d("onStop", "Comment detail requests are all cancelled");
        }
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
