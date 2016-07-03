package com.company.damonday.Setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_login_register;
import com.company.damonday.LatestComment.latestcommentvolley;
import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.Fragment.Fragment_Registration;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;
import com.company.damonday.Setting.Lib.RoundedTransformation;
import com.company.damonday.function.APIConfig;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by tom on 21/6/15.
 */
public class Setting extends Fragment {
    private LinearLayout islogouted, islogined;

    private SessionManager session;
    private LoginSQLiteHandler db;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);
        session = new SessionManager(getActivity());
        // SqLite database handler
        db = new LoginSQLiteHandler(getActivity());

        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getActionBar().setTitle(R.string.setting);
       // Log.d("setting","rr");
        getActivity().setTitle(R.string.setting);
        View view = inflater.inflate(R.layout.setting, container, false);
        islogined = (LinearLayout) view.findViewById(R.id.logined);
        islogouted = (LinearLayout)view.findViewById(R.id.logouted);
        Button btnSysLogout = (Button) view.findViewById(R.id.sys_logout);
        ImageView imgProfile = (ImageView) view.findViewById(R.id.profile_img);
        TextView txtUsername = (TextView) view.findViewById(R.id.username);
        TextView txtEmail = (TextView) view.findViewById(R.id.email);
        RelativeLayout onClick_aboutus =(RelativeLayout)view.findViewById(R.id.RA_aboutus);
        RelativeLayout onClick_feedback =(RelativeLayout)view.findViewById(R.id.RA_feedback);
        RelativeLayout onClickMyComment = (RelativeLayout)view.findViewById(R.id.RA_my_comment);
        //already logined
        if(session.isLoggedIn()){
            islogouted.setVisibility(View.GONE);
            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();
            String name = user.get("username");
            String email = user.get("email");
            txtUsername.setText(name);
            txtEmail.setText(email);


            Resources res = getResources();
            Bitmap originalProfilePic = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
            imgProfile.setImageBitmap(getRoundedShape(originalProfilePic));

            /*DisplayImageOptions options = new DisplayImageOptions.Builder()
                    // this will make circle, pass the width of image
                    .displayer(new RoundedBitmapDisplayer(getResources().getDimensionPixelSize(R.dimen.image_dimen_‌​menu)))
                    .cacheOnDisc(true)
                    .build();
            imageLoader.displayImage(url_for_image,ImageView,options);*/

            btnSysLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session.setLogin(false);
                    db.deleteUsers();


                    //refresh setting page
                    Setting setting = new Setting();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    //clear all of the fragment at the stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, setting, "setting").addToBackStack(null);

                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();



                }
            });


        }
        else if(AccessToken.getCurrentAccessToken() != null){
            islogouted.setVisibility(View.GONE);
            //btnSysLogout.setVisibility(View.GONE);
            //LoginButton btnFBLogout = (LoginButton) view.findViewById(R.id.fb_logout);


            Profile profile = Profile.getCurrentProfile();
            try {
                //get the profile and let it to be circular
                String profileImgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";
                Picasso.with(getActivity())
                        .load(profileImgUrl)
                        .transform(new RoundedTransformation(200, 0))
                        .fit()
                        .into(imgProfile);


                /*Glide.with(getActivity())
                        .load(profileImgUrl)
                        .into(imgProfile);*/
            }
            catch (Exception e){
                e.printStackTrace();
            }

            btnSysLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    Setting setting = new Setting();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    //clear all of the fragment at the stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, setting, "setting").addToBackStack(null);

                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

                }
            });

            /*btnFBLogout.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                //登入成功

                @Override
                public void onSuccess(LoginResult loginResult) {

                    LoginManager.getInstance().logOut();


                    //userFacebook(accessToken.getToken(), profile.getName(), accessToken.getUserId(), ImgUrl);


                }

                //登入取消

                @Override
                public void onCancel() {
                    // App code

                    Log.d("FB", "CANCEL");
                }

                //登入失敗

                @Override
                public void onError(FacebookException exception) {
                    // App code

                    Log.d("FB", exception.toString());
                }
            });*/


        }
        //ready for login
        else{
            islogined.setVisibility(View.GONE);
            Button btnLogin = (Button) view.findViewById(R.id.login);
            Button btnRegister = (Button) view.findViewById(R.id.register);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("lastFragment", "setting");

                    Fragment_Login fragment_login = new Fragment_Login();
                    fragment_login.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   // fragmentTransaction.replace(R.id.frame_container, fragment_login, "login").addToBackStack(null);

                    fragmentTransaction.hide(getFragmentManager().findFragmentByTag("setting"));
                    fragmentTransaction.add(R.id.frame_container, fragment_login, "login").addToBackStack(null);

                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment_Registration fragment_registration = new Fragment_Registration();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   // fragmentTransaction.replace(R.id.frame_container, fragment_registration, "register").addToBackStack(null);
                    fragmentTransaction.hide(getFragmentManager().findFragmentByTag("setting"));
                    fragmentTransaction.add(R.id.frame_container, fragment_registration, "register").addToBackStack(null);


                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

                }
            });
        }


        //click my comment list
        onClickMyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestcommentvolley latestcomment = new latestcommentvolley();
                Bundle bundle = new Bundle();
                bundle.putString("api", APIConfig.URL_Latest_Comment);
                bundle.putString("last_fragment_tag", "my_comment");
                latestcomment.setArguments(bundle);
                //getActivity().setTitle(R.string.my_comment);
                FragmentManager fragmentManager = getFragmentManager();
               // System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("setting"));
                fragmentTransaction.add(R.id.frame_container, latestcomment, "my_comment").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        });

        onClick_aboutus.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {

                //Bundle bundle = new Bundle();
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





        onClick_feedback.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {

                Bundle bundle = new Bundle();
//                bundle.putString("ent_id", Integer.toString(companyInfoItems.get(position).getEnt_id()));
                //              fragmentTabs_try = new FragmentTabs_try();
//                fragmentTabs_try.setArguments(bundle);
                FeedBack feedback_fragment = new FeedBack();

                FragmentManager fragmentManager = getFragmentManager();
                System.out.println(fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("setting"));
                fragmentTransaction.add(R.id.frame_container, feedback_fragment, "feedback").addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

        });

        return view;
    }

/*    @Override
    public void onResume() {
        super.onResume();
    }*/

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        Bitmap circleBitmap = Bitmap.createBitmap(scaleBitmapImage.getWidth(), scaleBitmapImage.getHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        Canvas c = new Canvas(circleBitmap);
        //This draw a circle of Gerycolor which will be the border of image.
        c.drawCircle(scaleBitmapImage.getWidth()/2, scaleBitmapImage.getHeight()/2, scaleBitmapImage.getWidth()/2, paint);
        BitmapShader shader = new BitmapShader(scaleBitmapImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setAntiAlias(true);
        paint.setShader(shader);
// This will draw the image.
        c.drawCircle(scaleBitmapImage.getWidth()/2, scaleBitmapImage.getHeight()/2, scaleBitmapImage.getWidth()/2-2, paint);
        return circleBitmap;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



    @Override
    public void onResume() {
        super.onResume();



        // Logs 'install' and 'app activate' App Events.
        /*Profile profile = Profile.getCurrentProfile();
        //info.setText(message(profile));
        try {
            String profileImgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";

            Glide.with(MainActivity.this)
                    .load(profileImgUrl)
                    .into(profileImgView);
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
        AppEventsLogger.activateApp(getActivity().getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(getActivity().getApplicationContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
