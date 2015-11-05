package com.company.damonday.Setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.company.damonday.CompanyInfo.Fragment.ViewWriteComment.Fragment_login_register;
import com.company.damonday.Login.Fragment.Fragment_Login;
import com.company.damonday.Login.LoginSQLiteHandler;
import com.company.damonday.Login.SessionManager;
import com.company.damonday.R;
import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;

/**
 * Created by tom on 21/6/15.
 */
public class Setting extends Fragment {
    private LinearLayout islogouted, islogined;

    private SessionManager session;
    private LoginSQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.setting);
        session = new SessionManager(getActivity());
        // SqLite database handler
        db = new LoginSQLiteHandler(getActivity());



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle(R.string.setting);
        View view = inflater.inflate(R.layout.setting, container, false);
        islogined = (LinearLayout) view.findViewById(R.id.logined);
        islogouted = (LinearLayout)view.findViewById(R.id.logouted);
        Button btnSysLogout = (Button) view.findViewById(R.id.sys_logout);
        LoginButton btnFBLogout = (LoginButton) view.findViewById(R.id.fb_logout);
        ImageView imgProfile = (ImageView) view.findViewById(R.id.profile_img);
        Log.d("isLoggedIn", Boolean.toString(session.isLoggedIn()));
        //already logined
        if(session.isLoggedIn()){
            islogouted.setVisibility(View.GONE);
            //btnFBLogout.setVisibility(View.GONE);


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

                    //display message login successfully
                    AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                    ab.setTitle(R.string.logout_success);
                    ab.setNeutralButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                    ab.create().show();



                }
            });


        }
        else if(AccessToken.getCurrentAccessToken() != null){
            islogouted.setVisibility(View.GONE);
            btnSysLogout.setVisibility(View.GONE);

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
                    fragmentTransaction.replace(R.id.frame_container, fragment_login, "login").addToBackStack(null);

                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();

                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }





        Button button_aboutus =(Button)view.findViewById(R.id.button_aboutus);
        Button button_feedback =(Button)view.findViewById(R.id.button_feedback);
        button_aboutus.setOnClickListener(new Button.OnClickListener() {

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

        button_feedback.setOnClickListener(new Button.OnClickListener() {

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

}
