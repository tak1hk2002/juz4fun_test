package com.company.damonday.function;

/**
 * Created by tom on 1/11/15.
 */


    import android.content.Context;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;

    public class ConnectionDetector {

        private Context _context;

        public ConnectionDetector(Context context){
            this._context = context;
        }

        public static boolean isConnectingToInternet(Context _context){
            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }

            }
            return false;
        }
    }



